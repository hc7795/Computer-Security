import java.util.*;
import java.io.*;


abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }
    
    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends HuffmanTree {
    public final String value; // the character this leaf represents
    
    public HuffmanLeaf(int freq, String val) {
        super(freq); //super calls a constructor of the superclass, which is HuffmanTree.
        value = val;
    }
}

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees
    
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}

public class Encoder{
	//one symbol
	public static HashMap<String, String> symbolnprefix = new HashMap<String, String>();
	public static HashMap<String, String> prefixnsymbol = new HashMap<String, String>();
	public static HashMap<String, int[]> encodings = new HashMap<String, int[]>();
	
	//two symbol
	public static Map<String, Integer> twoSymbol = new HashMap<String, Integer>();
	public static HashMap<String, String> symbolnprefix2 = new HashMap<String, String>();
	public static HashMap<String, String> prefixnsymbol2 = new HashMap<String, String>();
	public static HashMap<String, int[]> encodings2 = new HashMap<String, int[]>();
	
	public static char[] alph = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	public static void main(String args[]) throws IOException{
		
		File input = new File(args[0]);
		Scanner reader = new Scanner(input);
		
		int[] charFreqs = new int[256];
		int index = 1;
		int frequencySum = 0;
		ArrayList<Integer> frequencyList = new ArrayList<Integer>();
		//ArrayList<Character> symbolList = new ArrayList<Character>();
		
		int symbolNum = 0;
		while(reader.hasNextLine()) {
			String frequency = reader.nextLine();
			frequencyList.add(Integer.parseInt(frequency));
			frequencySum += Integer.parseInt(frequency);
			for(int i = 0; i < Integer.parseInt(frequency); i++) {
				++charFreqs[alph[index]];
			}
			++index;
			++symbolNum;
		}
		
		HuffmanTree tree = buildTree(charFreqs);
		
		System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
		printCodes(tree, new StringBuffer(), true);
		
		//generate a k-length text
		File testText = new File("testText");
		BufferedWriter bw = new BufferedWriter(new FileWriter(testText));

		int textLen = Integer.parseInt(args[1]);
		generateText(bw, textLen, frequencySum, frequencyList);
		
		//Encode the generated text
		File encodedtestText = new File("testText.enc1");
		BufferedWriter encodebw = new BufferedWriter(new FileWriter(encodedtestText));
		encode(testText, encodebw, true);
		
		//Decode the encoded text
		File originalText = new File("testText.dec1");
		BufferedWriter decodebw = new BufferedWriter(new FileWriter(originalText));
		decode(encodedtestText, decodebw, true);
		
		//Compute the average bits per symbol
		double avgBits = computeAvgBits(true, frequencySum, Integer.parseInt(args[1]), symbolNum);
		System.out.println("Efficiency, the average bits per symbol (single symbol) = " + avgBits);
		//Compute the entropy
		double entropy = getEntropy(frequencyList, frequencySum);
		System.out.println("Computed entropy (single symbol) = " + entropy);
		//percentage difference
		System.out.println("Percentage difference = " + (100 - ((double)entropy/avgBits * 100)) + "%");
		
		//the 2-symbol derived alphabet
		setSymbolPair(frequencyList);
		HuffmanTree twoSymbolTree = buildTreeForPair();
		System.out.println();
		System.out.println();
		System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
		printCodes(twoSymbolTree, new StringBuffer(), false);
		
		//Encode the generated text
		File encodedtestText2 = new File("testText.enc2");
		BufferedWriter encodebw2 = new BufferedWriter(new FileWriter(encodedtestText2));
		encode(testText, encodebw2, false);
		
		//Decode the encoded text
		File originalText2 = new File("testText.dec2");
		BufferedWriter decodebw2 = new BufferedWriter(new FileWriter(originalText2));
		decode(encodedtestText2, decodebw2, false);
		
		//Compute the average bits per symbol
		avgBits = computeAvgBits(false, frequencySum, Integer.parseInt(args[1]), symbolNum);
		System.out.println("Efficiency, the average bits per symbol (2-symbol) = " + avgBits);
		//percentage difference
		System.out.println("Percentage difference = " + (100 - ((double)entropy/avgBits * 100)) + "%");
	}

