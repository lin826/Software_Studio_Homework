package Game;
/**
 *  Class Verifier
 *  	A class that compare between two matrix whether there has specific pattern in both 
 *  matrix:
 *  
 *  	+---+---+---+			+---+---+---+			
 *  	| 1 | 2	| 3	|			| 2 | 1 | 8 |			
 *  	+---+---+---+			+---+---+---+			
 *  	| 4 | 5	| 6	|	<--->   | 9 | 4 | 5 | 			
 *    	+---+---+---+	match	+---+---+---+			
 *  	| 7 | 8 | 9	|			| 6 | 7 | 3 |			
 * 		+---+---+---+			+---+---+---+			
 * 			  m1		              m2
 * 
 * 		Given a m * n matrix which contains unique elements (labeled from 1 ~ m*n), what we only 
 *  care is if there is a special permutation of elements between matrixes. For example, in matrix 
 *  m1, the permutation of 1, 4, and 7 in the same column is a target pattern that we only concern,
 *  the verifier will keep it as the answer and store it.
 *  	While in matrix m2, though the permutation is very different from m1, the permutation of 
 *  its second column is what we want, so Verifier will return true if this target pattern match 
 *  between two matrixes
 *  
 *  ?��?�class??��?��?�兩?�陣??�內?��?��?��??�特定�?��?�形??
 *  	如�?��?��?示�?�a1?��??��?��???��?��?��?��?��?��?��?��?��?�若??��?��?��??"以直條�?��?��??1 4 7三�?��?��??"?��??��?�目標�?�特徵�?��?�Verifier??�儲存此
 *  ?��徵在?��??�a2中�?��?�然??��?��?��?��?�擺?��位置不盡?��??��?��?�是?��第�?��?�中仍�?��?�直條�?��?��??1 4 7三�?��?��?��?��?�此經�?�Verifier??��?��?��?��?��??
 *  ??��?�傳true表示m2亦�?��?�此?��徵�??
 *  
 *  @author csc-lab
 * */

public class Verifier {
	private int[] rowAnsPattern;	//Answer pattern stored from left to right, up and down
	private int[] colAnsPattern;	//Answer pattern stored from top down and left to right
	private int rows, cols;

	
	/** Constructor Verifier
	 * 	
	 *  @param int rows - numbers of row
	 *  @param int cols - numbers of column
	 * */
	public Verifier(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
	}
	
	/** public void setAnswer(int[][] SelectPattern)
	 * 		Turns the target pattern in to rowAnsPattern and colAnsPattern
	 *  in Verifier and wait for matching. int[] transRowArray and int[] transColArray are
	 *  called to transform matrix to answer arrays
	 * 
	 *  @param int[][] SelectPattern - The target pattern
	 * */
	public void setAnswer(int[][] SelectPattern) {
		this.rowAnsPattern = this.transRowArray(SelectPattern);
		this.colAnsPattern = this.transColArray(SelectPattern);
	}
	
	private int[] transRowArray(int[][] answerMetrix) {
		int[] rowPattern = new int[this.rows * this.cols];
		
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				rowPattern[i * this.cols + j] = answerMetrix[i][j];
			}
		}
		rowPattern = this.purifyArray(rowPattern);
		return rowPattern;
	}
	
	private int[] transColArray(int[][] answerMetrix) {
		int[] colPattern = new int[this.rows * this.cols];
		for (int j = 0; j < this.cols; j++) {
			for (int i = 0; i < this.rows; i++) {
				colPattern[j * this.rows + i] = answerMetrix[i][j];
			}
		}
		colPattern = this.purifyArray(colPattern);
		return colPattern;
	}
	
	private int[] purifyArray(int[] input) {
		int start = 0, end = this.rows * this.cols;
		while (input[start] < 0) start++;
		while (input[end - 1] < 0) end--;
		int[] purifiedArray = new int[end - start];
		
		for (int i = 0; i < end - start; i++) purifiedArray[i] = input[i + start];  
		return purifiedArray;
	}
	
	/** public boolean puzzleMatch(int[][] input)
	 * 		To verify whether the input has target pattern or not. boolean verify
	 *  is called to perform string matching between rowAnsPattern and row array of input
	 *  and between colAnsPattern and column array of input
	 *  
	 *  @param int[][] input - the matrix which is waiting for test
	 * */
	public boolean puzzleMatch(int[][] input) {
		return verify(transRowArray(input), this.rowAnsPattern) && 
			   verify(transColArray(input), this.colAnsPattern);
	}
	
	private boolean verify(int[] inputArray, int[] ansPattern) {
		boolean rowPass = false;
		int start = 0;
		
		while(inputArray[start] != ansPattern[0]) start++;
		if ((start + ansPattern.length) > inputArray.length) return rowPass;
		
		for (int j = 0; j < ansPattern.length; j++) {
			if (ansPattern[j] == -1) continue;
			else {
				if (inputArray[start + j] == ansPattern[j]) rowPass = true;
				else {
					rowPass = false;
					break;
				}
			}
		}
		return rowPass;
	}
}
