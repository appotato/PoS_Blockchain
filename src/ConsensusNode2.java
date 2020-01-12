import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The ConsensusEvaluation project implements an application to
 * simulate decentralized network with various consensus protocol
 * @author Hoan
 * @since 20/05/2019
 */


public class ConsensusNode2 {
    private static final int port = 8333;
    private static ArrayList<String> IPList = new ArrayList<String>();
    private static String name;
    private static int protocol;
    private static Scanner reader = new Scanner(System.in);

    /**
     *This is the main method
     * This method simply provides a user friendly interface to start/connect to a decentralized network
     * Register library BouncyCastle as the security provider because this security provides many apis for cryptographic
     * To start a decentralized network users must choose a protocol and open a server at port 8333
     * To connect to a decentralized network users send a CONNECTION message and wait for network information reply
     * @param args Unused
     *
     */
    public static void main(String[] args) {
        Provider newProvider = new BouncyCastleProvider();
        Security.addProvider(newProvider);
        startAnnouncement();
        name = reader.nextLine();
        if (name.isEmpty()){
            name = "Hoan";
        }
        showConnectionOptions();

        try {
            int connection = Integer.parseInt(reader.nextLine());
            if (connection == 0){
                PoS_Node.runAsFirstNode();

            } else {

                showInstructionsToConnectToAnExistingNetwork();
                String knownIPAddress = reader.nextLine();
                ConnectionMessage newConnectionMessage = new ConnectionMessage();

                try {
                    Socket newSocket = new Socket(knownIPAddress,port);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(newSocket.getOutputStream());
                    objectOutputStream.writeObject(newConnectionMessage);

                    ObjectInputStream objectInputStream = new ObjectInputStream(newSocket.getInputStream());

                    objectOutputStream.flush();


                    ProtocolMessage receivedProtocolMessage = (ProtocolMessage) objectInputStream.readObject();
                    IPListMessage receivedIPListMessage = (IPListMessage) objectInputStream.readObject();

                    objectInputStream.close();
                    objectOutputStream.close();
                    newSocket.close();

                    System.out.println("======================================================");
                    protocol = receivedProtocolMessage.getProtocol();
                    System.out.println("Protocol Message received!");
                    IPList.add(knownIPAddress);
                    System.out.println("IPList Message received!");
                    for (String ipAddress:receivedIPListMessage.getIPList()){
                        IPList.add(ipAddress);
                    }
                    System.out.println("IPList updated!");
                    System.out.println("======================================================");

                    showRoutingOptions();
                    boolean wallet = true;
                    int routing = Integer.parseInt(reader.nextLine());
                    if (routing == 1){
                    } else {
                        wallet = false;
                    }
                    PoS_Node.run(wallet);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException e){
                    e.printStackTrace();
                } catch (NumberFormatException e){
                    e.printStackTrace();
                }


            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    private static void showConnectionOptions() {
        System.out.println("======================================================");
        System.out.println("Press 0 to start a new network");
        System.out.println("Press 1 to connect to an existing network");
        System.out.println("======================================================");
    }

    private static void showRoutingOptions() {
        System.out.println("======================================================");
        System.out.println("Press 0 to run as node");
        System.out.println("Press 1 to run as Wallet");
        System.out.println("======================================================");
    }

    private static void showInstructionsToConnectToAnExistingNetwork() {
        System.out.println("======================================================");
        System.out.println("This requires at least an IP address of a node in the network!");
        System.out.println("Input the IP address of a node:");
        System.out.println("======================================================");
    }

    private static void startAnnouncement() {
        System.out.println("======================================================");
        System.out.println("Hello, I am a tool to simulate decentralized networks!");
        System.out.println("(This is to ensure data from different users are not messed when using virtual machines)");
        System.out.println("Please input your name:");
        System.out.println("======================================================");
    }

    public static ArrayList<String> getIPList(){
        return IPList;
    }

    public static String getName(){
        return name;
    }

    public static int getProtocol(){
        return protocol;
    }
     static void setProtocol(int protocol) {
        ConsensusNode2.protocol = protocol;
    }

}
