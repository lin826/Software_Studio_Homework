package shape;

public class Rectangle extends Shape {

	double height,width;
	
	public Rectangle(double width,double height) {
		this.width = width;
		this.height = height;
		//set the variable height,width

	}

	@Override
	public double getArea() {
		//calculate the area and return
		return width*height;
	}

	@Override
	public boolean equals() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		//prepare for the final output
		return ("I am a rectangle and my area is "+(width*height));
	}

}
