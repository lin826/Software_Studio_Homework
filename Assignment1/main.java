import java.util.Scanner;

public class main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Please enter your sequence: ");
		while(true){ //重複執行，不斷重新輸入字串
			String input = scanner.nextLine(); //讀入一行字串
			String[] s = input.split(" "); //移出空白自原
			int N = s.length; //計算字串總長度
			int longest=0; //宣告最常回文子字串為0
		
//		for(int a=0;a<N;a++)
//			System.out.println(s[a]);

		int[] p = new int[100]; //儲存最長回文子字串
		
		for(int x=0;x<N;x++) //子字串開始判斷位置為s[x]
			for(int y=x;y<N;y++){ //結束位置為s[y]
				int length=0,i=0; //宣告此子字串中有相同值的長度為0，i為已判斷子字串長度
				do{
					if (s[x+i].equals(s[y-i])) //當距左右端i單位位置的值相等，length+1。
						length+=2;
					i++;
					}while(y-x-(2*i)>1); //判斷到中間剩一個(子字串長度為奇數)或沒有東西(偶數)為止
				if((y-x+1)%2==1)  //當中間還有剩一個，length長度+1
					length++;
				if(length>=(y-x)&&length>=longest){
					for(int j=0;j<(y-x+1);j++){
						p[j]=Integer.valueOf(s[x+j]);
						System.out.print(s[x+j]);
					}					
					System.out.println(p[0]);
					longest = length;
				}	
			} 
		for(int b=0;b<longest;b++){
			for(int c=0;c<p[b];c++)
				System.out.print((char)168);
			System.out.println("");
		}
		System.out.print("Please enter your sequence: "); 
		}
	} 
}