/*
cs361 (Bill Young)
Assignment1 
Honghui Choi
hc7795
*/
import java.util.*;
import java.io.IOException;
import java.io.FileReader;

public class SecureSystem {
	HashMap<String, SecurityLevel> subject;	
	HashMap<String, SecurityLevel> object;
	HashMap<String, Integer> subjectTemp;
	HashMap<String, Integer> objectValue;
	
	public static void main(String[] args) throws IOException{
		Scanner reader = new Scanner(new FileInputStream(args[0]));
		SecureSystem sys = new SecureSystem();
		
		while(reader.hasNext()) {
			String line = reader.next();
			String[] words = instruction.split("\\s+");
			if(words[0].equals("read")) {
				//write method
				sys.read(words[1], words[2]);
				
			}
			else if(words[0].equals("write"))
				//read method
			else
				//illegal instructions?
			
		}
		/*
		SecurityLevel low = SecurityLevel.LOW;
		SecurityLevel high = SecurityLevel.HIGH;
		
		
		sys.createSubject("Lyle", low);
		sys.createSubject("Hal", high);
		
		sys.createObject("LObj", 0);
		sys.createObject("HObj", 0);
		
		//sys.getReferenceMonitor().createNewObject("Lobj", low);
		//sys.getReferenceMonitor().createNewObject("Hobj", low);
		*/

    }
	/*
	public void createSubject(String subjectName, SecurityLevel level) {
		this.subject.put(subjectName, level);
	}
	public void createObject(String objectName, SecurityLevel level) {
		this.object.put(objectName, level);
	}
	
	//public void createNewObject() {
	//}
	*/
	public void read(String subject, String object) {
		if(this.subject.get(subject) > this.object.get(object)) {
			int objVal = this.objectValue.get(object);
			this.subjectTemp.put(subject, objVal);
		}
	}
}
/*
class SecurityLevel {
	final static SecurityLevel LOW;
	final static SecurityLevel HIGH;
	private Integer level;	
	
	//final static SecurityLevel Low = new SecurityLevel(0);
	
	public SecurityLevel (int level) {
		System.out.println("a constructor.");
		this.level = level;
	}
}
*/
