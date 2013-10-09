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
    public final char value; // the character this leaf represents
    
    public HuffmanLeaf(int freq, char val) {
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
	public static HashMap<Character, String> symbolnprefix = new HashMap<Character, String>();
	public static HashMap<String, Character> prefixnsymbol = new HashMap<String, Character>();
	public static char[] alph = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

	public static void main(String args[]) throws IOException{
		File input = new File(args[0]);
		Scanner reader = new Scanner(input);
		
		int[] charFreqs = new int[256];
		int index = 1;
		int frequencySum = 0;
		ArrayList<Integer> frequncyList = new ArrayList<Integer>();
		
		while(reader.hasNextLine()) {
			String frequency = reader.nextLine();
			frequncyList.add(Integer.parseInt(frequency));
			frequencySum += Integer.parseInt(frequency);
			for(int i = 0; i < Integer.parseInt(frequency); i++) {
				++charFreqs[alph[index]];
			}
			++index;
		}
		
		HuffmanTree tree = buildTree(charFreqs);
		
		System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
		printCodes(tree, new StringBuffer());

		
		//generate a k-length text
		File testText = new File("testText");
		BufferedWriter bw = new BufferedWriter(new FileWriter(testText));

		int textLen = Integer.parseInt(args[1]);
		generateText(bw, textLen, frequencySum, frequncyList);
		
		//Encode the generated text
		File encodedtestText = new File("testText.enc1");
		BufferedWriter encodebw = new BufferedWriter(new FileWriter(encodedtestText));
		encode(testText, encodebw);
		
		//Decode the encoded text
		File originalText = new File("testText.dec1");
		BufferedWriter decodebw = new BufferedWriter(new FileWriter(originalText));
		decode(encodedtestText, decodebw);
	}
	
	public static void decode(File encodedtestText, BufferedWriter decodebw) throws IOException{
		Scanner sc = new Scanner(encodedtestText);
		while(sc.hasNext()) {
			String code = sc.nextLine();
			decodebw.write(prefixnsymbol.get(code));
		}
		decodebw.close();
	}
	
	public static void encode(File testText, BufferedWriter encodebw) throws IOException{
		Scanner sc = new Scanner(testText); 
		String line = sc.nextLine();
		for(int i = 0; i<line.length(); i++) {
			//System.out.println("char returned = " + freqnprefix.get(line.charAt(i)));
			encodebw.write(symbolnprefix.get(line.charAt(i)));
			encodebw.newLine();
		}
		encodebw.close();
	}
	
	public static void generateText(BufferedWriter bw, int klen, int frequencySum, ArrayList frequencyList) throws IOException{
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
		    trees.offer(new HuffmanLeaf(charFreqs[i], (char)i));
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

	public static void printCodes(HuffmanTree tree, StringBuffer prefix) {
	    assert tree != null;
	    if (tree instanceof HuffmanLeaf) {
			HuffmanLeaf leaf = (HuffmanLeaf)tree;
			
			//setting the global variables for encoding and decoding 
			symbolnprefix.put(new Character(leaf.value), prefix.toString());
			prefixnsymbol.put(prefix.toString(), new Character(leaf.value));
			
			// print out character, frequency, and code for this leaf (which is just the prefix)
			System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);

	    } else if (tree instanceof HuffmanNode) {
			HuffmanNode node = (HuffmanNode)tree;
			
			// traverse left
			prefix.append('0');
			printCodes(node.left, prefix);
			prefix.deleteCharAt(prefix.length()-1);
			
			// traverse right
			prefix.append('1');
			printCodes(node.right, prefix);
			prefix.deleteCharAt(prefix.length()-1);
	    }
	}
}
