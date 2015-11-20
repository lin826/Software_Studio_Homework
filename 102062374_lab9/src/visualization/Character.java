package visualization;

import processing.core.PApplet;
import processing.core.PImage;

public class Character {
	
	public static final int LEFT = 1;
	public static final int RIGHT = 3;
	public static final int STAY = 4;
	public static final int STEP = 5;
	
	private PApplet parent;
	private PImage[] image;
	private int x,y,frame,imgCount;
	private int movement;
	
	public Character(PApplet parent, PImage[] robotImg, int x, int y){
		this.parent = parent;
		this.image = robotImg;
		this.x = x;
		this.y = y;
		this.frame = 3;
		this.imgCount = 0;
	}
	
	public void display(){
		switch(this.movement){
		case LEFT:
			this.x -=STEP;
			if(this.x<-250)
				this.x = 1020;
			this.imgCount = 0;
			this.frame = (this.frame + 1) % 12 ;
			System.out.println("LEFT");
			break;
		case RIGHT:
			this.x += STEP;
			if(this.x>1020)
				this.x=-250;
			this.imgCount = 0;
			this.frame = (this.frame + 1) % 12 ;
			System.out.println("RIGHT");
			break;
		default:
			break;
		}
		parent.image(this.image[imgCount + frame/4], this.x, this.y);
	}

	public void setMovement(int m) {
		this.movement = m;
		
	}

}
