import java.io.*;
import java.util.*;
import java.lang.Integer;
import java.math.BigInteger;

public class AES {
	
	public static byte[][] inputArray = new byte[4][4];
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
	
	 final static int[] LogTable = {
		    0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3, 
		    100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193, 
		    125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120, 
		    101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142, 
		    150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56, 
		    102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16, 
		    126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186, 
		    43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87, 
		    175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232, 
		    44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160, 
		    127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183, 
		    204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157, 
		    151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209, 
		    83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171, 
		    68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165, 
		    103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7};
	 
	 final static int[] AlogTable = {
		    1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53, 
		    95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170, 
		    229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49, 
		    83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205, 
		    76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136, 
		    131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154, 
		    181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163, 
		    254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160, 
		    251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65, 
		    195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117, 
		    159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 
		    155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84, 
		    252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202, 
		    69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14, 
		    18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23, 
		    57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1};
	
	public static void main(String[] args) throws IOException{
		File keyFile = new File(args[1]);
		File inputFile = new File(args[2]);
		
		Scanner keyReader = new Scanner(keyFile);
		Scanner inputReader = new Scanner(inputFile);
				
		String cipherKey = keyReader.nextLine();
		int keyIndex = 0;
		int row = 0;
		int col = 0;
		byte binaryInByte = 0;
		String hexInBinary = "";
		String key = "";
		int count = 0;
		for(int i = 0; i<cipherKey.length()-1; i+=2) {
			if(count ==4) {
				++row;
				col = 0;
				count = 0;
			}
			key = cipherKey.substring(i, i+2);
			//System.out.println("key = " + key);
			hexInBinary = String.format("%8s", Integer.toBinaryString(Integer.parseInt(key, 16))).replace(' ', '0');
			binaryInByte = (byte)Integer.parseInt(key, 2);
			keyArray[row][col] = binaryInByte;
			++col;
			++count;
		}
        
		while(inputReader.hasNext()) {
			String inputLine = inputReader.nextLine();
			
			String hex = "";
			row = 0;
			col = 0;
			count = 0;//increments a row at every 4 runs
			binaryInByte = 0;
			hexInBinary = "";
			for(int i=0; i<inputLine.length()-1; i+=2) {
				hex = inputLine.substring(i, i+2);
				if(count == 4) {
					++row;
					col = 0;
					count = 0;
				}
				//inputArray[row][col] = hex;
				hexInBinary = String.format("%8s", Integer.toBinaryString(Integer.parseInt(hex, 16))).replace(' ', '0');
				//System.out.println("hexInBinary = " + hexInBinary);
				binaryInByte = (byte)Integer.parseInt(hexInBinary, 2);
				//System.out.println("*****bunaryInByte = " + Integer.toHexString(binaryInByte));
				inputArray[row][col] = binaryInByte;
				//inputArray[row][col] = Byte.valueOf(new BigInteger(hex,16).toString(2));
				++count;
				++col;
			}
			
			System.out.println("plain text is: ");
			printHexNum(inputArray);
			System.out.println();			
			addRoundKey();
			System.out.println("After addRoundKey(0):");
			printHexNum(inputArray);
			System.out.println();
			
			for(int i = 0; i<9; i++) {
				subBytes();
				System.out.println("after subBytes:");
				printHexNum(inputArray);
				System.out.println();
				
				shiftRows();
				System.out.println("after shiftRows:");
				printHexNum(inputArray);
				System.out.println();
				
				for(int j=0; j<4; j++) {
					mixColumns(j);
				}
				System.out.println("after mixColumns:");
				printHexNum(inputArray);
				System.out.println();
				
				addRoundKey();
				System.out.println("after addRoundKey(" + (i+1) + "): ");
				printHexNum(inputArray);
				System.out.println();
				
			}
			
			subBytes();
			System.out.println("after subBytes");
			printHexNum(inputArray);
			System.out.println();
			
			shiftRows();
			System.out.println("after shiftRows");
			printHexNum(inputArray);
			System.out.println();
			
			addRoundKey();
			System.out.println("after addRoundKey");
			printHexNum(inputArray);
			System.out.println();
			//System.out.println("sdghikfdhgkjfg = " + Integer.toHexString(inputArray[0][0]));
		}
	}
	public static void printHexNum (byte[][] twoDarr) {
		String hex = "";
		byte hexByte = 0;
		for(int col = 0; col < twoDarr[0].length; col++) {
			for(int row = 0; row<twoDarr.length; row++) {
				hexByte = twoDarr[row][col];
				hex = Integer.toHexString(hexByte & 0xff);
				if(hex.length() == 1) {
					System.out.print("0" + hex);
				}
				else 
					System.out.print(hex);
			}
		}
	}
	
