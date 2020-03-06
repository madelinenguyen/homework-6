import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyMiniSearchEngine {
	private HashMap<String, List<List<Integer>>> indexes;

	// disable default constructor
	private MyMiniSearchEngine() {
	}

	public MyMiniSearchEngine(List<String> documents) {
		index(documents);
	}

	// takes a list of strings (each string called a document) and generates indexes
	// each item in the List is considered a document.
	// assume documents only contain alphabetical words separated by white spaces.
	private void index(List<String> texts) {
		indexes = new HashMap<>();

		// loop through documents
		for (int i = 0; i < texts.size(); i++) {
			String[] wordsInDoc = texts.get(i).split(" ");

			// loop through words in document
			for (int j = 0; j < wordsInDoc.length; j++) {
				
				// make sure everything is lowercase
				wordsInDoc[j] = clean(wordsInDoc[j]);

				// check if word is an existing key
				if (indexes.containsKey(wordsInDoc[j])) {

					// create 2D array to keep track of values
					List<List<Integer>> value = indexes.get(wordsInDoc[j]); // returns value given key
					// add index to indexes dictionary
					value.get(i).add(j);

				// if word isn't an existing key
				} else {
					List<List<Integer>> temp = new ArrayList<List<Integer>>();
					for (int l = 0; l < texts.size(); l++) {
						temp.add(new ArrayList<>());
					}
					temp.get(i).add(j);
					indexes.put(wordsInDoc[j], temp);
				}
			}
		}
	}

	// search(key) return all the document ids where the given key phrase appears.
	// key phrase can have one or two words in English alphabetic characters.
	// return an empty list if search() finds no match in all documents.
	public List<Integer> search(String keyPhrase) {
		// safety check
		if (keyPhrase.isEmpty() || indexes.isEmpty())
			return new ArrayList<>();

		// turn given keyPhrase into String array just in case it's more than one word
		String[] wordsInKey = keyPhrase.split(" ");
		
		// clean first word in preparation
		wordsInKey[0] = clean(wordsInKey[0]);
		
		// check first word
		if (!indexes.containsKey(wordsInKey[0])) return new ArrayList<>();
		
		// clone only the 2D Integer array of indexes for just the first word
		List<List<Integer>> result = clone(indexes.get(wordsInKey[0]));
		
		// start at index 1 (second word) since we already checked the first word
		// if words array is length 1, then this part won't execute
		for (int i=1; i<wordsInKey.length; i++) {
			wordsInKey[i] = clean(wordsInKey[i]);
			
			// return empty ArrayList if Dictionary doesn't contain key
			if (!indexes.containsKey(wordsInKey[i])) return new ArrayList<>();
			
			// create a temp clone of indexes (only the 2D array part)
			// this array will be manipulated and returned
			List<List<Integer>> indexesClone = clone(indexes.get(wordsInKey[i]));
			
			// traverse the result array (number of occurrences in each document)
			for (int j=0; j<result.size(); j++) {
				
				// if first word doens't appear
				if (result.get(j).isEmpty()) indexesClone.get(j).clear();
				else {
					
					// loop through the rest of the indexes to find a match
					for (int k=0; k<indexesClone.get(j).size(); k++) {
						int len = 0;
						
						// go through each phrase
						while ( indexesClone.get(j).get(k) > result.get(j).get(len) ) {
							len++;
							
							// check size of array
							if (result.get(j).size()<=len) break;
						}
						if (0<len) len--;
						if ( indexesClone.get(j).get(k)-1 != result.get(j).get(len) ) {
							indexesClone.get(j).remove(k);
							k--;
						}
					}
				}
			}
			result = indexesClone;
		}
		
		// create new ArrayList to return
		List<Integer> ans = new ArrayList<>();

		for (int i=0; i<result.size(); i++) {
			// copy over values from result
			if (!result.get(i).isEmpty()) ans.add(i);
		}

		return ans;
	}

	/*
	 * used to make everything lower case
	 */
	public static String clean(String string) {
		string = string.toLowerCase();
		string = string.replaceAll("[^a-z]", "");
		return string;
	}

	/*
	 * used to make list deep copies
	 */
	public static List<List<Integer>> clone(List<List<Integer>> original) {
		List<List<Integer>> newClone = new ArrayList<List<Integer>>();
		for (int i=0; i<original.size(); i++)
			newClone.add(new ArrayList<Integer>(original.get(i)));

		return newClone;
	}

}
