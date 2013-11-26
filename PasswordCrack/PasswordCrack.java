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
		
		//System.out.println("hihihi");
		boolean done = false;
		int resultIdx = 0;
		int round = 0;
		String firstName = "";
		String lastName = "";
		while(!users.isEmpty()) {
			for(int i = 0; i<users.size(); i++) {
			//	System.out.println("********* i = " + i);
				firstName = users.get(i).firstName;
				lastName = users.get(i).lastName;
				
				dict.add(firstName);
				dict.add(lastName);
				
				if(i==0) {
					for(int j =0; j < dict.size(); j++) {
						//System.out.println("i = " + i);
						//System.out.println("j = " + j);
						//System.out.println("i == 0, and round = " + round + ", j = " + j + ", dict.size() = " + dict.size());
						resultIdx = mangleAndCompare(i, users.get(i), dict.get(j), round, users);
						if(resultIdx >= 0) {
							System.out.println(users.get(resultIdx).fullName + " cracked");
							users.remove(users.get(resultIdx));
							if(i > 0)
								--i;
						}
					}
				}
				else {
					//System.out.println("else part");
					for(int k = dict.size()-2; k<dict.size(); k++) {
						//System.out.println("i ==  " + i + ", else" + ", round = " + round);
						resultIdx = mangleAndCompare(i, users.get(i), dict.get(k), round, users);
						
						if(resultIdx >= 0) {
							System.out.println(users.get(resultIdx).fullName + " cracked");
							users.remove(users.get(resultIdx));
							if(i > 0)
								--i;
						}
					}
				}
				
				dict.remove(firstName);
				dict.remove(lastName);
			}
			++round;
		}
	} 
	
	public static int mangleAndCompare(int index, User us, String dictEle, int round, ArrayList<User> users) {
		ArrayList<String> dict = new ArrayList<String>();
		dict.add(dictEle);
		//System.out.println("round = " + round);
<<<<<<< HEAD
		ArrayList<String> temp = new ArrayList<String>();
=======
>>>>>>> 0a47e64e2fa0698751de476904552db7a05dc155
		

		int dictSize = dict.size();
		while(round > 0) {
			//System.out.println("round > 0?");
			
			//System.out.println("round = " + round + "dict size = " + dict.size());
			for(int i =0; i < dictSize; i++) {
				Mangle mg = new Mangle(dict.get(i));
				temp = mg.runMangle(dict.get(i));
				//System.out.println("temp.size() = " + temp.size());
				dict.addAll(temp);
			}
			//System.out.println("after round = " + round + "dict size = " + dict.size());
			//System.out.println("for loop ended");
			dictSize = dict.size();
			//System.out.println("round = " + round +", dict.size() = "+ dict.size());
			--round;
		}
		
		String encryptedPass = "";
		//System.out.println("dict.size() = " + dict.size() + ", dictEle = " + dictEle);
		for(int i = 0; i < dict.size(); i++) {
			//System.out.println("inside mangleAndCompare");
			encryptedPass = jcrypt.crypt(us.salt, dict.get(i));
			//System.out.println("encryptedPass = " + encryptedPass);
			if(encryptedPass.equals(us.encryptedPassword)) {
				//System.out.println(us.encryptedPassword);
				return index;
			}
			else {
				//System.out.println("inside else");
				for(int j =0; j<users.size(); j++) {
					encryptedPass = jcrypt.crypt(users.get(j).salt, dict.get(i));
					if(encryptedPass.equals(users.get(j).encryptedPassword)) {
						//System.out.println("found!!");
						//System.out.println(users.get(j).encryptedPassword);
						return j;
					}
				}
			}
		}
		//System.out.println("here?");
		return -1;
	}
}

class User {
	String firstName;
	String salt;
	String fullName;
	String lastName;
	String encryptedPassword; 
	String[] fullNameContainer = new String[2];
	
	public User(String inputLine) {
		//System.out.println(inputLine);
		String[] inputParts = inputLine.split(":");
		//System.out.println(Arrays.toString(inputParts));
		salt = inputParts[1].substring(0, 2);
		encryptedPassword = inputParts[1];
		
		fullName = inputParts[4];
		fullNameContainer = fullName.split(" ");
		firstName = fullNameContainer[0].toLowerCase();
		lastName = fullNameContainer[1].toLowerCase();
		//System.out.println("first name = " + firstName + ", last name = " + lastName);
		//System.out.println("salt = " + salt);
	}
	
}
