package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Topbar extends JPanel{

	private Stack<JLabel> lives = new Stack<JLabel>();
	private int score = 0;
	private int height,width,lifecount;
	private BufferedImage heart;
	Game game;
	
	public Topbar(Game game ,Rectangle bounds, int lifecount, int score) {
		this.game = game;
		this.setBackground(Color.BLACK);		
		this.height = bounds.height;
		this.width = bounds.width;
		this.lifecount=lifecount;
		this.score = score;
		this.setBounds(bounds);
		this.setPreferredSize(new Dimension(this.width , this.height));
		try {                
	          heart = ImageIO.read(new File("res/heart.png"));
	       } catch (IOException ex) {
	            // handle exception...
	       }
		this.paintImmediately(bounds); //Paints the specified region now.
	}
	
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int x = 20 ;
	        for(int i=0;i<lifecount;i++)
	        	g.drawImage(heart , x+i*(heart.getWidth()+5) , 5 , this); // see javadoc for more info on the parameters            
	        g.setFont(new Font(Font.SERIF , Font.BOLD , 26));
			g.setColor(Color.WHITE);
	        g.drawString("Score: "+String.valueOf(game.score), this.width/2-50 , 12+heart.getHeight()/2);
	    }
	
	/*
	 * implement all labels and methods you need to do about "score" and "life"
	 */
	

}
