/**
 * This message requests showing a simplified version of the current ledger
 * regardless of the protocol
 */
public class Show_Ledger_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "SHOW";

    public Show_Ledger_Message(){
        super.setType(type);
    }
}
