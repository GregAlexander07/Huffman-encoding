package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import HashTable.*;
import List.*;
import SortedList.*;
import Tree.*;


/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author Greg A. Viera Pérez <802168412> (Implementation)
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {

	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("input1.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/*We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it*/
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/*If input file is empty just return an empty string, if not just extract the data*/
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return line;
	}

	/**
	 * Compute Symbol Frequency Distribution of each character inside input string
	 * @param inputString - string to be considered to calculate each characters frequency distribution
	 * @return fdMap - the frequency distribution map of every character in inputString
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		Map<String, Integer> fdMap = new HashTableSC<>(10, new SimpleHashFunction<>());

		//loop through data set
		for (int i = 0; i < inputString.length(); i++) {

			//current letter
			String letter = String.valueOf(inputString.charAt(i));


			if (!fdMap.containsKey(letter)) {
				fdMap.put(letter, 1);

			} else {
				int oldValue = fdMap.get(letter);
				fdMap.put(letter, oldValue + 1);

			}
		}
		return fdMap;


	}

	/**
	 * Receives a Map with the frequency distribution and returns the root
	 * node of the corresponding Huffman tree.
	 * @param fD - he frequency distribution of the symbols
	 * @return huffmanRoot - the root node of the tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {

		SortedLinkedList<BTNode<Integer, String>> SL = new SortedLinkedList<BTNode<Integer, String>>();

		// store FD in SL
		for (String string : fD.getKeys()) {
			BTNode<Integer, String> nta = new BTNode<>(fD.get(string), string);
			SL.add(nta);
		}

		if (SL.size() == 1)
			//if there was only one node, that would be the root of the tree
			return SL.removeIndex(0);


		//construct tree
		while (SL.size() > 1) {
			BTNode<Integer, String> node = new BTNode<>();
			BTNode<Integer, String> left = SL.removeIndex(0);
			BTNode<Integer, String> right = SL.removeIndex(0);
			node.setLeftChild(left);
			node.setRightChild(right);
			left.setParent(node);
			right.setParent(node);

			//combine elements for parent node
			node.setKey(left.getKey() + right.getKey());
			node.setValue((left.getValue() + right.getValue()));


			SL.add(node);
		}

		//remove the remaining node of the list (the root of the huffman_tree)
		BTNode<Integer, String> huffmanRoot = SL.removeIndex(0);
		return huffmanRoot;
	}

	/**
	 * Receives the root of a Huffman tree and returns a mapping of every
	 * symbol to its corresponding Huffman code. Construct Prefix Codes
	 * @param huffmanRoot - the first/root node of the tree
	 * @return codeMap - a map containing the symbols and the corresponding huffman code
	  */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {

		Map<String, String> codeMap = new HashTableSC<>(2, new SimpleHashFunction<>());
		String code = ""; //
		//fill the code map
		huffman_codeAux(huffmanRoot, codeMap, code);
		return codeMap;

	}

	/**
	 * Receives the Huffman code map and the input string and returns the encoded
	 * string.
	 * @param encodingMap - mapping each symbol and its huffman code
	 * @param inputString - String to be encoded according to encodingMap
	 * @return the resulting string composed by all the
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		/* TODO Encode String */

		//string builder instead of regular string so that I am able to mutate it while adding to it
		StringBuilder encodedMessage = new StringBuilder();
		for (char c: inputString.toCharArray()){

			encodedMessage.append(encodingMap.get(String.valueOf(c)));
		}
		//return resulting string
		return encodedMessage.toString();
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with it's frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 *
	 * Used for output Purposes
	 *
	 * @param output - Encoded String
	 * @param lookupTable
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

				int index = prefixCodes.firstIndex(searched);

				if(index >= 0) { //Found it
					result= result + symbols.get(index);
					start = i;
				}
			}
			return result;
		}

	/**
	 * Create a mapping between every symbol and its corresponding Huffman code
	 * depending on the position of the node in the tree
	 * @param huffmanNode - the node to be evaluated
	 * @param codeMap - stores the mapping of the symbols and their corresponding huffman code
	 * @param code - holds the code string of each symbol
	 */
	public static void huffman_codeAux(BTNode<Integer, String> huffmanNode, Map<String, String> codeMap, String code) {


		//if its a leaf add the string code to code map
		if(huffmanNode.getLeftChild() == null && huffmanNode.getRightChild() == null){
			codeMap.put(huffmanNode.getValue(), code);
		}

		//string to the left adds 0
		if(huffmanNode.getLeftChild() != null)
			huffman_codeAux(huffmanNode.getLeftChild(), codeMap, code + "0");

		//string to the left adds 0
		if(huffmanNode.getRightChild() != null)
			huffman_codeAux(huffmanNode.getRightChild(), codeMap, code + "1");

	}

}
