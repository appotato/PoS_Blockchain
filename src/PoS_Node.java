import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a PoS node/wallet
 * It can be divided in to two parts
 *   Run as a PoS node
 *   Run as a PoS wallet
 *  To run as a node it is necessary to have some stakes
 *  so users will be asked to a number they want
 */
public class PoS_Node {
    private static volatile ArrayList<PoSBlock> blockchain = new ArrayList<PoSBlock>();
    private static volatile ArrayList<PoS_Transaction> transactions = new ArrayList<PoS_Transaction>();
    private static String publicKey;
    private static String privateKey;
    private static Scanner reader = new Scanner(System.in);
    private static final int port = 8333;
    private static final int protocol = 2;
    private static ArrayList<String> nodeIPList = new ArrayList<String>();
    private static int stake = 0;
    private static boolean firstNode = false;
    private static volatile ArrayList<Integer> stakeDistribution = new ArrayList<>();
    private static volatile ArrayList<String> candidateList = new ArrayList<>();
    private static volatile String leader;

    public static void run (boolean wallet){
        ConsensusNode2.setProtocol(protocol);
        if (wallet) runAsWallet();
        else runAsNode();
    }
    private static void runAsNode(){
        for(String ip:ConsensusNode2.getIPList()){
            nodeIPList.add(ip);
        }
        System.out.println("======================================================");
        System.out.println("Run as a node, protocol: PoS!");
        System.out.println("======================================================");
        startServerThread();
        PoS_Blockchain_Request_Message newBlockchainRequestMessage = new PoS_Blockchain_Request_Message();
        sendMessage(newBlockchainRequestMessage, nodeIPList.get(0));
        while (stake <= 0){
            showStakeCommand();
            try {
                stake = Integer.parseInt(reader.nextLine());
            } catch (NumberFormatException e) {
            }
        }
        PoS_Transaction newNodeTransaction = new PoS_Transaction(publicKey, publicKey, Integer.toString(stake));
        newNodeTransaction.hasNewNode();
        newNodeTransaction.generateSignature(privateKey);
        PoS_Transaction_Message newNodeMessage = new PoS_Transaction_Message(newNodeTransaction);
        broadcastMessage(newNodeMessage, nodeIPList);
    }

