import java.util.*;
import java.io.*;

public class ACS {
	public static void main(String[] args) throws IOException{
		File userList;
        File fileList;
        Scanner sc = new Scanner(System.in);
        System.out.println("Input: ");
        String[] inputArr = new String[3];
        String input;
        
        if(args.length > 2) { // if -r is given
        	userList = new File(args[1]);
            fileList = new File(args[2]);
            //takes an input
            input = sc.nextLine();
            inputArr = input.split(" ");
            if(inputArr[1].equals("root")) {
            	System.out.println("Output:");
            	System.out.println(inputArr[0] + " root root 0");
            }
        }
        else {
        	userList = new File(args[0]);
            fileList = new File(args[1]);
            input = sc.nextLine();
            inputArr = input.split(" ");
        }
        
        if(inputArr[0].equals("CHMOD")) {
        	
        }
        else { //READ, WRITE, EXECUTE
        	
        }
        
	}
}
