import java.io.*;
import java.util.*;

public class AES {
	
	public static String[][] keyArray = new String[4][4];
	
	public static void main(String[] args) throws IOException{
		File input = new File(args[1]);
        Scanner reader = new Scanner(input);
        
        while(reader.hasNext()) {
        	String key = reader.nextLine();
        	
        }
	}

}
