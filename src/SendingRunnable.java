import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This runnable class is used to send messages
 * to other nodes in the network
 */
public class SendingRunnable implements Runnable {
	private Socket nodeSocket;
	private Message message;

	public SendingRunnable(Socket nodeSocket, Message message){
		this.nodeSocket = nodeSocket;
		this.message = message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(nodeSocket.getOutputStream());
			try{
				objectOutputStream.writeObject(message);
				objectOutputStream.flush();
				objectOutputStream.close();
			} catch (Exception ex){
				ex.printStackTrace();
			}
		} catch (IOException ex){
			ex.printStackTrace();
		} 
	}
}
