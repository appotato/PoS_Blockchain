import java.util.ArrayList;

/**
 * This message indicates that a new PoS block has been mined and broadcast
 */
public class PoS_Block_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_NEW_BLOCK";
    private PoSBlock newBlock;

    public PoS_Block_Message(PoSBlock newBlock){
        super.setType(type);
        this.newBlock = newBlock;
    }

    public PoSBlock getNewBlock(){
        return newBlock;
    }
}
