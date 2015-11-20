package game;

import processing.core.PApplet;
import processing.core.PImage;

public class Plane {
	
	public static final int STAY = 0;
	public static final int UP = 1;
	public static final int DOWN = 2;
	
	private PApplet parent;
	private PImage[] images;
	private int x, y, w, h;
	private int frame, imageCount, movement,delay=3;
	
	public Plane(PApplet parent, PImage[] images) {
		this.parent = parent;
		this.images = images;
		this.imageCount = images.length;
		this.x = 0;
		this.y = parent.height/2;
		this.w = images[0].width;
		this.h = images[0].height;
	}
	
	public void display() {
		
		switch(this.movement){
			case UP:
				this.y -= 5;
				this.frame = (this.frame + 1)%(imageCount*delay); // imageCount*delay for more smoothly
				break;
			case DOWN:
				this.y += 5;
				this.frame = (this.frame + 1)%(imageCount*delay); // imageCount*delay for more smoothly
				break;
			default:
				break;
		}
		this.parent.image(images[frame/delay], x, y, w, h); // (frame/delay) to get the picture in images
		
	}
	
	public void setMovement(int m){
		this.movement = m;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public int getW(){
		return this.w;
	}
	
	public int getH(){
		return this.h;
	}
	
}
