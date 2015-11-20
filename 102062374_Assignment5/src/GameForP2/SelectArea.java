package GameForP2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Game.ImgPlane;


public class SelectArea extends JPanel {
	

	private ImgPlane plane;
	private List<JButton> chunkButtons;
	private JPanel selectPlane;
	private JButton submit;
	private int[] selectedChunks;
	private int[][] selectedMatrix;
	private int rows;
	private int cols;
	private Rectangle bounds;
	//private boolean //;
	public boolean isSubmit = false;
	
	
	/** Constructor public SelectArea(ImgPlane plane, int rows, int cols)
	 * 		To construct the select area of the game.
	 *
	 *  @param ImgPlane plane - imgPlane of this round of game
	 *  @param int rows - numbers of row
	 *  @param int cols - numbers of columns   
	 * */
	public SelectArea(ImgPlane plane, int rows, int cols, Rectangle bounds){
		this.plane = plane;
		this.rows = rows;
		this.cols = cols;
		this.bounds = bounds;
		
		this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0,0,this.bounds.width,this.bounds.height);
		
		this.selectPlane = new JPanel();
		this.submit = new JButton("<html><p align=center><br>PRESS TO</br><br>start calibrating</br></html>");

		this.initSelectPlane();
		this.initSubmitButton();
		this.initSelectedChunksAndMtx();
		
		this.add(selectPlane);
		this.add(submit);
		
	}
	
	
	/*render image chunks*/
	private void initSelectPlane(){
		this.selectPlane.setLayout(null);
		
		this.selectPlane.setBounds(this.getWidth()/2+(this.getWidth()/2-plane.getChunkWidth()*cols)/2,
				60, plane.getImgChunk().get(0).getWidth() * (cols),
				plane.getImgChunk().get(0).getHeight() * (rows));
		this.selectPlane.setBackground(Color.black);
		this.selectPlane.setOpaque(true);
		System.out.println("width= "+plane.getImgChunk().get(0).getHeight() * (rows)+" "+" rows= "+rows+" height= "+plane.getImgChunk().get(0).getWidth() * (cols));
		
		chunkButtons = new ArrayList<JButton>();
		for(int i=0;i<cols*rows;++i){
			JButton chunkButton = new JButton(new ImageIcon(plane.getImgChunk().get(i)));
			chunkButton.setDisabledIcon(new ImageIcon(plane.getImgChunk().get(i)));
			chunkButton.setEnabled(false);
			this.chunkButtons.add(chunkButton);
			this.selectPlane.add(chunkButton);
			chunkButton.setBounds((i%cols)*(plane.getChunkWidth()+1), (i/cols)*(plane.getChunkHeight()+1), plane.getChunkWidth(), plane.getChunkHeight());
		}
	}

	
	private void initSubmitButton(){
		this.submit.setSize(150, 70);
		this.submit.setLocation((this.getWidth()-this.submit.getWidth())/2, 60+this.selectPlane.getHeight()+5);
		this.submit.setBackground(new Color(0,0,0));
		this.submit.setOpaque(true);
	}	
	
	
	private void initSelectedChunksAndMtx(){
		setSelectedChunks(new int[rows*cols]);
		for(int i=0;i<rows*cols;++i){
			getSelectedChunks()[i] = -1;
		}
	}
	
	private BufferedImage darkenImg(BufferedImage img){
		BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, null);
		float scaleFactor = 2f;
		RescaleOp op = new RescaleOp(scaleFactor, 0, null);
		bi = op.filter(bi, null);
		return bi;
	}
	
	public void begin(){
		this.setSubmit(true);	
		int count=0;
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				selectedMatrix[i][j] = getSelectedChunks()[i*cols+j];
				if(selectedMatrix[i][j]!=-1){
					++count;
					}
				
			}
		}

		if(count!=0){
			plane.setTargetPattern(selectedMatrix);
			System.out.println("disabled");
		}
		
	}
	
	
	
	public void selectChunk(int chunkIndex){
		chunkButtons.get(chunkIndex).setIcon(new ImageIcon(plane.getImgChunk().get(chunkIndex)));
		chunkButtons.get(chunkIndex).setDisabledIcon(new ImageIcon(darkenImg(plane.getImgChunk().get(chunkIndex))));

		getSelectedChunks()[chunkIndex] = chunkIndex ;
		System.out.println("idx= "+chunkIndex+" selected? " +getSelectedChunks()[chunkIndex]);
	}
	
	public void unselectChunk(int chunkIndex){
		chunkButtons.get(chunkIndex).setIcon(new ImageIcon(darkenImg(plane.getImgChunk().get(chunkIndex))));
		chunkButtons.get(chunkIndex).setDisabledIcon(new ImageIcon(plane.getImgChunk().get(chunkIndex)));

		chunkButtons.get(chunkIndex).setBackground(Color.red);
		chunkButtons.get(chunkIndex).invalidate();
		getSelectedChunks()[chunkIndex] = -1;
		System.out.println("idx= "+chunkIndex+" not selected? " +getSelectedChunks()[chunkIndex]);
	}
	
	public int[] getSelectedChunks() {
		return selectedChunks;
	}

	public void setSelectedChunks(int[] selectedChunks) {
		this.selectedChunks = selectedChunks;
	}


	public boolean isSubmit() {
		return isSubmit;
	}


	public void setSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}
}
