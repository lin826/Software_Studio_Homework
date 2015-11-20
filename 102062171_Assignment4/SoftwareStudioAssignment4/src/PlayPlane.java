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
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class PlayPlane extends JPanel implements MouseMotionListener, MouseListener {
	
	private ImgPlane plane;
	private int rows;
	private int cols;
	private JLabel chunkLabels[][]; //not initiated yet
	private Point xyPoint;
	private Point indexPoint;
	private Point enteredIndexPoint;
	private boolean donePlay;
	
	
	public PlayPlane(ImgPlane plane, int rows, int cols){
		this.plane = plane;
		this.rows = rows;
		this.cols = cols;
		this.donePlay = false;
		
		this.initPlayPlane();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
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
	
	private Point xy2Index(Point xyPoint){
		Point indexPoint = new Point(xyPoint.y/(plane.getChunkHeight()+1),xyPoint.x/(plane.getChunkWidth()+1));
		return indexPoint;
	}
	
	private Point index2Xy(Point indexPoint){
		Point xyPoint = new Point(indexPoint.y*(plane.getChunkWidth()+1),indexPoint.x*(plane.getChunkHeight()+1));
		return xyPoint;
	}
	
	private void resetExchangedLabelPosition(Point p1, Point p2){
		chunkLabels[p1.x][p1.y].setIcon(new ImageIcon(plane.getImgChunk().get(plane.getImgPosition()[p1.x][p1.y])));
		chunkLabels[p2.x][p2.y].setIcon(new ImageIcon(plane.getImgChunk().get(plane.getImgPosition()[p2.x][p2.y])));
		chunkLabels[p1.x][p1.y].repaint();
		chunkLabels[p2.x][p2.y].repaint();
		
	}
	
	
	public boolean hasDonePlay(){
		return donePlay;
	}

	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(donePlay==false){
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
		if (donePlay == false) {
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
		if(donePlay==false){
			chunkLabels[enteredIndexPoint.x][enteredIndexPoint.y].setBounds(index2Xy(enteredIndexPoint).x, index2Xy(enteredIndexPoint).y, plane.getChunkWidth(), plane.getChunkHeight());
			this.donePlay = true;			
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}


}
