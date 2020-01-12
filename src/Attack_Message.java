/**
 * This is a message indicating an attack simulation to many protocols
 * The fields are to define which node is bad and
 * how many fake transactions are there in the transaction pool
 * The current field is sometimes important for protocol to not get duplicate data
 */
public class Attack_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "ATTACK";
    private boolean isBad;
    private int numberOfFakeTransactions;
    private int current;

    public Attack_Message(boolean isBad, int numberOfFakeTransactions){
        super.setType(type);
        this.isBad = isBad;
        this.numberOfFakeTransactions = numberOfFakeTransactions;
    }

    public Attack_Message(boolean isBad, int numberOfFakeTransactions, int current){
        super.setType(type);
        this.isBad = isBad;
        this.numberOfFakeTransactions = numberOfFakeTransactions;
        this.current = current;
    }

    public Attack_Message(){
        super.setType(type);
    }

    public int getNumberOfFakeTransactions() {
        return numberOfFakeTransactions;
    }

    public boolean isBad() {
        return isBad;
    }

    public int getCurrent(){
        return current;
    }
}
