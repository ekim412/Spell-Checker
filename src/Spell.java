/*
Created by olga Veksler
 */


import java.io.File;
import java.lang.reflect.Method;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
 
public class Spell{
	
	public static HashDictionary dictionary;
	public Spell(){
		StringHashCode stringHC = new StringHashCode();
		
	}
	
	
	public static LinkedList<String> substitution(String word, LinkedList<String> substitutions){
		char letter = 'a';
		String suggestedW = "";
		
		StringBuffer stringB = new StringBuffer(word);
		
		for(int i = 0; i< word.length(); i++){
			for(letter = 'a'; letter <= 'z'; letter++){
				suggestedW = stringB.replace(i, i+1, String.valueOf(letter)).toString();
				if(dictionary.find(suggestedW) != null){
					if(!substitutions.contains(suggestedW)){
						substitutions.add(suggestedW);
					}
				}
				stringB = new StringBuffer(word);
			}
		}
		return substitutions;
	}
	
	public static LinkedList<String> omission(String word, LinkedList<String> substitutions){
		String suggestedW = "";
		char letter = 'a';
		StringBuffer stringB = new StringBuffer(word);
		for(int i = 0; i < word.length(); i++){
			suggestedW = stringB.delete(i, i+1).toString();
			for(letter = 'a'; letter <= 'z'; letter++){
				if(dictionary.find(suggestedW) != null){
					if(!substitutions.contains(suggestedW)){
						substitutions.add(suggestedW);
					}
				}
			}
		}
		return substitutions;
	}
	
	public static LinkedList<String> insertion(String word, LinkedList<String> substitutions){
		String suggestedW = "";
		char letter = 'a';
		StringBuffer stringB = new StringBuffer(word);
		for(int i = 0; i < word.length(); i++){
			for(letter = 'a'; letter <= 'z'; letter++){
				suggestedW = stringB.insert(i, letter).toString();
				if(dictionary.find(suggestedW) != null){
					if(!substitutions.contains(suggestedW)){
						substitutions.add(suggestedW);
					}
				}
				stringB = new StringBuffer(word);
			}
			
		}
		return substitutions;
	}
	
	public static LinkedList<String>reversal(String word, LinkedList<String> substitutions){
		String suggestedW = "";
		StringBuffer stringB = new StringBuffer(word);
		StringBuffer reverse = new StringBuffer();
		
		for(int i = 0; i<word.length()-1; i++){
			reverse = new StringBuffer(stringB.substring(i, i+2)).reverse();
			stringB.delete(i, i + 2);
			suggestedW = stringB.insert(i, reverse).toString();
			if(dictionary.find(suggestedW) != null){
				if(!substitutions.contains(suggestedW)){
					substitutions.add(suggestedW);
				}
			}
		}
		return substitutions;
	}
   
          
    public static void main(String[] args) throws java.io.IOException{
         if (args.length != 2 ) {
            System.out.println("Usage: spell dictionaryFile.txt inputFile.txt ");
            System.exit(0);
         }
         BufferedInputStream dict = null,file = null;
         FileWordRead wordReader;
         
         try{
           
            //dict  = new BufferedInputStream(new FileInputStream(args[0]));
            //file  = new BufferedInputStream(new FileInputStream(args[1]));
			// To read from specific files, comment the 2 lines above and 
            // uncomment 2 lines below 
            dict  = new BufferedInputStream(new FileInputStream("C:\\dictionary.txt"));
			file  = new BufferedInputStream(new FileInputStream("C:\\checkText.txt"));
	   
         }
         catch (IOException e){ // catch exceptions caused by file input/output errors
            System.out.println("Check your file name");
            System.exit(0);
        }
         
         wordReader = new FileWordRead(dict);
         
         int i = 0;
         while(wordReader.hasNextWord()){
        	 try{
        		 dictionary.insert(i, wordReader.nextWord());
        	 }
        	 catch (DictionaryException k){
        		 k.printStackTrace();
        	 }
         }
         
         wordReader = new FileWordRead(file);
         String word = "";
         Iterator<String> iterator;
         boolean x;
         LinkedList<String>suggestions = new LinkedList<String>();
         while(wordReader.hasNextWord()){
        	 word = wordReader.nextWord();
        	 if(dictionary.find(word) == null){
        		 x = true;
        		 suggestions = new LinkedList<String>();
        		 System.out.print(word + " => ");
        		 suggestions = (substitution(word,suggestions));
        		 suggestions = (omission(word,suggestions));
        		 suggestions = (insertion(word, suggestions));
        		 suggestions = (reversal(word, suggestions));
        		 
        		 iterator = suggestions.iterator();
        		 if(suggestions.isEmpty() != true){
        			 while(iterator.hasNext()){
        				 if(x = true){
        					 System.out.println(iterator.next());
        					 x = false;
        				 }
        				 else{
        					 System.out.println(", " + iterator.next());
        				 }
        			 }
        		 }
        		 else{
        			 System.out.println("Word was not in the dictionary.");
        		 }
        		 System.out.println("");
        	 }
         }
         
    }
}