	public static double computeAvgBits(boolean oneSymbol, int frequencySum, int n, int frequencyNum){
		double totalBits = 0;
		if(oneSymbol) {
			for(Map.Entry<String, int[]> entry : encodings.entrySet()) {
				totalBits += (double)n*((double)entry.getValue()[0]/frequencySum) * entry.getValue()[1];
			}
			System.out.println("total bits = " + totalBits);
			return (double) totalBits/n;//(Math.ceil(log(frequencyNum,2)) *n);
		}
		else {
			for(Map.Entry<String, int[]> entry : encodings2.entrySet()) {
				totalBits += (double)(n/2)*((double)entry.getValue()[0]/(frequencySum*frequencySum)) * entry.getValue()[1];
			}
			System.out.println("total bits = " + totalBits);
			return (double) totalBits/n;//(Math.ceil(log(frequencyNum,2)) *n);
		}
	}
	
	// makes all possible pairs and map them to the product of their frequencies
	public static void setSymbolPair(ArrayList<Integer> frequencyList) {
		//a bunch of variables used in this method
		char symbol1 = '\0';
		char symbol2 = '\0';
		int frequency = 0;
		String pair = "";
	
		ArrayList<Integer> frequencyList2 = frequencyList;
		for(int i=0; i<frequencyList.size(); i++) {//frequencyList
			symbol1 = alph[i+1];
			for(int j = 0; j<frequencyList2.size(); j++) {//frequencyList2
				symbol2 = alph[j+1];
				frequency = frequencyList.get(i) * frequencyList2.get(j);
				pair = new StringBuilder().append(symbol1).append(symbol2).toString();
				//System.out.println("pair = " + pair + "frequency = " + frequency);
				twoSymbol.put(pair, frequency);
			}
		}
		//System.out.println("In setSymbolPair method, twoSymbol.size() = " + twoSymbol.size());
	}
	
	public static double getEntropy(ArrayList<Integer> frequencyList, int frequencySum) {
		double entropy = 0;
		for(int j = 0; j < frequencyList.size(); j++) {
			entropy += (double)frequencyList.get(j)/frequencySum * log((double)frequencyList.get(j)/frequencySum, 2);
		}
		
		//check if n symbols are all equally probable
		if(frequencyList.get(0) == frequencySum/frequencyList.size()) {
			System.out.println("**Entered a equal probability condition");
			boolean sameFrequency = true;
			for(int k =0; k<frequencyList.size(); k++) {
				if(frequencyList.get(k) != frequencySum/frequencyList.size())
					sameFrequency = false;
			}
			if(sameFrequency) {
				entropy = log((double)1/frequencySum, 2);
				System.out.println("sameFrequency");
			}
		}
		entropy *= -1;
		return entropy;
	}
	
	public static double log (double x, double base) {
	    return (Math.log(x) / Math.log(base));
	}
	
	public static void decode(File encodedtestText, BufferedWriter decodebw, boolean oneSymbol) throws IOException{
		Scanner sc = new Scanner(encodedtestText);
		while(sc.hasNext()) {
			String code = sc.nextLine();
			if(oneSymbol)
				decodebw.write(prefixnsymbol.get(code));
			else
				decodebw.write(prefixnsymbol2.get(code));
		}
		decodebw.close();
	}
	
	public static void encode(File testText, BufferedWriter encodebw, boolean oneSymbol) throws IOException{
		Scanner sc = new Scanner(testText); 
		String line = sc.nextLine();
		//System.out.println("line  = " + line);
		for(int i = 0; i<line.length(); i++) {
			if(oneSymbol) {
				encodebw.write(symbolnprefix.get(Character.toString(line.charAt(i))));
				encodebw.newLine();
			}
			else {
				if(2*i <= line.length()-2) {
					//System.out.println("line.substring(2i, i+2) = " + line.substring(2*i, 2*i+2));
					encodebw.write(symbolnprefix2.get(line.substring(2*i, 2*i+2)));
					//System.out.println("wrote successfully");
					encodebw.newLine();
				}
			}
		}
		encodebw.close();
	}
	
