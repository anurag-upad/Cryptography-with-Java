package org.anurag.playfair.cipher;

public class StringUtility {
	
	/**
	 * light-weight utility method to convert a lowercase character to uppercase character
	 * as corresponding String's utility method (String.toUpperCase()) is slightly expensive (creates a new String every time)
	 * @param char
	 * @return char
	 */
	public static char makeCharUpperCaseIfNot(char ch) {

		if(!(ch >= 65 && ch <=90))
			ch=(char)(ch - 32);
		return ch;
	}
	
}
