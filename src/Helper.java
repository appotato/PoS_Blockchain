/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;*/

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This is a utility helper class that has many convenient methods implemented for testing such
 * as creating a sameple transaction pool of 1000 transactions!
 */
public class Helper {
	private static ArrayList<String> nodeIPList = new ArrayList<String>();
	private static ArrayList<String> nodeIPList2 = new ArrayList<String>();
	private static final String[] testPublicKeys = {
			"MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAESbh0KbqYUuJgBNYs1IHZNiCWWj65IyCk+1YAp4m/hoT9yGIUNkhM6YQl4W6fC5rA",
            "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEEoq8TSoH3qDHQPegu3qJ8A8qvx1kbw75td4yobKca/QJVBHIY1k+CgyDe8os3i2O",
            "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEQiRea148/dR5AxaGBAWy2+TffnnDUVewtGi+zmL+wwfeWGZigfXFg/7IyU4HUYLE",
            "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEXV52X7hahjWiUoKdx+FvpYSq3woh3I21XGMxdbX8+TimQOWsjbbkFDcitJbtQLea",
            "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEniDOvllPoiyY2KCUW7SAGuBWx0snPuF3RpV7DJ6plN51clFYoPWIGVzUY1MFoOXN",
            "MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAECTMovzErdgkT4jvVL0aYK9wdTflRh47vlHBMnrKsOTLSrL5FhvHUP2ATaRd4FDrK"};
    private static final String[] testPrivateKeys = {
    		"MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBijUhHc1hSNUAyX1CQxvwu8ZIuCNeKLumOgCgYIKoZIzj0DAQGhNAMyAARJuHQpuphS4mAE1izUgdk2IJZaPrkjIKT7VgCnib+GhP3IYhQ2SEzphCXhbp8LmsA=",
            "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBgWBvZnO8Ql5eIWuaiQSAe+uFLyQTJjPjKgCgYIKoZIzj0DAQGhNAMyAAQSirxNKgfeoMdA96C7eonwDyq/HWRvDvm13jKhspxr9AlUEchjWT4KDIN7yizeLY4=",
            "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBjFXDzoSbd5jyTbQTAYn6JjLA2NRzKvo7SgCgYIKoZIzj0DAQGhNAMyAARCJF5rXjz91HkDFoYEBbLb5N9+ecNRV7C0aL7OYv7DB95YZmKB9cWD/sjJTgdRgsQ=",
            "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBjDtD15YyGsFwMH8wRb/SgrU9pVPgac/2agCgYIKoZIzj0DAQGhNAMyAARdXnZfuFqGNaJSgp3H4W+lhKrfCiHcjbVcYzF1tfz5OKZA5ayNtuQUNyK0lu1At5o=",
            "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBi+h33PRBKwhuo5wP3uY9ZE04HI0SiDALugCgYIKoZIzj0DAQGhNAMyAASeIM6+WU+iLJjYoJRbtIAa4FbHSyc+4XdGlXsMnqmU3nVyUVig9YgZXNRjUwWg5c0=",
            "MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBiNaJKlaJrzgW2aan4KKZFPpl0P/PSeicGgCgYIKoZIzj0DAQGhNAMyAAQJMyi/MSt2CRPiO9UvRpgr3B1N+VGHju+UcEyesqw5MtKsvkWG8dQ/YBNpF3gUOso="};
    private static Float[] stakes = {10f,10f,10f,10f,10f,10f};
	private static String publicKey;
	private static String privateKey;
    
    public static String getPublicKey(int i) {
    	return testPublicKeys[i];
    }

    public static ArrayList<String> getPublicKeys(){
    	ArrayList<String> publicKeys = new ArrayList<String>();
    	for(int i = 0; i < testPublicKeys.length; i++){
    		publicKeys.add(testPublicKeys[i]);
		}
    	return publicKeys;
	}
    
    public static String getPrivateKey(int i) {
    	return testPrivateKeys[i];
    }

    
    public static void printPublicKey(int i) {
    	System.out.println("Your public key is: " + testPublicKeys[i]);
    }
    
    public static void printPublicKeys() {
		System.out.println("======================================================");
    	for(int i = 0; i < testPublicKeys.length; i++) {
    		System.out.println(i + ": " + testPublicKeys[i]);
    	}
		System.out.println("======================================================");
    }
    
    /* Proof of Stake */
    public static float getStakes(int i) {
    	return stakes[i];
    }
    
    public static float getTotalDeposit() {
    	float totalDeposit = 0;
    	for (int i = 0; i < stakes.length; i++) {
    		totalDeposit += stakes[i];
    	}
    	return totalDeposit;
    }
    
    public static void printCurrentStake(int i) {
    	System.out.println("Your current stake is: " + stakes[i]);
    	int k = 10*i;
    	System.out.print("Index: ");
    	for (int j = 0; j<= 9; j++) {
    		System.out.print(k + j + " ");
    	}
    	System.out.println();
    }


	public static int getNumbersOfCandidate(){
    	return testPublicKeys.length;
	}

	public static ArrayList<PoS_Transaction> createTestPoSTransactions2(int numberOfTestTransactions, int time){
		ArrayList<PoS_Transaction> testTransactions = new ArrayList<PoS_Transaction>();
		for (int i = 0; i < numberOfTestTransactions; i++){
			String content = "This is test transaction number " + i + ", time: " + time;
			PoS_Transaction newTestTransaction = new PoS_Transaction(testPublicKeys[0], testPublicKeys[0], content);
			newTestTransaction.setTimeStamp(i);
			newTestTransaction.calculateHash();
			newTestTransaction.generateSignature(testPrivateKeys[0]);
			testTransactions.add(newTestTransaction);
		}
		return testTransactions;
	}

	private static void createNewWallet(){
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			keyGen.initialize(ecSpec, random);
			KeyPair keyPair = keyGen.generateKeyPair();
			privateKey = StringUtil.getStringFromKey(keyPair.getPrivate());
			publicKey = StringUtil.getStringFromKey(keyPair.getPublic());
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
