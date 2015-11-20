package game;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JProgressBar;


/*This class deals with time bar gui, please implement it*/
public class TimeBar extends JProgressBar{
	
	public TimeBar()
	{
		super();
		this.setForeground(Color.GREEN);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
}
