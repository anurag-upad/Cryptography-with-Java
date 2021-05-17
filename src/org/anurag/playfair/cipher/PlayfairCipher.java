package org.anurag.playfair.cipher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class PlayfairCipher {
	
	public static final String FILLER_STRING_FOR_CIPHER_TABLE="ABCDEFGHIKLMNOPQRSTUVWXYZ";
	private String cipherKey;
	private List<String> textToEncryptList;
	private char[][] cipherKeyTable=new char[5][5];
	
	
	public PlayfairCipher(String keyword, List<String> textToEncryptList) {
		this.cipherKey=keyword;
		this.textToEncryptList=textToEncryptList;
	}


	public void encrypt() {
		
		//create Cipher Key from given input
		createCipherKeyFromInput();
		
		//create Cipher Table from Cipher Key created in above method
		createCipherTableFromCipherKey();
		
		//encrypt input texts based on Cipher table and given rules
		encryptInputMessage();
		
	}


	//Encrypt each input message
	private void encryptInputMessage() {

		//iterate over the list of texts to be encrypted
		for(String inputText:this.textToEncryptList) {

			//first, apply given set of rules to the text-to-be-encrypted
			inputText = applyRulesToMessage(inputText);

			//now create Digraphs(pairs of 2 letters) from the above string
			List<String> digraphsList = createDigraphsFromInputText(inputText);

			digraphsList.stream().forEach(System.out::println);


			//encrypt the digraphs one by one and attach them to the output
			StringBuilder encryptedTextBuilder = new StringBuilder();
			encryptEachDigraph(digraphsList, encryptedTextBuilder);

			//print the encrypted text on the output console
			System.out.println(encryptedTextBuilder.toString());
		}

	}

	
	//apply given set of rules on each input message
	private String applyRulesToMessage(String inputText) {
		StringBuilder inputTextBuilder=new StringBuilder();

		for(char ch : inputText.toCharArray()) {

			if(!Character.isWhitespace(ch)) {						//ignore whitespaces
				ch=StringUtility.makeCharUpperCaseIfNot(ch);		//make all chars uppercase
				inputTextBuilder.append(ch == 'J' ? 'I' : ch);		//replace all occurences of 'J' with 'I'
			}
		}

		for(int i=0; i<inputTextBuilder.length(); i=i+2) {

			if(i+1 < inputTextBuilder.length() && 
					inputTextBuilder.charAt(i) == inputTextBuilder.charAt(i+1))
				inputTextBuilder.insert(i+1, 'X');					//if 2 consecutive chars in pair are same, insert 'X'
		}

		if(inputTextBuilder.length() % 2 != 0)						//append 'X' in the end for odd-length string			
			inputTextBuilder.append('X');

		return inputTextBuilder.toString();
	}


	//create Digraphs(pairs of 2 letters) from input text
	private List<String> createDigraphsFromInputText(String formattedText) {
		List<String> digraphsList=new ArrayList<>(formattedText.length()/2);  		

		for(int i=0; i<formattedText.length()-1; i=i+2) {

			StringBuilder pairCharsBuilder=new StringBuilder("");
			pairCharsBuilder.append(formattedText.charAt(i))
							.append(formattedText.charAt(i+1));

			digraphsList.add(pairCharsBuilder.toString());
		}

		return digraphsList;
	}

	
	//encrypt each digraph with the given set of rules for Cipher Table
	private void encryptEachDigraph(List<String> digraphsList, StringBuilder encryptedTextBuilder) {

		for(int i=0; i<digraphsList.size(); i++){

			if(digraphsList.get(i).equals("XX")){ 
				encryptedTextBuilder.append("YY"); 						//Rule-4 : if digraph is "XX", always replace it with "YY" 
				continue; 
			}

			char firstLetter=digraphsList.get(i).charAt(0);
			char secondLetter=digraphsList.get(i).charAt(1);

			//find the places of both the letters of Digraph in Cipher Table
			//index 0 will have the row number and index 1 will have the column number
			int[] char1place=getCharPlaceInCipherTable(firstLetter);
			int[] char2place=getCharPlaceInCipherTable(secondLetter);

			if(char1place[0] == char2place[0]){							//Rule-2 : scenario of characters in same row
				char1place[1]=(char1place[1] + 1) % 5;
				char2place[1]=(char2place[1] + 1) % 5;
			}
			else if(char1place[1] == char2place[1]){					//Rule-3 : scenario of characters in same column
				char1place[0]=(char1place[0] + 1) % 5;
				char2place[0]=(char2place[0] + 1) % 5;
			}
			else{														//Rule-1 : scenario of characters in different row and column		
				int temp=char1place[1];									//just swap column indexes,keeping row index unchanged
				char1place[1]=char2place[1];				
				char2place[1]=temp;
			}

			//get the corresponding cipher characters from the cipherKeyTable and attach it to output text
			encryptedTextBuilder.append(cipherKeyTable[char1place[0]][char1place[1]])
								.append(cipherKeyTable[char2place[0]][char2place[1]]);
		}
	}


	//get the places of both letters of digraph in Cipher Table
	private int[] getCharPlaceInCipherTable(char ch){
		int[] keyPlacesArray = new int[2];

		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){

				if (cipherKeyTable[i][j] == ch){
					keyPlacesArray[0]=i;
					keyPlacesArray[1]=j;
					break;
				}
			}
		}
		return keyPlacesArray;
	}

	
	//create Cipher Key from given Input
	private void createCipherKeyFromInput() {
		StringBuilder cipherKeyBuilder=new StringBuilder();

		HashSet<Character> orderedKeySet=new LinkedHashSet<>();		//remove duplicates while maintaining insertion order
		for(char ch:this.cipherKey.toCharArray()) {

			if(!Character.isWhitespace(ch)) {
				ch=StringUtility.makeCharUpperCaseIfNot(ch);	//ignore whitespaces in key
				orderedKeySet.add((ch));  							//make all key letters uppercase						
			}
		}

		Iterator<Character> itr=orderedKeySet.iterator();
		while(itr.hasNext()){
			char ch=itr.next();

			if(ch == 'J' || ch == 'I') {							//replace 'J' with 'I'
				if(cipherKeyBuilder.indexOf("I") == -1)				//check if 'I' is not present already due to an earlier occurrence of 'J' or 'I' 
					cipherKeyBuilder.append('I');					
			}else{
				cipherKeyBuilder.append(ch);
			}
		}

		this.cipherKey=cipherKeyBuilder.toString();
		//System.out.println("Cipher Key : " + this.cipherKey);
	}


	//create Cipher table from the created Cipher Key in above method
	private void createCipherTableFromCipherKey() {

		String cipherKeyWithAllChars=this.cipherKey;

		//first, fill Cipher Key with unique reamining alphabets
		char[] fillerChars=FILLER_STRING_FOR_CIPHER_TABLE.toCharArray();

		for(int i=0; i<fillerChars.length; i++) {

			if(cipherKeyWithAllChars.length() == 25)				//Cipher table can hold max of 25 chars only
				break;

			if(cipherKeyWithAllChars.indexOf(fillerChars[i]) == -1)
				cipherKeyWithAllChars+=fillerChars[i];
		}
		System.out.println(cipherKeyWithAllChars);


		//then, generate Cipher Table : a 2-D array of size 5,5
		int k=0;
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				this.cipherKeyTable[i][j] = cipherKeyWithAllChars.charAt(k++);
			}
		}
		
		printCipherTable(cipherKeyTable);

	}
	
	private void printCipherTable(char[][] cipherKeyTable) {
		for(int i=0; i<5; i++) {
			System.out.println();
			for(int j=0; j<5; j++) {
				System.out.print(cipherKeyTable[i][j]) ;
			}
		}
		System.out.println();
	}

}
