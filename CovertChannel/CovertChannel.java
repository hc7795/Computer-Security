/*
 * CS361 Assignment#2 Covert Channel
 * Name: Honghui Choi
 * UT EID: hc7795
 */

import java.util.*;
import java.io.*;
public class CovertChannel {
	final int low = 0;
	final int high = 1;

	public static HashMap<String, int[]> subject;
	public static HashMap<String, int[]> object;
    String strByte;
	
	public CovertChannel() {
		subject = new HashMap<String, int[]>();
		object = new HashMap<String, int[]>();
		strByte = "";
	}
	
	public static void main(String args[]) throws IOException{
		final long startTime = System.currentTimeMillis();
		File reader;
		String fileName = ""; 
		
		CovertChannel sys = new CovertChannel();
		ReferenceMonitor rm = new ReferenceMonitor();
		
		sys.createNewSubject("lyle", new int[]{sys.low, 0});
		sys.createNewSubject("hal", new int[]{sys.high, 0});
		
		File instructionsLog = new File("log");
		BufferedWriter log = new BufferedWriter(new FileWriter(instructionsLog));
				
		if(args[0].toString().equals("v")) {
			reader = new File(args[1]);
			fileName = args[1].toString();
		}
		else{
			reader = new File(args[0]);
			fileName = args[0].toString();
		}	
		
		File outputFile = new File(fileName + ".out");
		BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
		
		Scanner sc = new Scanner(reader);
		long bitsNum = 0;
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			byte[] bytes = line.getBytes();
			for(int i = 0; i <bytes.length; i++) {
				bitsNum += 8;
				String bits = String.format("%8s", Integer.toBinaryString(bytes[i])).replace(' ', '0');
				for(int j = 0; j<bits.length(); j++) {
					if(bits.charAt(j) == '0') {						
						log.write("RUN HAL");
						log.newLine();
						log.write("CREATE HAL OBJ");
						log.newLine();
						
						sys.run("hal", output);
						sys.create("hal", "obj");
					}
					
					log.write("CREATE LYLE OBJ");
					log.newLine();
					log.write("WRITE LYLE OBJ 1");
					log.newLine();
					log.write("READ LYLE OBJ");
					log.newLine();
					log.write("DESTROY LYLE OBJ");
					log.newLine();
					log.write("RUN LYLE");
					log.newLine();
					
					sys.create("lyle", "obj");
					rm.starProperty("lyle", "obj", 1); 
					rm.simpleSecurity("lyle", "obj");
					sys.destroy("lyle", "obj");
					sys.run("lyle", output);
				}
			}
			output.newLine();
		}
		output.close();
		log.close();
		final long endTime = System.currentTimeMillis();
		double executionTime = (double)(endTime - startTime)/1000;
		System.out.println("Total execution time: " + executionTime + "seconds");
		System.out.println("bandwidth = " + bitsNum/executionTime + " bits/sec");
	}
	
	public void create(String subjectName, String objectName) {

		if(this.object.containsKey(objectName)) {
			return;
		}
		createNewObject(objectName, new int[]{this.subject.get(subjectName)[0],0});
	}
		
	
	public void destroy(String subjectName, String objectName) {
		if(!this.object.containsKey(objectName)) {
			return;
		}
		if(this.subject.get(subjectName)[0] <= this.object.get(objectName)[0]) {
			this.object.remove(objectName);
		}
	}
	

	public void run(String subjectName, BufferedWriter writer) throws IOException{ //convert bits to char and write to the output file.
		if(subjectName.equals("lyle")) {
			this.strByte += Integer.toString(this.subject.get(subjectName)[1]);
			if(strByte.length() == 8) { //convert binary to char 
				int k = Integer.parseInt(strByte.substring(0, 8), 2);
				writer.write((char) k);
				this.strByte = "";
			}
		}
	}
	
	public void createNewSubject(String subjectName, int[] levelnTemp) {

		this.subject.put(subjectName, levelnTemp);
	}
	
	public void createNewObject(String objectName, int[] levelnValue) {
		this.object.put(objectName, levelnValue);
	}

}


class ReferenceMonitor{
	CovertChannel sys = new CovertChannel();
	ObjectManager om = new ObjectManager();
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
			om.executeRead(subjectName, objectName, passed);
		}
		else {
			passed = false;
			om.executeRead(subjectName, objectName, passed);
		}
	}
	public void starProperty(String subjectName, String objectName, int value){
		if(sys.subject.get(subjectName)[0] <= sys.object.get(objectName)[0]) {
			om.executeWrite(objectName, value);
		}
	}
	class ObjectManager {
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