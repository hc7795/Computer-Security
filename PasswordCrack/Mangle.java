import java.util.ArrayList;

public class Mangle {

	public static ArrayList<String> mangledList;
	
	
	public Mangle(String dictEle) {
		mangledList = new ArrayList<String>();
		mangledList.add(dictEle);
	}
		
	public static ArrayList<String> runMangle (String dictEle) {
		prepend(dictEle);
		append(dictEle);
		deleteFirstCharater(dictEle);
		deleteLastCharacter(dictEle);
		reverse(dictEle);
		duplicate(dictEle);
		reflect(dictEle);
		uppercase(dictEle);
		lowercase(dictEle);
		capitalize(dictEle);
		ncapitalize(dictEle);
		toggle(dictEle);
		
		String firstEight = "";
		for(int i = 0; i < mangledList.size(); i++) {
			if(mangledList.get(i).length() > 8) {
				firstEight = mangledList.get(i).substring(0, 8);
				mangledList.set(i, firstEight);
			}
		}
		
		return mangledList;
	}
	
	public static void prepend(String dictEle) {
		
		for(int i=32; i<127; i++) {
			mangledList.add((char)i + dictEle);
		}
		//http://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
	}
	public static void append(String dictEle) {
		for(int i=32; i<127; i++) {
			mangledList.add(dictEle + (char)i);
		}
	}
	public static void deleteFirstCharater(String dictEle) {
		mangledList.add(dictEle.substring(1, dictEle.length()));
	}
	public static void deleteLastCharacter(String dictEle) {
		mangledList.add(dictEle.substring(0, dictEle.length()-1));
		
	}
	public static void reverse(String dictEle) {
		dictEle = new StringBuffer(dictEle).reverse().toString();
		mangledList.add(dictEle);
	}
	public static void duplicate(String dictEle) {
		dictEle += dictEle;
		mangledList.add(dictEle);
	}
	public static void reflect(String dictEle) {
		String reflect = dictEle;
		String reflect2 = "";
		dictEle += new StringBuffer(dictEle).reverse().toString();
		reflect2 = dictEle + reflect; 
		reflect += dictEle;
		mangledList.add(reflect);
		mangledList.add(reflect2);
	}
	public static void uppercase(String dictEle) {
		dictEle = dictEle.toUpperCase();
		mangledList.add(dictEle);
	}
	public static void lowercase(String dictEle) {
		dictEle = dictEle.toLowerCase();
		mangledList.add(dictEle);
	}
	public static void capitalize(String dictEle) {
		String firstLetter= Character.toString(dictEle.charAt(0)).toUpperCase();
		mangledList.add(firstLetter + dictEle.substring(1, dictEle.length()));
	}
	public static void ncapitalize(String dictEle) {
		String body = dictEle.substring(1,dictEle.length()).toUpperCase();
		mangledList.add(dictEle.charAt(0) + body);
	}
	public static void toggle(String dictEle) {
		String toggledStr = ""; 
		for(int i = 0; i<dictEle.length(); i++) {
			for(int j = i; j<dictEle.length(); j++) {
				//System.out.println("i = " + i);
				//System.out.println("j = " + j);
				toggledStr = dictEle.substring(i, j+1).toUpperCase();
				
				if(i == 0) {
					mangledList.add(toggledStr + dictEle.substring(j+1, dictEle.length()));
				}
				else if (i == 1){
					mangledList.add(dictEle.substring(0, 1) + toggledStr + dictEle.substring(j+1, dictEle.length()));
				}
				else {
					mangledList.add(dictEle.substring(0, i) + toggledStr + dictEle.substring(j+1, dictEle.length()));
				}
			}
		}
	}
//	public static String capitalizeAt(String dictEle, int index) {
//		String dictEle.charAt()
//	}
}
