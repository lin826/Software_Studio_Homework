package shape;

public class Circle extends Shape {

	double radius;
	public Circle(double radius) {
		//set the variable radius
		this.radius = radius;
	}

	@Override
	public double getArea() {
		//calculate the area and return
		return radius*radius*3.14;
	}

	@Override
	public boolean equals() {
		return false;
	}

	@Override
	public String toString() {
		//prepare for the final output
		return ("I am a circle and my area is "+(radius*radius*3.14));
	}

}