	public static void addRoundKey() {
		//byte[] keyColumn = new byte[4];
		//byte[] inputColumn = new byte[4];
		String keyEle = "";
		String inputEle = "";
		String result = "";
		for(int col=0; col<4; col++) {
			for(int row = 0; row<4; row++) {
				//System.out.println("keyArray[row][col] = " + keyArray[row][col]);
				//System.out.println("inputArray[row][col] = " + inputArray[row][col]);
				inputArray[row][col] = (byte)(keyArray[row][col]  ^ inputArray[row][col]);
				//System.out.println("after, inputArray[row][col] = " + inputArray[row][col]);
				
				/*
				keyEle = Integer.toHexString(keyArray[row][col] & 0xff);
				inputEle = Integer.toHexString(inputArray[row][col] & 0xff);
				if(keyEle.equals("0")) {
					keyEle = "00";
				}
				if(inputEle.equals("0")) {
					inputEle = "00";
				}
				*/
				
				//System.out.println("keyEle = " + keyEle);
				//System.out.println("inputEle = " + inputEle);
				//String.format("%8s", Integer.toBinaryString(new BigInteger(keyEle,16))).replace(' ', '0');
				
				
				/*
				BigInteger keyNum = new BigInteger(keyEle,16);
				BigInteger inputNum = new BigInteger(inputEle, 16);
				result = keyNum.xor(inputNum).toString();
				//System.out.println("result = " + result);
				keyArray[row][col]  = (byte) (Integer.parseInt(result) & 0xff);
				*/
				//++i;
			}
		}
	}
	
	//Given by Prof.Young
	public static void mixColumns (int c) {
	    // This is another alternate version of mixColumn, using the 
	    // logtables to do the computation.
	    
	    byte a[] = new byte[4];
	    
	    // note that a is just a copy of st[.][c]
	    for (int i = 0; i < 4; i++) 
	        a[i] = inputArray[i][c];
	    
	    // This is exactly the same as mixColumns1, if 
	    // the mul columns somehow match the b columns there.
	    inputArray[0][c] = (byte)(mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
	    inputArray[1][c] = (byte)(mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
	    inputArray[2][c] = (byte)(mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
	    inputArray[3][c] = (byte)(mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));
	    } // mixColumn2
	
	//Given by Prof.Young
	public static void invMixColumns (int c) {
	    byte a[] = new byte[4];
	    
	    // note that a is just a copy of st[.][c]
	    for (int i = 0; i < 4; i++) 
	        a[i] = inputArray[i][c];
	    
	    inputArray[0][c] = (byte)(mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]));
	    inputArray[1][c] = (byte)(mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]));
	    inputArray[2][c] = (byte)(mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]));
	    inputArray[3][c] = (byte)(mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]));
	     } // invMixColumn2
	
	//Given by Prof.Young
	private static byte mul (int a, byte b) {
	    int inda = (a < 0) ? (a + 256) : a;
	    int indb = (b < 0) ? (b + 256) : b;

	    if ( (a != 0) && (b != 0) ) {
	        int index = (LogTable[inda] + LogTable[indb]);
	        byte val = (byte)(AlogTable[ index % 255 ] );
	        return val;
	    }
	    else 
	        return 0;
	} // mul
	
	public static void shiftRows() {
		byte temp = 0;
		int k = 0;
		for(int i=1; i<inputArray.length; i++) {
			k = i;
			while(k > 0) {
				temp = inputArray[i][0];
				for(int j = 1; j<inputArray[i].length; j++) {
					inputArray[i][j-1] = inputArray[i][j];
				}
				inputArray[i][inputArray[i].length-1] = temp;
				--k;
			}
		}
	}

	public static void subBytes() {
		byte hexByte = 0;
		int unsignedByte = 0;
		String hexString = "";
		byte sboxValue = 0;
		String hexInBinary = "";
		int row = 0;
		int col = 0;
		byte unsigned = 0;
		for(int i = 0; i<inputArray.length; i++) {
			for(int j = 0; j<inputArray[i].length; j++) {
				hexByte = inputArray[i][j];
				hexString = Integer.toHexString(hexByte & 0xff);
				if(hexString.length() == 1)
					hexString = "0" + hexString;
				//System.out.println("unsignedByte = " +  unsignedByte);
				//System.out.println("hexString = " + hexString);
				row = Integer.parseInt(Character.toString(hexString.charAt(0)), 16);
				col = Integer.parseInt(Character.toString(hexString.charAt(1)), 16);
				//System.out.println("row = " + row + ", col = " + col + ", sbox[row][col] = " + sbox[row][col]);

				//System.out.println("Integer.parseInt(sbox[row][col], 16) = " + Integer.parseInt(sbox[row][col], 16));
				sboxValue = (byte) Integer.parseInt(sbox[row][col], 16);
				inputArray[i][j] = sboxValue;
			}
		} 
	}
}





/*
public static void mixColumns() {
    void gmix_column(unsigned char *r) {
		unsigned char a[4];
		unsigned char c;
		for(c=0;c<4;c++) {
			a[c] = r[c];
			}
		r[0] = gmul(a[0],2) ^ gmul(a[3],1) ^ gmul(a[2],1) ^ gmul(a[1],3);
		r[1] = gmul(a[1],2) ^ gmul(a[0],1) ^ gmul(a[3],1) ^ gmul(a[2],3);
		r[2] = gmul(a[2],2) ^ gmul(a[1],1) ^ gmul(a[0],1) ^ gmul(a[3],3);
		r[3] = gmul(a[3],2) ^ gmul(a[2],1) ^ gmul(a[1],1) ^ gmul(a[0],3);
	}
	byte[] a = new byte[4];
	for(int c = 0; c<4; c++) {
		for(int row = 0; row<4; row++) {
			a[row] = inputArray[row][c];
		}
		
	}
}
*/
