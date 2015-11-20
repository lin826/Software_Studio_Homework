package game;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/** 
 *  Class ImgPlane
 * 
 * 		A class that perform image loading, splitting and other imgPlane control method.
 *  	Initially, after loading the image, it will be split in chunks and the original position
 *  of each chunks will be saved. Other methods will see imgPlane as a library which provide 
 *  basic image information.
 *  
 *		這個class將儲存有關Image的相關方法，包含圖片讀取、將圖片分為小塊，每個小塊會依照其原來的位置被編號，並依序儲存在資料
 *	結構內(由左而右，由上至下)。ImgPlane儲存的資訊將會被其他的class進一步調用。
 * */

public class ImgPlane {
	private int[][] imgPosition; 
	// numbering the chunk of image, from left to right, Top down, start from 0.
	// imgPosition[i][j] = the index of the chunk which put at the location [i][j].
	
	private List<BufferedImage> imgChunks = new ArrayList<BufferedImage>(); // instance of image
	private String tag , source;
	private int rows;
	private int cols;
	private int[][] targetPattern = null; // catch the information about which chunk is selected
	
	/** Constructor ImgPlane (String pathName, int rows, int cols)
	 * 		Constructor for initializing image plane, original position of image chunks are also
	 * 		initialized here
	 * 
	 *  @param - pathName: the inputed image path
	 *  @param - rows
	 *  @patam - cols 
	 * */
	public ImgPlane(String pathName,String tag, int rows, int cols) {
		this.source = pathName;
		this.tag = new String(tag);
		this.rows = rows;
		this.cols = cols;
		imgPosition = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				imgPosition[i][j] = i * rows + j;
			}
		}
		this.imgChunks = loadAndChunkImage(pathName);
	}
	
	/** private BufferedImage[] loadAndChunkImage(String pathName)
	 * 		It load the image and cut it in a chunk by inputed numbers of rows and columns
	 * 
	 *  @param - pathName: input image
	 * */
	private List<BufferedImage> loadAndChunkImage(String pathName) {
		File file = new File(pathName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException ex) {
			System.out.println("Image file not found");
			ex.printStackTrace();
		}
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(fis);
			
		} catch (IOException ex) {
			System.out.println("Image file loading fail");
			ex.printStackTrace();
		}
		
		int chunkWidth = image.getWidth() / this.cols;
		int chunkHeight = image.getHeight() / this.rows;
		List<BufferedImage> imgChunks = new ArrayList<BufferedImage>();

		for (int r = 0; r < this.rows; r++) {
			for (int c = 0; c < this.cols; c++) {
				imgChunks.add(image.getSubimage(c*chunkWidth, r*chunkHeight, chunkWidth, chunkHeight));
			}
		}
		return imgChunks;
	}
	
	/** public void randomImgPosition()
	 * 		it shuffle the element in random position of imgPlane
	 * 
	 *  @param - none
	 * */
	public void randomImgPosition() {
		int shuffleTime = (int)((Math.random())* 500);
		int a1, b1, a2, b2,temp;
		
		for(int i = 0; i < shuffleTime; i++) {
			a1  = (int)((Math.random())* this.rows);
			b1  = (int)((Math.random())* this.cols);
			a2  = (int)((Math.random())* this.rows);
			b2  = (int)((Math.random())* this.cols);
			
			temp = imgPosition[a1][b1];
			imgPosition[a1][b1] = imgPosition[a2][b2];
			imgPosition[a2][b2] = temp;
		}
	}
	
	public void setTargetPattern(int[][] target) {
		this.targetPattern = target; 
	}
	
	public void exchangeImgPosition(Point p1, Point p2){
		int temp = imgPosition[p1.x][p1.y];
		
		System.out.println("p1 " +imgPosition[p1.x][p1.y] +" p2 "+imgPosition[p2.x][p2.y]);
		imgPosition[p1.x][p1.y] = imgPosition[p2.x][p2.y];
		System.out.println("p1 " +imgPosition[p1.x][p1.y] +" p2 "+imgPosition[p2.x][p2.y]);
		imgPosition[p2.x][p2.y] = temp;
		System.out.println("p1 " +imgPosition[p1.x][p1.y] +" p2 "+imgPosition[p2.x][p2.y]);
	}
	
	public int[][] getImgPosition() {
		return this.imgPosition;
	}
	
	public List<BufferedImage> getImgChunk() {
		return this.imgChunks;
	}
	
	public int[][] getTargetPattern() {
		return this.targetPattern;
	}
	
	public String getTag(){
		return this.tag;
	}
	
	public String getSource(){
		return this.source;
	}
	
	public int getChunkWidth(){
		return imgChunks.get(0).getWidth(); 
	}
	
	public int getChunkHeight(){
		return imgChunks.get(0).getHeight();
	}
}
