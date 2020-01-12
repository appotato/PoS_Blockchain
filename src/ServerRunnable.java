import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This runnable class is to start a server thread.
 * This runnable is designed to be used by all protocols.
 */
public class ServerRunnable implements Runnable {
	private int port;
	private int protocol;
	
	public ServerRunnable(int port, int protocol) {
		this.port = port;
		this.protocol = protocol;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

					String ipAddress = socket.getInetAddress().getHostAddress();
					
					Message receivedMessage = (Message) objectInputStream.readObject();

					if (receivedMessage.getType().equals("CONNECTION")) {
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						System.out.println("======================================================");
						System.out.println("Received connection request!");

						ProtocolMessage newProtocolMessage = new ProtocolMessage(ConsensusNode2.getProtocol());
						objectOutputStream.writeObject(newProtocolMessage);
						ArrayList<String> ipList =  PoS_Node.getNodeIPList();

						IPListMessage newIPListMessage = new IPListMessage(ipList);



						objectOutputStream.writeObject(newIPListMessage);
						objectOutputStream.flush();
						objectOutputStream.close();
						System.out.println("======================================================");
					} else {
						PoS_Node.handleReceivedMessage(receivedMessage, ipAddress);
					}

					objectInputStream.close();
					socket.close();


				} catch (EOFException ex){
					ex.printStackTrace();
				} catch (ClassNotFoundException ex2) {
					ex2.printStackTrace();
				} finally {
					try {
						if (socket != null) socket.close();
					} catch (IOException ex) {}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) serverSocket.close();
			} catch (IOException ex){}
		}
	}


}
