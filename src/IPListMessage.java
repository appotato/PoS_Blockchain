import java.util.ArrayList;

/**
 * This class is attached with an IP address list of nodes in the
 * network so that new nodes can broadcast message to the network.
 */
public class IPListMessage extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "IPLIST";
    private ArrayList<String> IPList;

    public IPListMessage(ArrayList<String> IPList){
        super.setType(type);
        this.IPList = IPList;
    }

    public ArrayList<String> getIPList(){
        return IPList;
    }
}
