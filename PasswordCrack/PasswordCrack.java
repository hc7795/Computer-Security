import java.util.*;
import java.io.*;
public class PasswordCrack {
	
	public static ArrayList<String> mangledDict = new ArrayList<String>();
	public static ArrayList<String> doubleMangledDict = new ArrayList<String>();
	
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
		
		Mangle mg1 = new Mangle(dict.get(0));
		for(int i = 0; i < dict.size(); i++) {
			mangledDict.addAll(mg1.runMangle(dict.get(i)));
		}
		
		System.out.println(mangledDict.size());
		
		Mangle mg2 = new Mangle(mangledDict.get(0));
		for(int i = 0; i < mangledDict.size(); i++) {
			doubleMangledDict.addAll(mg2.runMangle(mangledDict.get(i)));
		}
		
		System.out.println("here?");
		ArrayList<String> userDict = new ArrayList<String>();
				
		boolean done = false;
		int resultIdx = 0;
		int round = 0;
		while(!users.isEmpty()) {
			for(int i = 0; i<users.size(); i++) {
			//	System.out.println("********* i = " + i);
				userDict.add(users.get(i).firstName);
				userDict.add(users.get(i).lastName);

				resultIdx = mangleAndCompare(i, users.get(i), userDict, round, users);
				
				if(resultIdx >= 0) {
					System.out.println(users.get(resultIdx).fullName + " cracked");
					users.remove(users.get(resultIdx));
					if(i > 0)
						--i;
				}
					
				userDict.remove(users.get(i).firstName);
				userDict.remove(users.get(i).lastName);
			}
			if(round < 3) {
				++round;
			}
		}
	}
	
	
	public static int mangleAndCompare(int index, User us, ArrayList<String> userDict, int round, ArrayList<User> users) {
		int roundOrg = round;
		ArrayList<String> namesMangled = new ArrayList<String>();
		String encryptedPass = "";

		while(round > 0) {
			//System.out.println("round > 0?");
			for(int i =0; i < userDict.size(); i++) {
				Mangle mg = new Mangle(userDict.get(i));
				namesMangled.addAll(mg.runMangle(userDict.get(i)));
			}
			--round;
		}
		
		if(roundOrg == 1) {
			mangledDict.addAll(namesMangled);
			
			for(int i = 0; i < mangledDict.size(); i++) {
				encryptedPass = jcrypt.crypt(us.salt, mangledDict.get(i));
				if(encryptedPass.equals(us.encryptedPassword)) {
					System.out.println(us.encryptedPassword);
					return index;
				}
				else {
					for(int j =0; j<users.size(); j++) {
						encryptedPass = jcrypt.crypt(users.get(j).salt, mangledDict.get(i));
						if(encryptedPass.equals(users.get(j).encryptedPassword)) {
							System.out.println(users.get(j).encryptedPassword);
							return j;
						}
					}
				}
			}
			
		}
		else if(roundOrg == 2) {
			doubleMangledDict.addAll(namesMangled);
			
			for(int i = 0; i < doubleMangledDict.size(); i++) {
				encryptedPass = jcrypt.crypt(us.salt, doubleMangledDict.get(i));
				if(encryptedPass.equals(us.encryptedPassword)) {
					System.out.println(us.encryptedPassword);
					return index;
				}
				else {
					for(int j =0; j<users.size(); j++) {
						encryptedPass = jcrypt.crypt(users.get(j).salt, doubleMangledDict.get(i));
						if(encryptedPass.equals(users.get(j).encryptedPassword)) {
							System.out.println(users.get(j).encryptedPassword);
							return j;
						}
					}
				}
			}	
		}
		
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

