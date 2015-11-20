package game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PlayPlane extends JPanel implements MouseMotionListener, MouseListener {
	
	final Game game;
	
	private ImgPlane plane;
	private int rows;
	private int cols;
	private JLabel chunkLabels[][]; //not initiated yet
	private HashMap<Integer , JLabel> chunkMap;
	private Point xyPoint;
	private Point indexPoint;
	private Point enteredIndexPoint;
	private boolean donePlay;
	private boolean isEnable;
	private int[] highlightIndexes;
	
	public PlayPlane(Game game , ImgPlane plane, int rows, int cols){
		this.game = game;
		this.plane = plane;
		this.rows = rows;
		this.cols = cols;
		this.donePlay = false;
		this.isEnable = false;
		this.highlightIndexes = null;
		
		this.initPlayPlane();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void setPlayplaneEnabled(boolean flag)
	{
		this.isEnable = flag;
		this.setEnabled(flag);
		for(JLabel[] row : this.chunkLabels){
			for(JLabel col : row){
				//col.setEnabled(flag);
			}
		}
	}
	
	/***************************** codes for playPlane *******************************/
	private void initPlayPlane(){
		this.setLayout(null);

		plane.randomImgPosition();
		chunkLabels = new JLabel[rows][cols];
		chunkMap = new HashMap<Integer , JLabel>();
		
		for(int i=0;i<rows;++i){
			for(int j=0;j<cols;++j){
				int idx = plane.getImgPosition()[i][j];
				JLabel chunkLabel = new JLabel(new ImageIcon(plane.getImgChunk().get(idx)));
				chunkLabels[i][j] = chunkLabel;
				chunkMap.put(idx, chunkLabel);
				this.add(chunkLabels[i][j]);
				chunkLabels[i][j].setBounds(j*(plane.getChunkWidth()+1), i*(plane.getChunkHeight()+1), plane.getChunkWidth(), plane.getChunkHeight());
			}
		}
		
		this.setPlayplaneEnabled(false);
	}
	
	/*Set highlight id*/
	public void setHighlight(int[] indexes)
	{
		for(int i : indexes)
			System.out.print(i + " ");
		System.out.println();
		this.highlightIndexes = indexes;
	}
	
	/*Paint highlight plane*/
	public void highLightPlane()
	{
		try{
			for(int idx : this.highlightIndexes){
				if(idx >= 0){
					JLabel chunk = this.chunkMap.get(idx);
					chunk.setIcon(new ImageIcon(highlightImg(plane.getImgChunk().get(idx))));
				}
			}
		} catch (Exception e){
			e.printStackTrace();
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
	
	public void blinkPlane(boolean flag)
	{
		for(int i=0;i<rows;++i){
			for(int j=0;j<cols;++j){
				ImageIcon icon = (flag) ? new ImageIcon(highlightImg(plane.getImgChunk().get(plane.getImgPosition()[i][j])))
										: new ImageIcon(darkenImg(plane.getImgChunk().get(plane.getImgPosition()[i][j])));
				this.chunkLabels[i][j].setIcon(icon);
			}
		}
	}
	
	private BufferedImage highlightImg(BufferedImage img){
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, null);
		float scaleFactor = 2f;
		RescaleOp op = new RescaleOp(scaleFactor, 0, null);
		bi = op.filter(bi, null);
		return bi;
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
	
	private Point xy2Index(Point xyPoint){
		Point indexPoint = new Point(xyPoint.y/(plane.getChunkHeight()+1),xyPoint.x/(plane.getChunkWidth()+1));
		return indexPoint;
	}
	
	private Point index2Xy(Point indexPoint){
		Point xyPoint = new Point(indexPoint.y*(plane.getChunkWidth()+1),indexPoint.x*(plane.getChunkHeight()+1));
		return xyPoint;
	}
	
	private void resetExchangedLabelPosition(Point p1, Point p2){
		int idx1 = plane.getImgPosition()[p1.x][p1.y];
		int idx2 = plane.getImgPosition()[p2.x][p2.y];
		
		ImageIcon icon1 = (isContain(idx1)) ? new ImageIcon(highlightImg(plane.getImgChunk().get(idx1)))
											: new ImageIcon(plane.getImgChunk().get(idx1));

		ImageIcon icon2 = (isContain(idx2)) ? new ImageIcon(highlightImg(plane.getImgChunk().get(idx2)))
											: new ImageIcon(plane.getImgChunk().get(idx2));
		
		chunkLabels[p1.x][p1.y].setIcon(icon1);
		chunkLabels[p2.x][p2.y].setIcon(icon2);
		chunkLabels[p1.x][p1.y].repaint();
		chunkLabels[p2.x][p2.y].repaint();
		
	}
	
	
	public boolean hasDonePlay(){
		return donePlay;
	}
	
	/*if highlight indexes contains this index*/
	public boolean isContain(int index)
	{
		for(int i : this.highlightIndexes){
			if(index == i)
				return true;
		}
		return false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(!donePlay && isEnable){
		chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(e.getX()-plane.getChunkWidth()/2, e.getY()-plane.getChunkHeight()/2, plane.getChunkWidth(), plane.getChunkHeight());
		if(e.getX()>((enteredIndexPoint.y+1)*plane.getChunkWidth())){ //right
			Point temp = new Point(enteredIndexPoint.x, enteredIndexPoint.y+1);
			plane.exchangeImgPosition(enteredIndexPoint,temp);
			this.resetExchangedLabelPosition(enteredIndexPoint, temp);
			chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(index2Xy(enteredIndexPoint).x, index2Xy(enteredIndexPoint).y, plane.getChunkWidth(), plane.getChunkHeight());
			enteredIndexPoint = new Point(temp.x,temp.y);
		}
		else if(e.getX()<((enteredIndexPoint.y)*plane.getChunkWidth())){ //left
			Point temp = new Point(enteredIndexPoint.x, enteredIndexPoint.y-1);
			plane.exchangeImgPosition(enteredIndexPoint,temp);
			this.resetExchangedLabelPosition(enteredIndexPoint, temp);
			chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(index2Xy(enteredIndexPoint).x, index2Xy(enteredIndexPoint).y, plane.getChunkWidth(), plane.getChunkHeight());
			enteredIndexPoint = new Point(temp.x,temp.y);
		}
		else if(e.getY()<((enteredIndexPoint.x)*plane.getChunkWidth())){ //up
			Point temp = new Point(enteredIndexPoint.x-1, enteredIndexPoint.y);
			plane.exchangeImgPosition(enteredIndexPoint,temp);
			this.resetExchangedLabelPosition(enteredIndexPoint, temp);
			chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(index2Xy(enteredIndexPoint).x, index2Xy(enteredIndexPoint).y, plane.getChunkWidth(), plane.getChunkHeight());
			enteredIndexPoint = new Point(temp.x,temp.y);
		}
		else if(e.getY()>((enteredIndexPoint.x+1)*plane.getChunkWidth())){ //down
			Point temp = new Point(enteredIndexPoint.x+1, enteredIndexPoint.y);
			plane.exchangeImgPosition(enteredIndexPoint,temp);
			this.resetExchangedLabelPosition(enteredIndexPoint, temp);
			chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(index2Xy(enteredIndexPoint).x, index2Xy(enteredIndexPoint).y, plane.getChunkWidth(), plane.getChunkHeight());
			enteredIndexPoint = new Point(temp.x,temp.y);
		}
		else;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!donePlay && isEnable) {
			xyPoint = e.getPoint();
			indexPoint = xy2Index(xyPoint);
			enteredIndexPoint = indexPoint;
			this.remove(chunkLabels[indexPoint.x][indexPoint.y]);
			this.add(chunkLabels[indexPoint.x][indexPoint.y]);
			chunkLabels[indexPoint.x][indexPoint.y].setIcon(new ImageIcon(darkenImg(plane.getImgChunk().get(plane.getImgPosition()[indexPoint.x][indexPoint.y]))));
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!donePlay && isEnable){
			chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(index2Xy(enteredIndexPoint).x, index2Xy(enteredIndexPoint).y, plane.getChunkWidth(), plane.getChunkHeight());
			this.donePlay = true;
			
			//TODO: send the tag info via client
			this.game.sendAnswer();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}


}
