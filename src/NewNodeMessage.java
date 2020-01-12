/**
 * This message informs that a new node with the attached ip address has joined the network
 */
public class NewNodeMessage extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "NEW_NODE";
    private String ipAddress;

    public NewNodeMessage(String ipAddress){
        super.setType(type);
        this.ipAddress = ipAddress;
    }

    public String getIpAddress(){
        return ipAddress;
    }
}
