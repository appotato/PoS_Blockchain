/**
 * This message requests for the result of the test conducted
 */
public class Test_Result_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "TEST_RESULT";

    public Test_Result_Message(){
        super.setType(type);
    }
}