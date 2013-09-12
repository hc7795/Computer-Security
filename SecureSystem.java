/*
cs361 (Bill Young)
Assignment1 
Honghui Choi
hc7795
*/
import java.util.*;
import java.io.IOException;
import java.io.*;

public class SecureSystem {
	final int low = 0;
	final int high = 1;
	HashMap<String, int[]> subject;	
	HashMap<String, int[]> object;
	
	public SecureSystem() {
		subject = new HashMap<String, int[]>();
		object = new HashMap<String, int[]>();
	}

	public static void main(String[] args) throws IOException{
		File reader = new File(args[0]); 
		Scanner sc = new Scanner(reader);
		
		SecureSystem sys = new SecureSystem();

		sys.createNewSubject("Lyle",new int[]{sys.low, 0});
		sys.createNewSubject("Hal", new int[]{sys.high, 0});
		sys.createNewObject("Lobj", new int[]{sys.low,  0});
		sys.createNewObject("Hobj", new int[]{sys.high, 0});
		

		while(sc.hasNextLine()) {
			System.out.println("Entered a while loop");
			String instruction = sc.nextLine();
			//System.out.println("instruction = " + instruction);
			String[] words = instruction.split("\\s+");
			boolean isLegalInstruction = sys.legalInstruction(words);
			
			if(isLegalInstruction) {
				if(words[0].toLowerCase().equals("read"))
					sys.read(words[1], words[2]);
				else if(words[0].toLowerCase().equals("write")) {
					System.out.println("words[1] = " + words[1]);
					System.out.println("words[2] = " + words[2]);
					System.out.println("Integer.parseInt(words[3]) = " + Integer.parseInt(words[3]));
					sys.write(words[1], words[2], Integer.parseInt(words[3]));
				}
			}
			else {
				System.out.println("illegal");
			}
		}
		
		//SecurityLevel low = new SecurityLevel(0);
		//SecurityLevel high = new SecurityLevel(1);

		//sys.createNewObject("LObj", [sys.low, 0]);
		//sys.createNewObject("HObj", [sys.high, 0]);

	}

	public void createNewSubject(String subjectName, int[] levelnTemp) {
		System.out.println("creating a new subject");
		//System.out.println("subjectName = " + subjectName);
		//System.out.println("levelnTemp = " + Arrays.toString(levelnTemp));
		this.subject.put(subjectName, levelnTemp);
	}
	public void createNewObject(String objectName, int[] levelnValue) {
		System.out.println("Creating a new object");
		this.object.put(objectName, levelnValue);
	}

	public void read(String subject, String object) {
		System.out.println("Entered a read method");
		
		if(this.subject.get(subject)[0] > this.object.get(object)[0]) 
			this.subject.get(subject)[1] = this.object.get(object)[1];
	}
	public void write(String subject, String object, int value) {
		System.out.println("entered a write method");
		
		if(this.subject.get(subject)[0] < this.object.get(object)[0])
			this.object.get(object)[1] = value;
	}
	public boolean legalInstruction (String[] instruction) {
		if(instruction.length < 3 || instruction.length > 4)
			return false;
		else if(!(instruction[0].toLowerCase().equals("read") || instruction[0].toLowerCase().equals("write")))
			return false;
		else if(!(instruction[0] instanceof String) || !(instruction[1] instanceof String) || !(instruction[2] instanceof String))
			return false;
		else if(instruction[0].toLowerCase().equals("write") && instruction.length != 4)
			return false;
		else if(instruction[0].toLowerCase().equals("write")) {
			try {
			Integer.parseInt(instruction[3]);
			} catch(NumberFormatException e) {
				return false; }
		}
		return true;
	}
}
/*
class SecurityLevel {
	private final Integer lev;	

	//final static SecurityLevel Low = new SecurityLevel(0);

	public SecurityLevel (int lev) {
		System.out.println("a constructor.");
		this.lev = lev;
	}

	Integer getLev() {
		return this.lev;
	}
}
*/
