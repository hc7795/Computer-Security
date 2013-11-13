import java.util.*;
import java.io.*;
public class PasswordCrack {
	
	//public static ArrayList<String> mangledDict = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException{
		//System.out.println(jcrypt.crypt("(b","amazing"));
	
		File wordsDict = new File(args[0]);
		File pass = new File(args[1]);
		
		Scanner dictRd = new Scanner(wordsDict);
		Scanner passRd = new Scanner(pass);
		
		
		ArrayList<String> dict = new ArrayList<String>();
		ArrayList<User> users = new ArrayList<User>();
		
		while(dictRd.hasNext()) {
			dict.add(dictRd.nextLine());
		}
		
		User us; 
		while(passRd.hasNext()) {
			us = new User(passRd.nextLine());
			users.add(us);
		}
		
		
		boolean done = false;
		
		while(!users.isEmpty()) {
			int round = 0;
			for(int i = 0; i<users.size(); i++) {
				dict.add(users.get(i).firstName.toLowerCase());
				dict.add(users.get(i).lastName.toLowerCase());
				for(int j =0; j < dict.size(); j++) {
				
					if(mangleAndCompare(users.get(i), dict.get(j), round) {
						users.remove(users.get(i));
					}
					
				}
				
				dict.remove(users.get(i).firstName.toLowerCase());
				dict.remove(users.get(i).lastName.toLowerCase());
			}
			++round;
		}
	} 
	
	public static boolean mangleAndCompare(User us, String dictEle, int round) {
		while(round > 0) {
			
			--round;
		}
		
		String encryptedPass = "";
		for(int i = 0; i < dict.size(); i++) {
			encryptedPass = jcrypt.crypt(us.salt, dict.get(i));
			if(encryptedPass.equals(us.encryptedPassword))
				return true;
		}
		
		return false;
	}
}

class User {
	String firstName;
	String salt;
	String fullName;
	String lastName;
	String encryptedPassword; 
	String[] fullNameContainer = new String[2];
	
	public 
	User(String inputLine) {
		//System.out.println(inputLine);
		String[] inputParts = inputLine.split(":");
		//System.out.println(Arrays.toString(inputParts));
		salt = inputParts[1].substring(0, 2);
		encryptedPassword = inputParts[1];
		
		fullName = inputParts[4];
		fullNameContainer = fullName.split(" ");
		firstName = fullNameContainer[0];
		lastName = fullNameContainer[1];
		//System.out.println("first name = " + firstName + ", last name = " + lastName);
		//System.out.println("salt = " + salt);
	}
	
}

