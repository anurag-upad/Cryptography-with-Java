package test.java;

import static org.junit.Assert.assertEquals;

import org.anurag.crypto.EncryptDecryptUtility;
import org.junit.Test;

public class EncryptDecryptUnitlityTest {
	
	
	@Test
	public void testEncryptDecryptUtility() {

		String inputText = "anurag upadhyay is testing this class";
		String encryptedText= EncryptDecryptUtility.encrypt(inputText);
		String decryptedText= EncryptDecryptUtility.decrypt(encryptedText);
		
		assertEquals(inputText, decryptedText);
	}

}
