import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * This is a utility class that helps with String manipulation
 * using pre-implemented library such as bountycastle
 */
public class StringUtil {

    /**
     * This method applies SHA-256 hash function to a String input
     * @param input a String of any length
     * @return a 256-bit String in hex
     */
	public static String applySHA256(String input){
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /**
     * This method applies ECDSA, a type of Asymmetric Encryption that is used to enhance security by digital signature,
     * a private key is one half of a pair of Asymmetric keys, used to create a signature that can be verify by the other half,
     * the public key
     * @param privateKey
     * @param input
     * @return
     */
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }


    /**
     * This method utilizes EDCSA to create a verification between the public key and the signature
     * generated from the obove mention private key
     * @param publicKey
     * @param data
     * @param signature
     * @return
     */
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method is used to change format of a key to a String for better view
     * @param key
     * @return
     */
    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * This method loads the public key from the String generated from it using the above method
     * @param stored
     * @return
     * @throws GeneralSecurityException
     */
    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode(stored);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("ECDSA","BC");
        return fact.generatePublic(spec);
    }

    /**
     * This method loads the private key from the String generated from it
     * @param stored
     * @return
     * @throws GeneralSecurityException
     */
    public static PrivateKey loadPrivateKey(String stored) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode(stored);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("ECDSA","BC");
        return fact.generatePrivate(spec);
    }
}

