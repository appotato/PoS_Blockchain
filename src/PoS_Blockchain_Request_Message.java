/**
 * This message is sent by new nodes when they join a PoS network and
 * want to know the current state of the PoS blockchain
 */
public class PoS_Blockchain_Request_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "POS_BLOCKCHAIN_REQ";

    public PoS_Blockchain_Request_Message(){
        super.setType(type);
    }
}
