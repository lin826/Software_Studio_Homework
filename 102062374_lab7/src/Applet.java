import processing.core.*;
import visualization.MercatorMap;

public class Applet extends PApplet{
	PImage mapImage;
	PVector place;

	public void setup() {
		this.setSize(1000,800);
		smooth();
		background(0);
		mapImage = loadImage("res/WorldMap.jpeg");
		MercatorMap mercatorMap = new MercatorMap(991f,768f);
		place = mercatorMap.getScreenLocation(new PVector(23.583234f,120.5825975f));
		
		}
	
	public void draw() {		
		image(mapImage,0,0);		
		text("Taiwan", place.x-15, place.y-10);
		noStroke();
		ellipse(place.x, place.y, 10, 10);
		fill(0, 127, 255);

		
		
	}
	
}
