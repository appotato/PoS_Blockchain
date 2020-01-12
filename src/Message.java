import java.io.Serializable;

/**
 * This abstract class is to build other message classes
 */
public abstract class Message implements Serializable {
	private static final long serialVersionUID = 6529685098267757690L;
    private String type;


    public String getType() {
        return type;
    }
    
    public void setType(String type) {
    	this.type = type;
    }

}
