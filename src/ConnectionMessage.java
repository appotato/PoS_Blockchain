/**
 * This messages shows that a node wants to join the network
 */
public class ConnectionMessage extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "CONNECTION";

    public ConnectionMessage(){
        super.setType(type);
    }
}
