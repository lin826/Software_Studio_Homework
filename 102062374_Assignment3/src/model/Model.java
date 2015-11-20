package model;

/**
 * This is the file doing pre-process for your input. You are ask to:
 * 1.Implement file input and load it into your data structure. 
 * 	ArrayList is recommend as the data structure for SentencePC and SentenceMP 
 * 2.A method that randomly choose a sentence in your data structure as the 
 * 	question 
 * 
 * */

import java.util.*;
import java.io.*;


public class Model {
	public static ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
	

	/*
	 * Hints for parsing input:
	 * 
	 * Using "split" method in String class to distribute inputed line. Use
	 * regular expression as a split method String str = new String();
	 * str.split("[^-0-9]"); // Get only the digits in the string.
	 * str.split("[^A-Za-z ]"); // Get numbers letter with a space in the
	 * string. str.split("[A-Za-z0-9-]"); // Get anything in the string but keep
	 * letter, digit and '-' away
	 */

  	/*
  	 * public static void main(String[] args) throws IOException {  //自行命名方法
  	 * buildSentenceElementList();
  	 * }
  	 */

	/**
	 * private static String stringArrayToString(String[] str) For the return
	 * value of split is String[], turn String[] to String for better use
	 * @throws IOException 
	 * @input - String[] str - a String[]
	 */
	public static void buildSentenceElementList() throws IOException {
		InputStreamReader readMT = new InputStreamReader(new FileInputStream(
				"res/MTTestData_v3.txt"), "UTF-8");
		BufferedReader brMT = new BufferedReader(readMT);
		ArrayList<String> originalSentenceMTElements = new ArrayList<String>();
		int numOfSentenceMT = 0;
		int[] lengthOfSentenceMT = new int[10]; //用以紀錄 SentenceElementList 每幾個單位為一題
		int lastPosition = 0;	
		
		originalSentenceMTElements.add(brMT.readLine());
		System.out.print("res/MTTestData.txt \n");
		for (int position = 0; originalSentenceMTElements.get(position) != null; position++) {
			originalSentenceMTElements.add(brMT.readLine());			
			if (originalSentenceMTElements.get(position).equals("-1")) { // 形成一個題目
				lengthOfSentenceMT[numOfSentenceMT]=position;
				Sentence newSentence = new Sentence();				
				for (int index = lastPosition; index <= position-1; index++) {
					SentenceElement newSentenceElement = NewSentenceElement(originalSentenceMTElements, index);
					newSentence.addElement(newSentenceElement);
		    }
				System.out.print(newSentence.getEngSentence()+"\n");
				sentenceList.add(newSentence);
				lastPosition = position+1;
				numOfSentenceMT++;
			}			
		}
		brMT.close();
		
		InputStreamReader readPC = new InputStreamReader(new FileInputStream(
				"res/PCTestData.txt"), "UTF-8");
		BufferedReader brPC = new BufferedReader(readPC);
		ArrayList<String> originalSentencePCElements = new ArrayList<String>();
		ArrayList<SentenceElement> sentencePCElements = new ArrayList<SentenceElement>();
		int numOfSentencePC = 0;
		int[] lengthOfSentencePC = new int[10]; //用以紀錄 SentenceElementList 每幾個單位為一題
		lastPosition = 0;	
		
		originalSentencePCElements.add(brPC.readLine());
		System.out.print("res/PCTestData.txt \n");
		for (int position = 0; originalSentencePCElements.get(position) != null; position++) {
			originalSentencePCElements.add(brPC.readLine());			
			if (originalSentencePCElements.get(position).equals("-1")) { // 形成一個題目
				lengthOfSentencePC[numOfSentencePC]=position;
				Sentence newSentence = new Sentence();				
				for (int index = lastPosition; index <= position-1; index++) {
					SentenceElement newSentenceElement = NewSentenceElement(originalSentencePCElements, index);
					newSentence.addElement(newSentenceElement);
		    }
				System.out.print(newSentence.getEngSentence()+"\n");
				sentenceList.add(newSentence);
				lastPosition = position+1;
				numOfSentenceMT++;
			}			
		}
		brMT.close();
	}

	private static String stringArrayToString(String[] str) {
		String temp = new String();
		for (String i : str)
			temp += i; // return temp;
		return temp;
	}

	private static SentenceElement NewSentenceElement(
			ArrayList<String> originalSentenceElementList, int k) {
		// 建立 sentencList
		// Get only the digits in the string.
		String[] value = originalSentenceElementList.get(k).split("[^-0-9]");
		String stringValue = stringArrayToString(value);
		if(stringValue.equals("")) {
			stringValue = "0";
		}
		// 取得中間英文字串 Get numbers letter with a space in the string.
		String[] eng = originalSentenceElementList.get(k).split("[^'A-Za-z. ]");
		String stringEng = stringArrayToString(eng);
		// 取得中文翻譯字串 Get anything in the string but keep letter, digit
		// and '-' away
		String[] chi = originalSentenceElementList.get(k).split("[/s A-Za-z0-9-^/./']");
		String stringChi = stringArrayToString(chi);
		// 建立 SentenceElement
		SentenceElement e = new SentenceElement(stringValue, stringEng.trim(),
				stringChi);
		// 將新建立的 SentenceElement 放入 sentenceElementList
		return e;
	}
	public static List<Sentence> shuffleSentenceList()
	{
		Collections.shuffle(sentenceList);
		return sentenceList;
	}
}
