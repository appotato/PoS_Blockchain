import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

/**
 * This class presents a block of data that is used in PoSW
 * The most important fileds are the validator and signature
 * because it allows other nodes to recognize a user as
 * the current leader
 */
public class PoSBlock implements Serializable {
	private static final long serialVersionUID = 6529685098267757690L;
	private String hash;
	private String previousHash;
	private PoS_Transaction transaction;
	private String validator;
	private long timeStamp;
	private String signature;
	
	public PoSBlock(String previousHash, PoS_Transaction transaction) {
		this.previousHash = previousHash;
		this.transaction = transaction;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	public String calculateHash() {
		return StringUtil.applySHA256(
        		previousHash + 
        		//Long.toString(timeStamp) +
        		transaction.getHash()
        		);
	}



	public void mintNewBlock() {
		hash = calculateHash();
	}
	
    public String getHash() {
    	return hash;
    }
    
    public void setTimeStamp(long timeStamp) {
    	this.timeStamp = timeStamp;
    }
    
    public String getPreviousHash() {
    	return previousHash;
    }

	/**
	 * This method utilizes EDCSA to make sure the data has not been tampered
	 * by generating a digital signature
	 * @param privateKeyString
	 */
  	public void generateSignature(String privateKeyString) {
  		String data = validator + hash + timeStamp;
  		PrivateKey privateKey;
		try {
			privateKey = StringUtil.loadPrivateKey(privateKeyString);
			byte[] signatureByte = StringUtil.applyECDSASig(privateKey,data);
			signature = Base64.getEncoder().encodeToString(signatureByte);	
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
  	}


	/**
	 * This method utilizes EDCSA to make sure the data has not been tampered
	 * by verifying a generated digital signature
	 * @return
	 */
  	public boolean verifySignature() {
  		String data = validator + hash + timeStamp;
  		byte[] signatureByte;
  		signatureByte = Base64.getDecoder().decode(signature);
  		try {
  			PublicKey validatorPublicKey = StringUtil.loadPublicKey(validator);
  			return StringUtil.verifyECDSASig(validatorPublicKey, data, signatureByte);
  		} catch (Exception ex) {
  			ex.printStackTrace();
  			return false;
  		}
  	}
  		
  	
  	public boolean validateBlock(){
  		if (!verifySignature()) {
  			return false;
  		}
  		return true;
  	}
  	
  	public void setValidator(String validator) {
  		this.validator = validator;
  	}
  	
  	public PoS_Transaction getTransaction(){
  		return transaction;
  	}
  	
  	public String getValidator() {
  		return validator;
  	}
  	
  	public void setHash( String hash) {
    	this.hash = hash;
    }

    public long getTimeStamp(){
		return timeStamp;
	}

}