    /**
     * Run as a wallet with many functions:
     * 0/. Creating a new wallet
     * 1/. Loading an existing wallet (this function is not implemented in this protocol because it is unnecessary
     * 2/. Testing the protocol by making nodes create a number of block by choice
     * 3/. See the result of the test, can also be used to see the result of an attack from option 6
     * 4/. Check for the blockchain in a list of hashString form, mostly used to manually check if two blockchains are identical
     * 5/. Check for the full version of the blockchain
     * 6/. Simulate an attack with X fake transactions in a pool of total 1000 transactions and Y bad nodes (these nodes
     * only choose to mine fake transaction
     */
    private static void runAsWallet(){
        nodeIPList = ConsensusNode2.getIPList();
        System.out.println("======================================================");
        showWalletOptions();
        try {
            int choice = Integer.parseInt(reader.nextLine());

            while (choice != 12){
                switch (choice){
                    case 0:
                        createNewWallet();
                        showWalletCommand();
                        int choice2 = Integer.parseInt(reader.nextLine());
                        while (choice2 == 0){
                            sendTransaction();
                            showWalletCommand();
                            choice2 = Integer.parseInt(reader.nextLine());
                        }
                        break;
                    case 1:
                        System.out.println("This option is not necessary for the current protocol!");
                        break;
                    case 2:
                        showTestOptions();
                        int numberOfTransactionsTested = Integer.parseInt(reader.nextLine());
                        Test_Message newTestMessage = new Test_Message(numberOfTransactionsTested);
                        broadcastMessage(newTestMessage, nodeIPList);
                        break;
                    case 3:
                        Test_Result_Message newTestResultMessage = new Test_Result_Message();
                        broadcastMessage(newTestResultMessage, nodeIPList);
                        break;
                    case 4:
                        Show_Ledger_Message newShowMessage = new Show_Ledger_Message();
                        broadcastMessage(newShowMessage, nodeIPList);
                        break;
                    case 5:
                        Show_Full_Ledger_Message newShowFullMessage = new Show_Full_Ledger_Message();
                        broadcastMessage(newShowFullMessage, nodeIPList);
                        break;
                    case 6:
                        showAttackOptions1();
                        int numberOfFakeTransactions = Integer.parseInt(reader.nextLine());
                        showAttackOptions2();
                        int numberOfBadNodes = Integer.parseInt(reader.nextLine());
                        sendingAttack(numberOfFakeTransactions, numberOfBadNodes);
                        break;
                    case 7:
                        DPoS_Info_Message newInfoMess = new DPoS_Info_Message();
                        broadcastMessage(newInfoMess, nodeIPList);
                        break;
                    case 9:
                        PoS_New_Round_Message newRoundMess = new PoS_New_Round_Message();
                        broadcastMessage(newRoundMess, nodeIPList);
                        break;
                    case 10:
                        PoS_Test_Prepare_Message prepareMessage = new PoS_Test_Prepare_Message();
                        broadcastMessage(prepareMessage, nodeIPList);
                        break;
                    case 11:
                        for (int i = 0; i < 10; i++){
                            PoS_New_Round_Message newRoundMessage = new PoS_New_Round_Message();
                            broadcastMessage(newRoundMessage, nodeIPList);
                            TimeUnit.SECONDS.sleep(20);
                        }
                        break;
                    default:
                        break;


                }
                showWalletOptions();
                choice = Integer.parseInt(reader.nextLine());
            }

        } catch (NumberFormatException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("======================================================");
    }

    /**
     * This method sends an attack message with a specific number of fake transactions
     * and a specific number of bad nodes
     * @param fakeTx
     * @param badNode
     */
    private static void sendingAttack(int fakeTx, int badNode){
        for(int i = 0; i < nodeIPList.size(); i++){
            boolean isBad = false;
            if (i < badNode) isBad = true;
            Attack_Message newAttackMessage = new Attack_Message(isBad, fakeTx);
            sendMessage(newAttackMessage, nodeIPList.get(i));
        }
    }

    private static void showAttackOptions1(){
        System.out.println("======================================================");
        System.out.println("How many fake transactions?");
        System.out.println("======================================================");
    }

    private static void showAttackOptions2(){
        System.out.println("======================================================");
        System.out.println("How many bad nodes?");
        System.out.println("======================================================");
    }

    private static void showTestOptions(){
        System.out.println("======================================================");
        System.out.println("How many transactions to test? ");
        System.out.println("======================================================");
    }

    public static void runAsFirstNode(){
        ConsensusNode2.setProtocol(protocol);
        startServerThread();
        System.out.println("======================================================");
        System.out.println("Running as first node, protocol: PoS!");
        System.out.println("======================================================");
        while (stake <= 0){
            showStakeCommand();
            try {
                stake = Integer.parseInt(reader.nextLine());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        createGenesisBlock();
        candidateList.add(publicKey);
        stakeDistribution.add(stake);
        leader = publicKey;
        firstNode = true;
    }

    /**
     * This method creates a genesis block
     */
    private static void createGenesisBlock(){
        String genesisData = "T.H.I.S.I.S.G.E.N.E.S.I.S";
        PoS_Transaction genesisTransaction = new PoS_Transaction(publicKey, publicKey, Integer.toString(stake));
        genesisTransaction.hasNewNode();
        genesisTransaction.generateSignature(privateKey);
        PoSBlock genesisBlock = new PoSBlock(genesisData, genesisTransaction);
        genesisBlock.calculateHash();
        genesisBlock.setValidator(publicKey);
        genesisBlock.generateSignature(privateKey);
        blockchain.add(genesisBlock);
    }


    private static void startServerThread() {
        Runnable serverRunnable = new ServerRunnable(port,protocol);
        Thread serverThread = new Thread(serverRunnable);
        serverThread.start();
    }

    /**
     * Send a serialized message to a specific node
     * @param message
     * @param IPAddress
     */
    public static void sendMessage(Message message, String IPAddress){
        Socket nodeSocket = null;
        try {
            nodeSocket = new Socket(IPAddress, port);
            Runnable sendingRunnable = new SendingRunnable(nodeSocket, message);
            Thread sendingThread = new Thread (sendingRunnable);
            sendingThread.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Broadcast a serialized message to all node in the network
     * @param message
     * @param IPList
     */
    public static void broadcastMessage(Message message, ArrayList<String> IPList) {
        for (String nodeIPAddress:IPList) {
            sendMessage(message, nodeIPAddress);
        }
    }

    private static void createNewWallet(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = StringUtil.getStringFromKey(keyPair.getPrivate());
            publicKey = StringUtil.getStringFromKey(keyPair.getPublic());
            System.out.println("======================================================");
            System.out.println("New wallet created!");
            System.out.println("Your Public Key: " + publicKey);
            System.out.println("Your Private Key is hidden!");
            System.out.println("======================================================");
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void showWalletCommand(){
        System.out.println("======================================================");
        System.out.println("Press 0 to send transactions to the network!");
        System.out.println("Press 1 to end!");
        System.out.println("======================================================");
    }

    /**
     * Broadcast transaction to the network
     */
    private static void sendTransaction(){
        System.out.println("Type in message to send");
        String content = reader.nextLine();

        System.out.println("Choose recipient");
        Helper.printPublicKeys();

        try {
            int choice = Integer.parseInt(reader.nextLine());
            String recipient = Helper.getPublicKey(choice);

            PoS_Transaction newTransaction = new PoS_Transaction(publicKey, recipient, content);
            newTransaction.generateSignature(privateKey);

            PoS_Transaction_Message newTransactionMessage = new PoS_Transaction_Message(newTransaction);

            broadcastMessage(newTransactionMessage, nodeIPList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void showStakeCommand(){
        System.out.println("======================================================");
        createNewWallet();
        System.out.println("You need some stakes to take part in the consensus protocol!");
        System.out.println("Submit the number of stakes you want!");
        System.out.println("======================================================");
    }

    /**
     * The most important method of this class, defining how the PoW protocol works
     * This method presents how to respond to various messages from other nodes
     * Blockchain_Request : this is sent by a new node joining the network, simply reply with the current
     * blockchain attached
     * New_Node: a new node just joins the network
     * Blockchain: reply to the blockchain request, get the blockchain attached to it
     * Transaction: a transaction is broadcasted, store it if it is valid
     * New_Block: another node has minted a new block, check if the validator is correct
     * Test: a Wallet wants to test the network, create some sample transactions and start to mint them
     * Test_Result: the request for the result of the test
     * Attack: an attack is simulated, create X fake transactions in a pool of 1000 transactions,
     * if chosen as a bad block this node can only mint fake transactions
     * Show: a request for showing the blockchain in form of a list of hashString
     * Show_Full: a request to print the whole current ledger
     * Info: a request for the current node list and stake distribution
     *
     * @param receivedMessage
     * @param ipAddress
     */
    public static void handleReceivedMessage(Message receivedMessage, String ipAddress) {
        switch (receivedMessage.getType()) {
            case "POS_BLOCKCHAIN_REQ":
                System.out.println("======================================================");
                System.out.println("Blockchain request message received!");
                System.out.println("======================================================");
                PoS_Blockchain_Message newPoW_Blockchain_Message = new PoS_Blockchain_Message(blockchain);
                sendMessage(newPoW_Blockchain_Message, ipAddress);
                NewNodeMessage newNewNodeMessage = new NewNodeMessage(ipAddress);
                broadcastMessage(newNewNodeMessage, nodeIPList);
                handleNewNode(ipAddress);
                break;
            case "NEW_NODE":
                NewNodeMessage receivedNewNodeMessage = (NewNodeMessage) receivedMessage;
                String receivedIpAdress = receivedNewNodeMessage.getIpAddress();
                handleNewNode(receivedIpAdress);
                break;
            case "POS_BLOCKCHAIN":
                System.out.println("======================================================");
                System.out.println("Blockchain message received!");
                PoS_Blockchain_Message blockchain_message = (PoS_Blockchain_Message) receivedMessage;
                blockchain = blockchain_message.getBlockchain();
                updateStakeDistribution(blockchain);
                findNewLeader(getHighestBlock());
                System.out.println("======================================================");
                break;
            case "POS_TRANSACTION":
                System.out.println("======================================================");
                System.out.println("Transaction message received!");
                PoS_Transaction_Message receivedTransactionMessage = (PoS_Transaction_Message)receivedMessage;
                PoS_Transaction receivedTransaction = receivedTransactionMessage.getTransaction();
                if (receivedTransaction.verifySignature()){
                    transactions.add(receivedTransaction);
                }
                System.out.println("======================================================");
                break;
            case "POS_NEW_BLOCK":
                PoS_Block_Message receivedPoSBlockMessage = (PoS_Block_Message)receivedMessage;
                PoSBlock receivedBlock = receivedPoSBlockMessage.getNewBlock();
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                if (getHighestBlock().getHash().equals(receivedBlock.getPreviousHash()) &&
                        receivedBlock.getValidator().equals(leader) &&
                        receivedBlock.verifySignature()){

                    blockchain.add(receivedBlock);
                    if (receivedBlock.getTransaction() != null){
                        PoS_Transaction blockTransaction = receivedBlock.getTransaction();
                        if (transactions.contains(blockTransaction)) transactions.remove(blockTransaction);
                        handleNewNodeBlock(blockTransaction);
                    }
                    findNewLeader(receivedBlock);
                } else {
                    PoS_Blockchain_Check_Request_Message check_request_message = new PoS_Blockchain_Check_Request_Message();
                    sendMessage(check_request_message, ipAddress);
                }
                break;
            case "TEST_PREPARE":
                transactions.addAll(Helper.createTestPoSTransactions2(1000, 0));
                break;
            case "TEST_RESULT":
                System.out.println("======================================================");
                System.out.println("======================================================");
                System.out.println("Start time: " + blockchain.get(1).getTimeStamp());
                int blockchainHeight = blockchain.size();
                System.out.println("End time: " + blockchain.get(blockchainHeight - 1).getTimeStamp());
                int count = 0;
                for (PoSBlock block: blockchain){
                    if (block.getTransaction().isGood()){
                        count++;
                    }
                }
                System.out.println("Number of good blocks: " + count);
                System.out.println("======================================================");
                break;
            case "SHOW":
                System.out.println("======================================================");
                printCurrentBlockchainInHash();
                System.out.println("======================================================");
                break;
            case "SHOW_FULL":
                System.out.println("======================================================");
                printCurrentBlockchain();
                System.out.println("======================================================");
                break;
            case "POS_BLOCKCHAIN_CHECK_REQ":
                PoS_Blockchain_Check_Message blockchainMessage = new PoS_Blockchain_Check_Message(blockchain);
                sendMessage(blockchainMessage, ipAddress);
                break;
            case "POS_BLOCKCHAIN_CHECK":
                System.out.println("======================================================");
                System.out.println("Blockchain check message received!");
                PoS_Blockchain_Check_Message blockchain_check_message = (PoS_Blockchain_Check_Message) receivedMessage;
                ArrayList<PoSBlock> checkedBlockchain = blockchain_check_message.getBlockchain();

                if (isCheckedBlockchainValid(checkedBlockchain) && checkedBlockchain.size() > blockchain.size()){
                    blockchain = checkedBlockchain;
                    findNewLeader(getHighestBlock());
                }


                System.out.println("======================================================");
                break;
            case "POS_NEW_CONSENSUS_ROUND":
                System.out.println("======================================================");
                System.out.println("New Consensus Round");
                if (leader.equals(publicKey)){
                    mintNewBlock();
                }
                break;
            case "DPOS_INFO":
                printCurrentCandidateListAndStakeDistribution();
                break;
        }
    }

    /**
     * Choose a transaction and create a block with users' private keys
     * This method runs when the user is the next leader
     */
    private static void mintNewBlock(){
        System.out.println("======================================================");
        System.out.println("Minting new block...");
        PoS_Transaction newTransaction = new PoS_Transaction("", "", "");
        newTransaction.setHash("");
        if (!transactions.isEmpty()){
            newTransaction = transactions.get(0);
            transactions.remove(0);
        }
        PoSBlock newBlock = new PoSBlock(getHighestBlock().getHash(), newTransaction);
        newBlock.setValidator(publicKey);
        newBlock.generateSignature(privateKey);
        newBlock.calculateHash();

        PoS_Block_Message newBlockMessage = new PoS_Block_Message(newBlock);
        broadcastMessage(newBlockMessage, nodeIPList);
        blockchain.add(newBlock);


        if (newTransaction.newNode() && newTransaction.getSender().equals(newTransaction.getRecipient())){
            try {
                int newNodeStake = Integer.parseInt(newTransaction.getContent());
                candidateList.add(newTransaction.getSender());
                stakeDistribution.add(newNodeStake);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("======================================================");
        findNewLeader(getHighestBlock());
    }

    private static PoSBlock getHighestBlock(){
        return blockchain.get(blockchain.size() - 1);
    }

    /**
     * This method follow FTS to find the next block leader
     * @param block
     */
    private static void findNewLeader(PoSBlock block){
        System.out.println("======================================================");
        System.out.println("Calculating new leader!");
        String hash = block.getHash();
        hash = StringUtil.applySHA256(hash);
        int followTheSatoshi =  Integer.parseInt(hash.substring(0, 2), 16);
        float winningTicketIndex = getTotalStake()*followTheSatoshi/256;
        System.out.println("Winning index is " + (int)winningTicketIndex);
        leader = candidateList.get(findLotteryWinnerIndex(winningTicketIndex));
        System.out.println("New Leader is: " + leader);
        System.out.println("======================================================");
    }

    private static int getTotalStake(){
        int totalStake = 0;
        for(int stake:stakeDistribution){
            totalStake += stake;
        }
        return totalStake;
    }

    private static int findLotteryWinnerIndex(float winnerIndex) {
        int i = 0;
        int value = stakeDistribution.get(i);
        while (value < winnerIndex) {
            i++;
            value += stakeDistribution.get(i);
        }
        return i;
    }

    /**
     * A new node joins the network, save their IP address
     * @param ipAddress
     */
    private static void handleNewNode(String ipAddress){
        System.out.println("======================================================");
        System.out.println("New node connected to the network!");
        if(!(nodeIPList.contains(ipAddress))){
            nodeIPList.add(ipAddress);
        }
        System.out.println(nodeIPList);
        System.out.println("======================================================");
    }

    /**
     * This methods shows the blockchain in JSON format
     */
    private static void printCurrentBlockchain(){
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("Current blockchain: ");
        System.out.println(blockchainJson);
    }

    /**
     * When a node connects to the network and receive the current state of the blockchai,
     * it must then construc the stake distribution from it
     * solution: to run through all the block with newNode transactions
     * @param blockchain
     */
    private static void updateStakeDistribution(ArrayList<PoSBlock> blockchain){
        for (PoSBlock block:blockchain){
            PoS_Transaction blockTransaction = block.getTransaction();
            handleNewNodeBlock(blockTransaction);
        }
    }

    /**
     * This method prints the list of nodes and their corresponding stakes
     */
    private static void printCurrentCandidateListAndStakeDistribution(){
        System.out.println("======================================================");
        for (int i = 0; i < candidateList.size(); i++){
            System.out.println("Current stake distribution:");
            System.out.println(candidateList.get(i));
            System.out.println(stakeDistribution.get(i));
        }
        System.out.println("======================================================");
    }

    public static ArrayList<String> getNodeIPList(){
        return nodeIPList;
    }

    private static void showWalletOptions(){
        System.out.println("Run as a wallet, protocol: PoS!");
        System.out.println("Press 0 to create a new wallet");
        System.out.println("Press 1 to load an existing wallet");
        System.out.println("Press 2 to test");
        System.out.println("Press 3 to see test result");
        System.out.println("Press 4 to show blockchain in hash");
        System.out.println("Press 5 to show full blockchain");
        System.out.println("Press 6 to attack");
        System.out.println("Press 7 to see stake distribution");
        System.out.println("Press 8 to change protocol");
        System.out.println("Press 9 to start a new consensus round");
        System.out.println("Press 10 to end");
        System.out.println("======================================================");
    }

    /**
     * This method prints the current blockchain in form of a list of hashString, mostly
     * used to check if two transactions are identical
     */
    private static void printCurrentBlockchainInHash(){
        for(PoSBlock block:blockchain){
            System.out.println(block.getHash());
        }
    }

    private static boolean isCheckedBlockchainValid(ArrayList<PoSBlock> checkedBlockchain){
        for (PoSBlock block : checkedBlockchain){
            if (block.getHash() != block.calculateHash()){
                return false;
            }
        }
        return true;
    }

    private static void handleNewNodeBlock(PoS_Transaction blockTransaction){
        if (blockTransaction.newNode()){
            if (blockTransaction.getSender().equals(blockTransaction.getRecipient())){
                try {
                    int newNodeStake = Integer.parseInt(blockTransaction.getContent());
                    candidateList.add(blockTransaction.getSender());
                    stakeDistribution.add(newNodeStake);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
