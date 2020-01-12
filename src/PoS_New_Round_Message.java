public class PoS_New_Round_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_NEW_CONSENSUS_ROUND";

    public PoS_New_Round_Message(){
        super.setType(type);
    }
}
