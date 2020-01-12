import java.util.ArrayList;

/**
 * This class is the response to a PoS blockchain request message. It is attached with
 * the current state of the blockchain to new nodes.
 */
public class PoS_Blockchain_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_BLOCKCHAIN";
    private ArrayList<PoSBlock> blockchain;

    public PoS_Blockchain_Message(ArrayList<PoSBlock> blockchain){
        super.setType(type);
        this.blockchain = blockchain;
    }

    public ArrayList<PoSBlock> getBlockchain(){
        return blockchain;
    }
}