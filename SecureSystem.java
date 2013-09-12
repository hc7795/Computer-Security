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
	HashMap<String, SecurityLevel> subject;	
	HashMap<String, SecurityLevel> object;
	HashMap<String, Integer> subjectTemp;
	HashMap<String, Integer> objectValue;

	public static void main(String[] args) throws IOException{
		File reader = new File(args[0]); 
		Scanner sc = new Scanner(reader);
		SecureSystem sys = new SecureSystem();

		while(sc.hasNextLine()) {
			String instruction = sc.nextLine();
			//System.out.println("instruction = " + instruction);
			String[] words = instruction.split("\\s+");
			boolean isLegalInstruction = sys.legalInstruction(words);
			
			if(isLegalInstruction) {
				if(words[0].toLowerCase().equals("read"))
					sys.read(words[1], words[2]);
				else if(words[0].toLowerCase().equals("write"))
					sys.write(words[1], words[2], Integer.parseInt(words[3]));
			}
			else {
				System.out.println("illegal");
			}
		}

		SecurityLevel low = new SecurityLevel(0);
		SecurityLevel high = new SecurityLevel(1);


		sys.createNewSubject("Lyle", low);
		sys.createNewSubject("Hal", high);

		sys.createNewObject("LObj", low);
		sys.createNewObject("HObj", high);
		/*
		//sys.getReferenceMonitor().createNewObject("Lobj", low);
		//sys.getReferenceMonitor().createNewObject("Hobj", low);
		*/

	}

	public void createNewSubject(String subjectName, SecurityLevel level) {
		this.subject.put(subjectName, level);
	}
	public void createNewObject(String objectName, SecurityLevel level) {
		this.object.put(objectName, level);
	}

	public void read(String subject, String object) {
		System.out.println("Entered a read method");
		/*if(this.subject.get(subject).lev() > this.object.get(object).lev()) {
			int objVal = this.objectValue.get(object);
			this.subjectTemp.put(subject, objVal);
		}
		*/
		
	}
	public void write(String subject, String object, int value) {
		System.out.println("entered a write method");
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
