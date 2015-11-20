package shape;

import java.lang.Comparable;
public abstract class Shape implements Comparable<Shape>{

	public abstract double getArea();
	public abstract boolean equals();
	public abstract String toString();

	//compare two shape's area and return the result as 1 means >, 0 means =, and -1 means <
	public int compareTo(Shape AnotherShape){
		if(this.getArea()>AnotherShape.getArea())
			return 1;
		else if(this.getArea()==AnotherShape.getArea())
			return 0;
		else
			return -1;
	}
}
