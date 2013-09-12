/*
cs361 (Bill Young)
Assignment1 
Honghui Choi
hc7795
*/
import java.util.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileInputStream;

public class SecureSystem {
	HashMap<String, SecurityLevel> subject;	
	HashMap<String, SecurityLevel> object;
	HashMap<String, Integer> subjectTemp;
	HashMap<String, Integer> objectValue;
	
	public static void main(String[] args) throws IOException{
		Scanner reader = new Scanner(new FileInputStream(args[0]));
		SecureSystem sys = new SecureSystem();
		
		while(reader.hasNext()) {
			String instruction = reader.next();
			String[] words = instruction.split("\\s+");
			if(words[0].equals("read")) {
				//write method
				sys.read(words[1], words[2]);
				
			}
			else if(words[0].equals("write"))
				//read method
				continue;
			else
				continue;
				//illegal instructions?
			
		}
		
		SecurityLevel low = new SecurityLevel(0);
		SecurityLevel high = new SecurityLevel(1);
		
		
		sys.createNewSubject("Lyle", low);
		sys.createNewSubject("Hal", high);
		
		sys.createObject("LObj", low);
		sys.createObject("HObj", high);
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
	
	//public void createNewObject() {
	//}

	public void read(String subject, String object) {
		if(this.subject.get(subject).lev() > this.object.get(object).lev()) {
			int objVal = this.objectValue.get(object);
			this.subjectTemp.put(subject, objVal);
		}
	}
}

class SecurityLevel {
	//final static SecurityLevel LOW;
	//final static SecurityLevel HIGH;
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
