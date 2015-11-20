package game;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Model {
	
	private int linesAmount;
	private String fileName;
	private List<String> list;
	
	public Model(String fileName) throws IOException{
		linesAmount = countLines(fileName);
		this.fileName = new String(fileName);
		this.list = this.getImgDatas();
	}
	
	public List<String> getImgDatas()
	{
		List<String> list = null;
		try {
			list = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = br.readLine()) != null){
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ImgPlane getPlaneByIndex(int index , int rows , int cols)
	{
		ImgPlane plane = null;
		try{
			String nameAndTag[] = list.get(index).split(" " , 2);
			plane = new ImgPlane("res/assignment4TestData/"+nameAndTag[0],nameAndTag[1],rows,cols);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return plane;
	}
	
	public ImgPlane getRandomPlane(int rows, int cols){
		Random num = new Random();
		String nameAndTag[] = new String[2];
		try {
			BufferedReader br =new BufferedReader(new FileReader(fileName));
			String temp = new String();
			for(int i=0;i<=num.nextInt(linesAmount);++i){
				temp=br.readLine();
			}
			nameAndTag = temp.split(" ",2);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImgPlane plane = new ImgPlane("res/assignment4TestData/"+nameAndTag[0],nameAndTag[1],rows,cols);
		return plane;
	}

	/** public static int countLines(String filename)
	 * 		return the number of lines in a file, which is by counting the number of carriage return
	 * 
	 * reference:
	 * 		http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 * 
	 **/
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
}
