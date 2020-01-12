import java.util.ArrayList;

public class PoS_Blockchain_Check_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_BLOCKCHAIN_CHECK";
    private ArrayList<PoSBlock> blockchain;

    public PoS_Blockchain_Check_Message(ArrayList<PoSBlock> blockchain){
        super.setType(type);
        this.blockchain = blockchain;
    }

    public ArrayList<PoSBlock> getBlockchain(){
        return blockchain;
    }
}
