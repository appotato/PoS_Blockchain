import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

/**
 * This class presents a transaction that will be used in PoS
 */
public class PoS_Transaction implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private String hash;
    private boolean newNode = false;
    private String sender;
    private String recipient;
    private String content;
    private String signature;
    private long timeStamp;

    private boolean good = true;

    public PoS_Transaction(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash(){
        hash = StringUtil.applySHA256(sender +
                recipient +
                content +
                timeStamp
        );
        return hash;
    }


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * This method utilizes EDCSA to make sure the data has not been tampered
     * by generating a digital signature
     * @param privateKeyString
     */
    public void generateSignature(String privateKeyString) {
        String data = sender + recipient + content + timeStamp;
        PrivateKey privateKey;
        try {
            privateKey = StringUtil.loadPrivateKey(privateKeyString);
            byte[] signatureByte = StringUtil.applyECDSASig(privateKey,data);
            signature = Base64.getEncoder().encodeToString(signatureByte);
        } catch (GeneralSecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method utilizes EDCSA to make sure the data has not been tampered
     * by verifying a generated digital signature
     * @return
     */
    public boolean verifySignature() {
        String data = sender + recipient + content + timeStamp;
        byte[] signatureByte;
        signatureByte = Base64.getDecoder().decode(signature);
        try {
            PublicKey senderPublicKey = StringUtil.loadPublicKey(sender);
            return StringUtil.verifyECDSASig(senderPublicKey, data, signatureByte);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public boolean validateTransaction() {
        if (!verifySignature()) {
            return false;
        }

        return true;
    }


    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setTimeStamp(int timeStamp){
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoS_Transaction that = (PoS_Transaction) o;
        return Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "hash='" + hash + '\'' +
                '}';
    }

    public void setAsFakeTransaction() {
        this.good = false;
    }

    public boolean isGood() {
        return good;
    }

    public void hasNewNode(){
        newNode = true;
    }

    public boolean newNode(){
        return newNode;
    }
}