import java.util.Scanner;

public class main {
	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Please enter your sequence: ");
		while(true){ //���ư���A���_���s��J�r��
			String input = scanner.nextLine(); //Ū�J�@��r��
			String[] s = input.split(" "); //���X�ťզۭ�
			int N = s.length; //�p��r���`����
			int longest=0; //�ŧi�̱`�^��l�r�ꬰ0
		
//		for(int a=0;a<N;a++)
//			System.out.println(s[a]);

		int[] p = new int[100]; //�x�s�̪��^��l�r��
		
		for(int x=0;x<N;x++) //�l�r��}�l�P�_��m��s[x]
			for(int y=x;y<N;y++){ //������m��s[y]
				int length=0,i=0; //�ŧi���l�r�ꤤ���ۦP�Ȫ����׬�0�Ai���w�P�_�l�r�����
				do{
					if (s[x+i].equals(s[y-i])) //��Z���k��i����m���Ȭ۵��Alength+1�C
						length+=2;
					i++;
					}while(y-x-(2*i)>1); //�P�_�줤���Ѥ@��(�l�r����׬��_��)�ΨS���F��(����)����
				if((y-x+1)%2==1)  //�����٦��Ѥ@�ӡAlength����+1
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