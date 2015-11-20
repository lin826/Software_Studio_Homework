package shape;

public class Square extends Shape {

	double length;
	/**
	 * @param args
	 */
	public Square(double length) {
		// TODO Auto-generated method stub
		this.length = length;

	}

	@Override
	public double getArea() {
		//calculate the area and return
		return length*length;
	}

	@Override
	public boolean equals() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		//prepare for the final output
		return ("I am a square and my area is "+(length*length));
	}

}
