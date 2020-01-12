/**
 * This message indicates that a PoS transaction is being broadcast
 */
public class PoS_Transaction_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_TRANSACTION";
    private PoS_Transaction transaction;

    public PoS_Transaction_Message(PoS_Transaction transaction){
        super.setType(type);
        this.transaction = transaction;
    }

    public PoS_Transaction getTransaction(){
        return transaction;
    }
}