public class PoS_Blockchain_Check_Request_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_BLOCKCHAIN_CHECK_REQ";

    public PoS_Blockchain_Check_Request_Message(){
        super.setType(type);
    }
}
