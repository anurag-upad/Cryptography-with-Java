package org.anurag.playfair.cipher;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class PlayfairCipherMain {
	
	public static void main(String[] args) {

		Scanner scanner=new Scanner(System.in);

		//read 'n' : number of text lines to be encrypted
		int n=Integer.parseInt(scanner.nextLine());

		//this map will hold key-value pairs of -Keyword- vs -List of Text Lines to be encrypted-
		Map<String,List<String>> keyVsTextToEncryptMap=new LinkedHashMap<>();

		//keep fetching input from console until 'n' becomes 0
		while(n != 0) {
			String key=scanner.nextLine();

			//read inputs to populate map of Keyword vs List of Texts to be encrypted
			List<String> textToEncryptList=new ArrayList<>();		
			for(int i=n; i>0; i--) {								
				textToEncryptList.add(scanner.nextLine());
			}
			keyVsTextToEncryptMap.put(key, textToEncryptList);

			//fetch the next set of input
			n=Integer.parseInt(scanner.nextLine());
		}

		//iterate over the inputs and print the output
		for(Entry<String, List<String>> entry : keyVsTextToEncryptMap.entrySet()) {

			PlayfairCipher input=null;
			if(!entry.getKey().isBlank()) {

				input=new PlayfairCipher(entry.getKey(), entry.getValue());

				//encapsulate the encryption logic inside another class
				input.encrypt();
			}

			//leave an empty line after processing each input request
			System.out.println();
		}

		//close the scanner gracefully
		scanner.close();

	}
	
}
