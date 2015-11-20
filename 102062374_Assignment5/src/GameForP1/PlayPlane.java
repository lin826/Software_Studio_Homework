package GameForP1;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.ImgPlane;


public class PlayPlane extends JPanel{
	
	private ImgPlane plane;
	private int rows;
	private int cols;
	private JLabel chunkLabels[][]; //not initiated yet
	private boolean donePlay;
	
	
	public PlayPlane(ImgPlane plane, int rows, int cols){
		this.plane = plane;
		this.rows = rows;
		this.cols = cols;
		this.donePlay = false;
		
		this.initPlayPlane();
	}
	
	/***************************** codes for playPlane *******************************/
	private void initPlayPlane(){
		this.setLayout(null);
		this.setEnabled(false);
		

		plane.randomImgPosition();
		chunkLabels = new JLabel[rows][cols];
		for(int i=0;i<rows;++i){
			for(int j=0;j<cols;++j){
				JLabel chunkLabel = new JLabel(new ImageIcon(plane.getImgChunk().get(plane.getImgPosition()[i][j])));
				chunkLabels[i][j] = chunkLabel;
				this.add(chunkLabels[i][j]);
				chunkLabels[i][j].setBounds(j*(plane.getChunkWidth()+1), i*(plane.getChunkHeight()+1), plane.getChunkWidth(), plane.getChunkHeight());
			}
		}
		
	}
	
	public void rearrangePlane(){
		this.removeAll();
		for(int i=0;i<rows;++i){
			for(int j=0;j<cols;++j){
				JLabel chunkLabel = new JLabel(new ImageIcon(plane.getImgChunk().get(plane.getImgPosition()[i][j])));
				chunkLabels[i][j] = chunkLabel;
				this.add(chunkLabels[i][j]);
				chunkLabels[i][j].setBounds(j*(plane.getChunkWidth()+1), i*(plane.getChunkHeight()+1), plane.getChunkWidth(), plane.getChunkHeight());
			}
		}
	}
	
	
	private BufferedImage darkenImg(BufferedImage img){
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, null);
		float scaleFactor = 0.5f;
		RescaleOp op = new RescaleOp(scaleFactor, 0, null);
		bi = op.filter(bi, null);
		return bi;
	}
	
	public boolean hasDonePlay(){
		return donePlay;
	}
}
