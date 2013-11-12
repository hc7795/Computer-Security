import java.util.*;
import java.io.*;
public class PasswordCrack {
	public static void main(String[] args) throws IOException{
		//System.out.println(jcrypt.crypt("(b","amazing"));
	
		File wordsDict = new File(args[0]);
		File pass = new File(args[1]);
		
		Scanner dictRd = new Scanner(wordsDict);
		Scanner passRd = new Scanner(pass);
		
		User us = new User();
		
		ArrayList<String> dict = new ArrayList<String>();
		
		while(dictRd.hasNext()) {
			dict.add(dictRd.nextLine());
		}
		
		while(passRd.hasNext()) {
			us.addUser(passRd.nextLine());
			
			//seed the dict
			dict.add(us.firstName);
			dict.add(us.lastName);
			
			
			for(){
				
			}
		}
		
	} 
}

class User {
	String firstName;
	String salt;
	String fullName;
	String lastName;
	String[] fullNameContainer = new String[2];
	
	public void addUser(String inputLine) {
		System.out.println(inputLine);
		String[] inputParts = inputLine.split(":");
		System.out.println(Arrays.toString(inputParts));
		salt = inputParts[1].substring(0, 2);
		fullName = inputParts[4];
		fullNameContainer = fullName.split(" ");
		firstName = fullNameContainer[0];
		lastName = fullNameContainer[1];
		System.out.println("first name = " + firstName + ", last name = " + lastName);
		System.out.println("salt = " + salt);
	}
	
}

