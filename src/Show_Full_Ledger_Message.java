/**
 * This message requires showing the full presentation
 * of the current ledger regardless of the protocols
 */
public class Show_Full_Ledger_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "SHOW_FULL";

    public Show_Full_Ledger_Message(){
        super.setType(type);
    }
}