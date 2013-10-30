import java.io.*;
import java.util.*;
import java.lang.Integer;
import java.math.BigInteger;

public class AES {
	
	public static byte[][] keyArray = new byte[4][4];
	
	public static String[][] sbox =
		 {{"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76"},
		  {"CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0"},
		  {"B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15"},
		  {"04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75"},
		  {"09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84"},
		  {"53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF"},
		  {"D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8"},
		  {"51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2"},
		  {"CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73"},
		  {"60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB"},
		  {"E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79"},
		  {"E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08"},
		  {"BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A"},
		  {"70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E"},
		  {"E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF"},
		  {"8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"}};
	
	public static void main(String[] args) throws IOException{
		File keyFile = new File(args[1]);
		File inputFile = new File(args[2]);
		
		Scanner reader = new Scanner(keyFile);
		Scanner inputReader = new Scanner(inputFile);
        
		while(inputReader.hasNext()) {
			String inputLine = inputReader.nextLine();
			
			String hex = "";
			int row = 0;
			int col = 0;
			int count = 0;//increments a row at every 4 runs
			for(int i=0; i<inputLine.length()-1; i+=2) {
				hex = inputLine.substring(i, i+2);
				if(count == 4) {
					++row;
					col = 0;
					count = 0;
				}
				//keyArray[row][col] = hex;
				String hexInBinary = String.format("%8s", Integer.toBinaryString(Integer.parseInt(hex, 16))).replace(' ', '0');
				System.out.println("hexInBinary = " + hexInBinary);
				byte binaryInByte = (byte)Integer.parseInt(hexInBinary, 2);
				keyArray[row][col] = binaryInByte;
				//keyArray[row][col] = Byte.valueOf(new BigInteger(hex,16).toString(2));
				++count;
				++col;
			}
			for (byte[] arr : keyArray) {
				System.out.println(Arrays.toString(arr));
			}
			
			System.out.println();
			subBytes();
			for (byte[] arr : keyArray) {
				System.out.println(Arrays.toString(arr));
			}
			
			shiftRows();
			System.out.println();
			for (byte[] arr : keyArray) {
				System.out.println(Arrays.toString(arr));
			}
			//System.out.println("sdghikfdhgkjfg = " + Integer.toHexString(keyArray[0][0]));
		}
	}
	//taken from http://www.samiam.org/mix-column.html
	public static void mixColumns() {
		
	}
	
	public static void shiftRows() {
		byte temp = 0;
		int k = 0;
		for(int i=1; i<keyArray.length; i++) {
			k = i;
			while(k > 0) {
				temp = keyArray[i][0];
				for(int j = 1; j<keyArray[i].length; j++) {
					keyArray[i][j-1] = keyArray[i][j];
				}
				keyArray[i][keyArray[i].length-1] = temp;
				--k;
			}
		}
	}
	
	public static void subBytes() {
		byte hexByte = 0;
		int unsignedByte = 0;
		String hexString = "";
		byte sboxValue = 0;
		int row = 0;
		int col = 0;
		for(int i = 0; i<keyArray.length; i++) {
			for(int j = 0; j<keyArray[i].length; j++) {
				hexByte = keyArray[i][j];
				if(hexByte < 0) {
					unsignedByte = hexByte + 256;
					hexString = Integer.toHexString(unsignedByte);
				}
				else
					hexString = Integer.toHexString(hexByte);
				if(hexString.length() == 1)
					hexString = "0" + hexString;
				//System.out.println("unsignedByte = " +  unsignedByte);
				//System.out.println("hexString = " + hexString);
				row = Integer.parseInt(Character.toString(hexString.charAt(0)), 16);
				col = Integer.parseInt(Character.toString(hexString.charAt(1)), 16);
				sboxValue = (byte) Integer.parseInt(sbox[row][col], 16);
				keyArray[i][j] = sboxValue;
			}
		} 
	}
	
}
