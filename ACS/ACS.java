import java.util.*;
import java.io.*;

public class ACS {
	
	public static HashMap<String, String> userList = new HashMap<String, String>();
	public static HashMap<String, String[]> fileList = new HashMap<String, String[]>();
	
	public static void main(String[] args) throws IOException{
	File userListFile;
	File fileListFile;
	Scanner sc = new Scanner(System.in);
	System.out.println("Input: ");
	String[] inputArr = new String[3];
	String input = "";
        
        if(args.length > 2) { // if -r is given
		userListFile = new File(args[1]);
		fileListFile = new File(args[2]);
		//takes an input
		input = sc.nextLine();
		inputArr = input.split(" ");
		if(inputArr[1].equals("root")) {
			System.out.println("Output:");
			System.out.println(inputArr[0] + " root root 0");
            }
        }
        else {
		userListFile = new File(args[0]);
		fileListFile = new File(args[1]);
		input = sc.nextLine();
		inputArr = input.split(" ");
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
        action(inputArr);
        
	}
	
	public static void action(String[] inputArr) {
		String action = inputArr[0];
		String user = inputArr[1];
		String file = inputArr[2];
		String mode = "";
		
		if(inputArr[0].equals("CHMOD")) {
			
		}
		else { //READ, WRITE, EXECUTE
			mode = fileList.get(file)[1];
			
		}
		
	}
}