package shape;

import java.util.Arrays;

public class AreaCalculator {

	//http://www.dotblogs.com.tw/pin0513/archive/2010/03/08/13931.aspx
		private static AreaCalculator instance = null; //建構式
		//透用一個靜態變數記錄實體
		public static AreaCalculator getInstance()
		{
			//Lazy instantiate(拖延實體化)
			if(instance == null){
				instance = new AreaCalculator();
			}
			
			return instance;
		}
	
	public static void main(String[] args) {
		
		//define the variables for each shape
		Shape rectangleA = new Rectangle(20,10);
		Shape squareB = new Square(6);
		Shape circleC = new Circle(6);
		Shape circleD = new Circle(8.5);
		Shape rectangleE = new Rectangle(12,3);
		
		Shape[] shapes = new Shape[5];
		shapes[0] = rectangleA;
		shapes[1] = squareB;
		shapes[2] = circleC;
		shapes[3] = circleD;
		shapes[4] = rectangleE;
		
		String[] shapesname = new String[5];
		shapesname[0] = "rectangleA";
		shapesname[1] = "squareB";
		shapesname[2] = "circleC";
		shapesname[3] = "circleD";
		shapesname[4] = "rectangleE";
		
		//compare and output the result of two different shape
/*		for(int i=0;i<5;i++)
			for(int j=i+1;j<5;j++){
				if(shapes[i].compareTo(shapes[j])==0){
					System.out.println(shapesname[i]+" is equal to "+shapesname[j]);
				}else if(shapes[i].compareTo(shapes[j])<0){
					System.out.println(shapesname[i]+" is less than "+shapesname[j]);
				}else if(shapes[i].compareTo(shapes[j])>0){
					System.out.println(shapesname[i]+" is larger than "+shapesname[j]);
				}
			}		
*/
		if(squareB.compareTo(rectangleE)==0){
			System.out.println("squareB is equal to rectangleE");
		}else if(squareB.compareTo(rectangleE)<0){
			System.out.println("squareB is less than rectangleE");
		}else {
			System.out.println("squareB is larger than rectangleE");
		}
		//sum up areas of the shapes
		System.out.println("the sum of B&C is " + sumOfArea(squareB, circleC));
		System.out.println("the sum of A&C&D is " + sumOfArea(rectangleA,circleC,circleD));
		
		//resort the shapes with the area
		Arrays.sort(shapes);
		
		//output the area of every shapes from small to big
		for(int i=0;i<shapes.length;i++){
			System.out.println(shapes[i]);
		} 

	}

	//while we'd like to sum up three area of the shapes
	private static double sumOfArea(Shape s1, Shape s2,Shape s3) {
		return (s1.getArea()+s2.getArea()+s3.getArea());
	}

	//while we'd like to sum up two area of the shapes
	private static double sumOfArea(Shape s1, Shape s2) {
		return (s1.getArea()+s2.getArea());
	}
}
