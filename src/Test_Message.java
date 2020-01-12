/**
 * This message is to conduct a test regardless of the protocols
 */
public class Test_Message extends Message {
	private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "TEST";
    private int numberOfTransactionsToTest;
    boolean isYou;

    public Test_Message(int i){
        super.setType(type);
        numberOfTransactionsToTest = i;
    }

    public Test_Message(int i, boolean you){
        super.setType(type);
        numberOfTransactionsToTest = i;
        isYou = you;
    }

    public int getNumberOfTransactionsToTest() {
        return numberOfTransactionsToTest;
    }

    public boolean isYou(){
        return isYou;
    }
}
