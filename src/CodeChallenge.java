import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CodeChallenge {

	private int wordCount;
	private HashMap<String, Integer> wordBank; //maps words to # of occurrences
	private String[] topWords; //list of top ten words
	private int[] topValues; //list of top ten occurrences - correspond to top ten words
	private ArrayList<String> sentenceList; //list of sentences
	
	
	public CodeChallenge() {
		//initialize counter, bank, lists
		wordCount = 0;
		wordBank = new HashMap<String, Integer>();
		topWords = new String[10];
		topValues = new int[] {0,0,0,0,0,0,0,0,0,0};
		sentenceList = new ArrayList<String>();
		
		//read input file
		System.out.print("Input the name of the file you would like to read: ");
		Scanner input = new Scanner(System.in);
		String fileName = input.nextLine();
		input.close();
		System.out.println();
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
		
			//read through file
			String line;
			while((line = br.readLine()) != null) {
				String[] sentences = line.split("\\.", 0); //split string at every period
				for(String s : sentences) {					
					sentenceList.add(s.trim()); //add to list of sentences for check at end
					
					//split sentence into words at spaces, commas, colons, quotation marks
					String[] words = s.trim().split("[:,\" ]+", 0);
					
					for(String w : words) {
						if(w.contentEquals("")) continue; //if empty string, ignore
						String word = w.toLowerCase();
						if(!(wordBank.containsKey(word))) { //if word is not in word bank, add
							wordBank.put(word, 1);
						} else { //otherwise increment value in word bank
							wordBank.put(word, wordBank.get(word)+1);
						}
						
						//update lists & counters
						updateTopTen(word);
						++wordCount;
					}
				}
			}
			br.close();
		} catch (FileNotFoundException fnfe) {
			System.out.println("The file " + fileName + " could not be found.\n");
		} catch (IOException ioe) {
			System.out.println("The file " + fileName + " had an error reading info.\n");
		}
		print();
	}
	
	/** updates lists based on current word **/
	private void updateTopTen(String w) {
		//if word has just been entered and list is not full, put in first nonempty spot
		if(wordBank.get(w) == 1) {
			if(wordBank.size() <= 10) {
				topWords[wordBank.size()-1] = w;
				topValues[wordBank.size()-1] = 1;
			}
		}
		
		else {
			//check entire list to see if word is there yet
			boolean found = false;
			for(int i = 9; i >= 0; i--) {
				//if # of occurrences is less than the value of the top ten list, then it can't be higher in list
				if(topValues[i] > wordBank.get(w)) break;
				
				//when word bank is first being put together, has null values
				if(i >= wordBank.size()) continue;
				
				//if word is in top ten list, check whether it has overtaken preceding value
				if(w.contentEquals(topWords[i])) {
					found = true;
					topValues[i] = wordBank.get(w); //update # of occurrences in top ten list
					if(i == 0) break; //no need to compare w/ previous values if front of list
					//swap with previous values until in correct spot on list
					for(int j = i-1; j >= 0; j--) {
						//swap with previous value if necessary
						if(wordBank.get(w) > topValues[j]) {
							topWords[j+1] = topWords[j];
							topValues[j+1] = topValues[j];
							topWords[j] = w;
							topValues[j] = wordBank.get(w);
						}
						else break;
					}
				}
				if(found) break;
			}
			//if word did not show up in list, compare w/ final element
			if(!found && wordBank.get(w) > topValues[9]) {
				topWords[9] = w;
				topValues[9] = wordBank.get(w);
			}
			
		}
	}
	
	//** prints final information to terminal **/
	private void print() {
		System.out.println("Word count: " + wordCount);
		System.out.println();
		
		printTopTen();
		
		String lastSentenceWithWord = null;
		//find last sentence that contains word
		for(int i = sentenceList.size()-1; i >= 0; i--) {
			if(sentenceList.get(i).toLowerCase().contains(topWords[0])) {
				lastSentenceWithWord = sentenceList.get(i);
				break;
			}
		}
		System.out.println("Last sentence with the word \"" + topWords[0] + "\":\n" + lastSentenceWithWord);
	}
	private void printTopTen() {
		System.out.println("Top 10 words:");
		for(int i = 0; i < 10; i++) {
			System.out.println("#" + (i+1) + ": " + topWords[i] + " - " + topValues[i]);
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		new CodeChallenge();
	}
}
