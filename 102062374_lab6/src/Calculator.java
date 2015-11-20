
public class Calculator {
	
	private float result;
	
	public Calculator(){
		;
	}
	public static void main(String arg[])
	{
		;
	}

	public void setNumber(float i) {
		result = i ;
	}

	public float getResult() {
		return result;
	}

	public void plus(float i) {
		result += i;
	}

	public void minus(float i) {
		result -= i;
	}

	public void times(float i) {
		result *= i;
	}

	public void square() {
		result *=result;
	}

	public void powerOf(float num) {
		float temp = 1;
		for(int i=0;i<num;i++)
			temp *= result;
		result = temp;
	}

	public void dividedBy(float i) {
		result /= i;
	}

}
