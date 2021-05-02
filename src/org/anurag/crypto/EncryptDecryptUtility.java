package org.anurag.crypto;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * A Utility class to Encrypt/Decrypt a String using AES 128-bit Encryption Algorithm
 */
public class EncryptDecryptUtility {
	
    private static final String ENCRYPTION_KEY           = "ssdkF$HUy2A#D%kd";
    private static final String AES_ENCRYPTION_ALGORITHM = "AES";
    private static final String CHAR_ENCODING       	 = "UTF-8";
    private static final String CIPHER_TRANSFORMATION    = "AES/CBC/PKCS5PADDING";
  
    
    /**
     * Method for Encrypting a String
     * @param toBeEncryptedText
     * @return encryptedText
     */
    public static String encrypt(String toBeEncryptedText) {
		//We can encrypt given data using the Cipher class of the javax.crypto package.
		//javax.crypto package comes shipped with JDK
        String encryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            byte[] key = ENCRYPTION_KEY.getBytes(CHAR_ENCODING);
            SecretKeySpec secretKey = new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHM);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
            byte[] cipherText = cipher.doFinal(toBeEncryptedText.getBytes(CHAR_ENCODING)); //doFinal completes the encryption operation
            Base64.Encoder encoder = Base64.getEncoder();
            encryptedText = encoder.encodeToString(cipherText);

        } catch (Exception e) {
             e.printStackTrace();
        }
        return encryptedText;
    }

    
    /**
     * Method for Decrypting provided encrypted String
     * @param encryptedText
     * @return decryptedText
     */
    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            byte[] key = ENCRYPTION_KEY.getBytes(CHAR_ENCODING);
            SecretKeySpec secretKey = new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHM);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] cipherText = decoder.decode(encryptedText.getBytes(CHAR_ENCODING));
            decryptedText = new String(cipher.doFinal(cipherText), CHAR_ENCODING);

        } catch (Exception e) {
        	e.printStackTrace();
        }
        return decryptedText;
    }
    
    public static void main(String[] args) throws Exception {
    	
    	System.out.println("Enter String to be encrypted : ");
    	Scanner input = new Scanner(System.in);
    	String inputString = input.nextLine();
    	
    	long startTime=System.currentTimeMillis();
    	String encyptedString  = encrypt(inputString);
    	String encodedString   = URLEncoder.encode(encyptedString, CHAR_ENCODING);
    	long endTime           = System.currentTimeMillis();

    	long startTimeD        = System.currentTimeMillis();
    	String decodedString   = URLDecoder.decode(encodedString, CHAR_ENCODING);
    	String decryptedString = decrypt(decodedString);
    	long endTimeD          = System.currentTimeMillis();

    	System.out.println("Input String  : "     + inputString);
    	System.out.println("Encrypted String  : " + encyptedString);
    	System.out.println("Encoded String  : "   + encodedString);
    	System.out.println("Time taken in milliseconds for encrypting and encoding string : " + (endTime-startTime));
    	System.out.println("Decoded String  : "   + decodedString);
    	System.out.println("Decrypted String  : " + decryptedString);
    	System.out.println("Time taken in milliseconds for decoding and decrypting string : " + (endTimeD-startTimeD));

    	if(inputString.equals(decryptedString))
    		System.out.println("Wohooo !!");
    	else
    		System.out.println("Test Failed!");

    }   
}

