import java.util.Scanner;

public class OutShuffle {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Please enter the number of cards and how many time you want to shuffle :");
		int n = scanner.nextInt(); //the number of cards
		int t = scanner.nextInt(); //how many time you want to shuffle
		do{
			int[] cards = new int[n];
			for(int i = 0; i < n; i++){ 
				cards[i] = i+1;
			} //�]�w�d�����X
			
			int[] temp = new int[n]; //�~�P��Ȧs���}�C
			for(int x = 0; x < t; x++){ //�@�~t�����~�P�j��
				for(int y = 1; y < n/2; y++){ //�C���~�P�洫�Ƨ�
					temp[2*y-1]=cards[n/2+y-1];
					temp[2*y]=cards[y];
				}
				for(int z = 1; z < n-1; z++)
					cards[z]=temp[z];
			}		

			for(int i = 0; i < n; i++)  //��X�~�P���G
			System.out.print(cards[i]+" ");
			
			System.out.print("\nPlease enter the number of cards and how many time you want to shuffle :");
			n = scanner.nextInt(); //the number of cards
			t = scanner.nextInt(); //how many time you want to shuffle
			scanner.close();
		}while(n!=0&&t!=0);
		System.out.print("End of shuffling.");
		
	}
}
