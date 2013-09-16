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
	
	public static HashMap<String, int[]> subject;
	public static HashMap<String, int[]> object;
	
	public SecureSystem() {
		subject = new HashMap<String, int[]>();
		object= new HashMap<String, int[]>();
	}
    
	public static void main(String[] args) throws IOException{
		System.out.println("Reading from file: " + args[0].toString());
		System.out.println();
		File reader = new File(args[0]);
		Scanner sc = new Scanner(reader);
		
		
		
		SecureSystem sys = new SecureSystem();
       
		ReferenceMonitor RefMan = new ReferenceMonitor();

		sys.createNewSubject("lyle",new int[]{sys.low, 0});
		sys.createNewSubject("hal", new int[]{sys.high, 0});
		sys.createNewObject("lobj", new int[]{sys.low,  0});
		sys.createNewObject("hobj", new int[]{sys.high, 0});
        
		while(sc.hasNextLine()) {
			String instruction = sc.nextLine();
			String[] words = instruction.split("\\s+");

			boolean isLegalInstruction = sys.legalInstruction(words);
						
			if(isLegalInstruction) {
				InstructionObject IO = new InstructionObject();
				IO.setType(words[0].toLowerCase());
				IO.setSubjectName(words[1].toLowerCase());
				IO.setObjectName(words[2].toLowerCase());
				if(words[0].toLowerCase().equals("write")) {
					System.out.println("write");
					IO.setValue(Integer.parseInt(words[3]));
				}
				RefMan.takeInstructions(IO);
			}
			else {
				System.out.println("Bad Instruction");
			}
			sys.printState();
			System.out.println();
		}
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
	
	public void createNewSubject(String subjectName, int[] levelnTemp) {
		//System.out.println("sunjectName = " + subjectName);
		subject.put(subjectName, levelnTemp);
		//System.out.println("in createNewSubject, containsKey = " + subject.containsKey(subjectName));
	}
	public void createNewObject(String objectName, int[] levelnValue) {
		object.put(objectName, levelnValue);
	}
	
	public void printState() {
		Set<String> SubjectKeyset = subject.keySet();
		Set<String> ObjectKeyset = object.keySet();
		
		int subjectSize = SubjectKeyset.size();
		int objectSize = ObjectKeyset.size();
		
		String[] subjects = new String[subjectSize];
		String[] objects = new String[objectSize];
		
		int i = 0;
		for(Iterator<String> it = SubjectKeyset.iterator(); it.hasNext();) {
			String subjectName = it.next();
			subjects[i] = subjectName;
			++i;
		}
		int j = 0;
		for(Iterator<String> it = ObjectKeyset.iterator(); it.hasNext();) {
			String objectName = it.next();
			objects[j] = objectName;
			++j;
		}
		
		System.out.println("The current state is:");
		for(int k = 0; k<objects.length; k++) {
			System.out.println(objects[k] + " has value: " + object.get(objects[k])[1]);
		}
		for(int k = 0; k<subjects.length; k++) {
			System.out.println(subjects[k] + " has recently read: " + subject.get(subjects[k])[1]);
		}
	}
}



class InstructionObject {
	private String instructionType; //READ, WRITE, BAD
	private String subjectName;
	private String objectName;
	private int temp;
	private int value;
	
	public void setType (String type) {
		this.instructionType = type;
	}
	public void setSubjectName(String name) {
		this.subjectName = name;
	}
	public void setObjectName(String name) {
		this.objectName = name;
	}
	public void setTemp(int temp) {
		this.temp = temp;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public String getInstructionType() {
		return this.instructionType;
	}
	public String getSubjectName() {
		return this.subjectName;
	}
	public String getObjectName() {
		return this.objectName;
	}
	public int getValue() {
		return this.value;
	}
}

class ReferenceMonitor{
	SecureSystem sys = new SecureSystem();
	public void takeInstructions(InstructionObject IO) {
		if(IO.getInstructionType().equals("read"))
			simpleSecurity(IO.getSubjectName(), IO.getObjectName());
		else {
			System.out.println("reference monitor, write");
			starProperty(IO.getSubjectName(), IO.getObjectName(), IO.getValue());
		}
	}
	
	public void simpleSecurity(String subjectName, String objectName){
		boolean passed = true;
		if(sys.subject.get(subjectName)[0] >= sys.object.get(objectName)[0]) {
			executeRead(subjectName, objectName, passed);
		}
		else {
			passed = false;
			executeRead(subjectName, objectName, passed);
		}
	}
	public void starProperty(String subjectName, String objectName, int value){
		//System.out.println("entered star property");
		//System.out.println("*****subjectName = " + subjectName);
		//System.out.println("containsKey = " + subject.containsKey(subjectName));
		//System.out.println("this.subject.get(subject)[0] " + Arrays.toString(subject.get(subjectName)));
		if(sys.subject.get(subjectName)[0] <= sys.object.get(objectName)[0]) {
			executeWrite(objectName, value);
		}
	}
	//ObjectManager()
	public void executeRead(String subjectName, String objectName, boolean passed){
		if(passed)
			sys.subject.get(subjectName)[1] = sys.object.get(objectName)[1];
		else
			sys.subject.get(subjectName)[1] = 0;
	}
	public void executeWrite(String objectName, int value){
		sys.object.get(objectName)[1] = value;
	}	
}
