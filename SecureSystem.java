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

	public static void main(String[] args) throws IOException{
		/*
		Scanner reader = new Scanner(new FileInputStream(args[0]));
		while(reader.hasNext()) {
			String line = reader.next();
			String[] words = instruction.split("\\s+");
			if(words[0].equals("write"))
				//write method
			else if(words[0].equals("read"))
				//read method
			else
				//illegal instructions?
			
		}
		*/
		SecurityLevel low = SecurityLevel.LOW;
		SecurityLevel high = SecurityLevel.HIGH;
		
		SecureSystem sys = new SecureSystem();
		
		sys.createSubject("Lyle", low);
		sys.createSubject("Hal", high);
		
		//sys.getReferenceMonitor().createNewObject("Lobj", low);
		//sys.getReferenceMonitor().createNewObject("Hobj", low);

    }
	
	public void createSubject(String subjectName, SecurityLevel level) {
		this.subject.put(subjectName, level);
	}
	
	//public void createNewObjecy() {
	//}
}

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
