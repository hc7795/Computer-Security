import java.util.*;
import java.io.*;

public class ACS {
	
	public static HashMap<String, String> userList = new HashMap<String, String>();
	public static HashMap<String, String[]> fileList = new HashMap<String, String[]>();
	public static boolean rootUser = true;
	
	public static void main(String[] args) throws IOException{
		File userListFile;
		File fileListFile;
		Scanner sc = new Scanner(System.in);
		
		String[] inputArr = new String[]{"","","",""};
		String input = "";
	        
		if(args.length > 2) { // if -r is given
			rootUser = false;
			userListFile = new File(args[1]);
			fileListFile = new File(args[2]);
		}
		else {
			rootUser = true;
			userList.put("root", "root");
			userListFile = new File(args[0]);
			fileListFile = new File(args[1]);
		}
	        
		Scanner userListRd = new Scanner(userListFile);
		Scanner fileListRd = new Scanner(fileListFile);
		
		//initialize userList and fileList
		String[] lineArr = new String[2];
		while(userListRd.hasNext()) {
			lineArr = userListRd.nextLine().split(" ");
			userList.put(lineArr[0], lineArr[1]);
		}
		String[] lineArr2 = new String[3];
		while(fileListRd.hasNext()) {
			lineArr2 = fileListRd.nextLine().split(" ");
			fileList.put(lineArr2[0], new String[]{lineArr2[1], lineArr2[2]});
		}
		
		
		//System.out.println("inputArr = " + Arrays.toString(inputArr));
		while(!input.equals("EXIT") || !input.equals("exit")) {
			System.out.println("Input: ");
			input = sc.nextLine();
			if(input.equals("EXIT") || input.equals("exit"))
				break;
			inputArr = input.split(" ");
			if(inputArr[1].equals("root") && !rootUser) {
				System.out.println("Output:");
				System.out.println(inputArr[0] + " root root 0");
			}
			else if(inputArr.length >= 3) 
				action(inputArr);
			System.out.println();
		}
		
		//Create a file and a bw object.    mode owner ownergroup filename
		File stateFile = new File("state.log");
		BufferedWriter bw = new BufferedWriter(new FileWriter(stateFile));
		String mode = "";
		String owner = "";
		String ownergroup = "";
		for (Map.Entry<String, String[]> entry : fileList.entrySet()) {
			String binaryStr = "";
		    String key = entry.getKey(); //filename;
		    String[] value = entry.getValue();
		    mode = value[1];
		    for(int i = 0; i<mode.length(); i++) {
				binaryStr += String.format("%3s", Integer.toBinaryString(Character.getNumericValue(mode.charAt(i)))).replace(' ', '0');
			}
		    mode = modeToString(binaryStr);
		    owner = value[0];
		    ownergroup = userList.get(owner);
		    bw.write(mode + " " + owner + " " + ownergroup + " " + key);
		    bw.newLine();
		}
		bw.close();
	}

	public static void action(String[] inputArr) {
		String action = inputArr[0];
		String user = inputArr[1];
		String file = inputArr[2];
		String chmod = "";
		if(inputArr.length > 3)
			chmod = inputArr[3];
		String mode = "";
		String binaryStr = "";
		String running_user = inputArr[1];
		String running_group = userList.get(user);
		boolean isRunningUser = true;
		boolean isRunningGroup = true;
		
		boolean isOwner = true;
		boolean isGroup = true;
		String modeStr = "";
		
		mode = fileList.get(file)[1];
		for(int i = 0; i<mode.length(); i++) {
			binaryStr += String.format("%3s", Integer.toBinaryString(Character.getNumericValue(mode.charAt(i)))).replace(' ', '0');
		}		
		if(binaryStr.charAt(0) == '1') {
			isRunningUser = false;
			running_user = fileList.get(file)[0]; 
		}
		if(binaryStr.charAt(1) == '1') {
			isRunningGroup = false;
			running_group = userList.get(fileList.get(file)[0]);
		}
		
		if(!user.equals(fileList.get(file)[0]))
			isOwner = false;
		if(!userList.get(user).equals(userList.get(fileList.get(file)[0])))
			isGroup = false;
		
		String[] temp = new String[2];
		System.out.println("output:");
		if(action.equals("CHMOD") || action.equals("chmod")) {
			if(isOwner || (rootUser && user.equals("root"))) {
				temp = fileList.get(file);
				temp[1] = chmod;
				fileList.put(file, temp);
				System.out.println(action + " " + running_user + " " + running_group + " 1");
			}
			else
				System.out.println(action + " " + running_user + " " + running_group + " 0");
		}
		else { //READ, WRITE, EXECUTE
			
			modeStr = modeToString(binaryStr);
			//System.out.println("modeStr = " + modeStr);
			
			if(action.equals("READ") || action.equals("read")){				
				if((isOwner && modeStr.charAt(0) == 'r') || (rootUser && user.equals("root")))
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else if(isGroup && modeStr.charAt(3) == 'r')
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else if(modeStr.charAt(6) == 'r')
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else
					System.out.println(action + " " + running_user +" "+ running_group +" 0");
			}
			else if(action.equals("WRITE") || action.equals("write")){ //WRITE
				if((isOwner && modeStr.charAt(1) == 'w') || (rootUser && user.equals("root")))
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else if(isGroup && modeStr.charAt(4) == 'w')
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else if(modeStr.charAt(7) == 'w')
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else
					System.out.println(action + " " + running_user +" "+ running_group +" 0");
					
			}
			else { //EXECUTE
				if((isOwner && modeStr.charAt(2) == 'e') || (rootUser && user.equals("root")))
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else if(isGroup && modeStr.charAt(5) == 'e')
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else if(modeStr.charAt(8) == 'e')
					System.out.println(action + " " + running_user +" "+ running_group +" 1");
				else
					System.out.println(action + " " + running_user +" "+ running_group +" 0");
			}
		}	
	}
	
	public static String modeToString(String binary) {
		String setID = binary.substring(0,3);
		String nineBits = binary.substring(3);
		String result = "";
		
		int count = 0;
		//int sectionCount = 0;
		for(int i = 0; i< nineBits.length(); i++) {
			++count;
			//System.out.println("count = " + count);
			if(count == 1) {
				if(nineBits.charAt(i) == '1')
					result += "r";
				else 
					result += "-";
			}
			else if(count == 2) {
				if(nineBits.charAt(i) == '1')
					result += "w";
				else 
					result += "-";
			}
			else { //the final bit
				if(i < 3) { //User
					if(setID.charAt(0) == '1' && nineBits.charAt(i) == '0')
						result += "S";
					else if(setID.charAt(0) == '1' && nineBits.charAt(i) == '1')
						result += "s";
					else if(nineBits.charAt(i) == '1')
						result += "x";
					else
						result += "-";
				}
				else if(i < 6) { //Group
					if(setID.charAt(1) == '1' && nineBits.charAt(i) == '0')
						result += "S";
					else if(setID.charAt(1) == '1' && nineBits.charAt(i) == '1')
						result += "s";
					else if(nineBits.charAt(i) == '1')
						result += "x";
					else
						result += "-";
				}
				else { //Other 
					if(setID.charAt(2) == '1' && nineBits.charAt(i) == '0')
						result += "T";
					else if(setID.charAt(2) == '1' && nineBits.charAt(i) == '1')
						result += "t";
					else if(nineBits.charAt(i) == '1')
						result += "x";
					else
						result += "-";
				}
				count = 0;
			}	
		}//EndFor
		return result;
	}

}