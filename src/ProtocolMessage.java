/**
 * This message is response to connection message.
 * It tells the recipient the current protocol of the
 * network.
 */
public class ProtocolMessage extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "PROTOCOL";
    private int protocol;

    public ProtocolMessage(int protocol){
        super.setType(type);
        this.protocol = protocol;
    }

    public int getProtocol(){
        return protocol;
    }
}
