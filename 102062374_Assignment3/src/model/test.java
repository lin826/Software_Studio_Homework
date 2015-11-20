package model;

import java.io.*;
import java.util.Date;

import javax.xml.crypto.dsig.keyinfo.PGPData;

public class test {

	public static void main(String[] args) throws IOException {
		
		InputStreamReader read = new InputStreamReader (new FileInputStream ("res/MTTestData_v2.txt"),"UTF-8");
		
		BufferedReader br = new BufferedReader(read);

		String str = br.readLine(); 
		do {
/*		    str.split("[^-0-9]"); 	  // Get only the digits in the string.
		    str.split("[^A-Za-z ]");   // Get numbers letter with a space in the string. 
			str.split("[A-Za-z0-9-]"); // Get anything in the string but keep letter, digit and '-' away
*/
			str = br.readLine(); 
			System.out.println("字元數: "+str.length());
			System.out.println(str);
		} while(str != null);

		
		br.close();

			
		
/*		int size = 0;
		
		File f = new File ("res/MTTestData_v2.txt");
		FileReader fr = new FileReader(f);
		
		char [] message = new char [(int) f.length()];
		
		System.out.println("Encoding: "+fr.getEncoding());
		size = fr.read(message);
		System.out.println("字元數: "+size);
		for(int i=0;i<size;i++)
			System.out.print(message[i]);
		fr.close(); */
		
	}
}
