package visualization;

import processing.core.*;

public class Gamescene extends PApplet {
	private PImage[] robotImg;
	private Character robot;
	public void setup(){
		
		size(1020, 770);
		this.robotImg = new PImage[3];
		this.robotImg[0] = loadImage(this.getClass().getResource("/res/Robot_Dark_1.png").getPath());
		this.robotImg[1] = loadImage(this.getClass().getResource("/res/Robot_Dark_2.png").getPath());
		this.robotImg[2] = loadImage(this.getClass().getResource("/res/Robot_Dark_3.png").getPath());
		this.robot = new Character(this,this.robotImg,width/2,height/2);
	}
	public void draw(){
		background(255,255,255);
		frameRate(15);
		if(!keyPressed)
			robot.setMovement(Character.STAY);
		robot.display();
	}
	public void keyPressed(){
		if(keyCode==LEFT)
			robot.setMovement(Character.LEFT);
		else if(keyCode==RIGHT)
			robot.setMovement(Character.RIGHT);
	}

}
