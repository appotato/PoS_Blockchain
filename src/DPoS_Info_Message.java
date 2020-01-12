/**
 * This message requests side information such as stake distribution, voting values...
 */
public class DPoS_Info_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "DPOS_INFO";

    public DPoS_Info_Message(){
        super.setType(type);
    }
}
