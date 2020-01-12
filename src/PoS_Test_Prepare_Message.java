public class PoS_Test_Prepare_Message extends Message {
    private static final long serialVersionUID = 6529685098267757690L;
    private static final String type = "TEST_PREPARE";

    public PoS_Test_Prepare_Message(){
        super.setType(type);
    }
}