	public static void generateText(BufferedWriter bw, int klen, int frequencySum, ArrayList<Integer> frequencyList) throws IOException{
		Random ran = new Random();
		//System.out.println("Entered a generateText method.");
		for(int i = 0; i<klen; i++) {
			int range = 0;
			int n = ran.nextInt(frequencySum) + 1;
			for(int j = 0; j<frequencyList.size(); j++) {
				range += Integer.parseInt(frequencyList.get(j).toString());
				if(n <= range) {
					bw.write(alph[j+1]);
					break;
				}
			}
		}
		bw.close();
	}
	
	public static HuffmanTree buildTree(int[] charFreqs) {
	    PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
	    // initially, we have a forest of leaves
	    // one for each non-empty character
	    for (int i = 0; i < charFreqs.length; i++)
		if (charFreqs[i] > 0) {
		    //System.out.println("charFreqs[i] = " + charFreqs[i]);
		    trees.offer(new HuffmanLeaf(charFreqs[i], Character.toString((char)i)));
		}
	    assert trees.size() > 0;
	    // loop until there is only one tree left
	    while (trees.size() > 1) {
			//System.out.println("trees.size() = " + trees.size());
			// two trees with least frequency
			//System.out.println("***");
			HuffmanTree a = trees.poll();
			HuffmanTree b = trees.poll();
			//HuffmanLeaf la = (HuffmanLeaf)a;
			//HuffmanLeaf lb = (HuffmanLeaf)b;
			//System.out.println("a.value = " + la.value);
			//System.out.println("b.value = " + lb.value);
			//System.out.println("a.frequency = " + a.frequency);
			//System.out.println("b.frequency = " + b.frequency);
			// put into new node and re-insert into queue
			trees.offer(new HuffmanNode(a, b));
	    }
	    return trees.poll();
	}
	
	public static HuffmanTree buildTreeForPair() {
		PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
		//System.out.println("twoSymbol.size() = " + twoSymbol.size());
		for (Map.Entry<String, Integer> entry : twoSymbol.entrySet()) {  //twoSymbol
			//System.out.println("entry.getValue() = " + entry.getValue());
			trees.offer(new HuffmanLeaf(entry.getValue(), entry.getKey()));
		}
		assert trees.size() > 0;
		while (trees.size() > 1) {
			HuffmanTree a = trees.poll();
			HuffmanTree b = trees.poll();
			trees.offer(new HuffmanNode(a, b));
		}
		return trees.poll();
	}

	public static void printCodes(HuffmanTree tree, StringBuffer prefix, boolean oneSymbol) {
	    assert tree != null;
	    if (tree instanceof HuffmanLeaf) {
			HuffmanLeaf leaf = (HuffmanLeaf)tree;
			
			if(oneSymbol) {
				encodings.put(leaf.value, new int[]{leaf.frequency, prefix.toString().length()});
				//setting the global variables for encoding and decoding 
				symbolnprefix.put(leaf.value, prefix.toString());
				prefixnsymbol.put(prefix.toString(), leaf.value);
			}
			else {
				encodings2.put(leaf.value, new int[]{leaf.frequency, prefix.toString().length()});
				symbolnprefix2.put(leaf.value, prefix.toString());
				prefixnsymbol2.put(prefix.toString(), leaf.value);
			}
			// print out character, frequency, and code for this leaf (which is just the prefix)
			System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);

	    } else if (tree instanceof HuffmanNode) {
			HuffmanNode node = (HuffmanNode)tree;
			
			// traverse left
			prefix.append('0');
			printCodes(node.left, prefix, oneSymbol);
			prefix.deleteCharAt(prefix.length()-1);
			
			// traverse right
			prefix.append('1');
			printCodes(node.right, prefix, oneSymbol);
			prefix.deleteCharAt(prefix.length()-1);
	    }
	}
}
