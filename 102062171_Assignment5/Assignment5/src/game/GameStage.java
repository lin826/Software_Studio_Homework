package game;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;


public class GameStage extends JPanel{
	
	final Game game;
	
	private int rows;
	private int cols;
	private int time = 100;

	private Rectangle bounds;
	private ImgPlane plane;
	private JTextField tag;
	private JLabel stageTag;
	private Verifier verifier;
	
	private SelectArea selectArea;
	private PlayPlane playPlane;
	private TimeBar timeBar;
	
	private boolean winFlag;
	private boolean doneRobotSummonAnimationFlag;
	private boolean isTimeUPFlag;
	private boolean isWrongMatch;
	private boolean isOver;
	
	
	/** Constructor GameStage(Rectangle bounds, ImgPlane plane, int rows, int cols)
	 * 		Generate a GameStage, initialize all the instance.
	 * 
	 *  @param Rectangle bounds - the size of image chunk
	 *  @param ImgPlane plane - the select image
	 *  @param int rows - number of rows
	 *  @param int cols - number of columns
	 * */
	public GameStage(Game game , Rectangle bounds, ImgPlane plane,int rows,int cols, int stageNumber){
		this.game = game;
		this.winFlag = false;
		this.doneRobotSummonAnimationFlag = false;
		this.isTimeUPFlag = false;
		this.isWrongMatch = false;
		this.isOver = false;
		
		this.rows = rows;
		this.cols = cols;
		this.plane = plane;
		this.bounds = bounds;
		
		this.setLayout(null);
		this.setBounds(50,0,this.bounds.width,this.bounds.height);
		Random Rcolor = new Random();
		this.setBackground(new Color(252, 200+Rcolor.nextInt(40), 60+Rcolor.nextInt(100)));
		this.setVisible(true);
		

		this.verifier = new Verifier(rows,cols);
		this.tag = new JTextField(10);
		this.stageTag = new JLabel("No."+stageNumber);
		this.selectArea = new SelectArea(this.game , plane, rows,cols,this.bounds);
		this.playPlane = new PlayPlane(this.game , plane, rows, cols);
		
		//Time bar
		this.timeBar = new TimeBar();
		this.timeBar.setBounds(new Rectangle(1000 , 50));
		this.timeBar.setLocation(150 , 600);
		this.timeBar.setValue(time);
		
		this.initTag();
		this.initStageTag();
				
		this.playPlane.setBounds((this.getWidth()/2-plane.getChunkWidth()*cols)/2,
				60, 
				plane.getChunkWidth()*(cols), 
				plane.getChunkHeight()*(rows));
		
		
		this.add(tag);
		this.add(stageTag);
		this.add(selectArea);
		this.add(playPlane);
		this.add(timeBar);
				
	}
	
	private void initTag(){
		this.tag.setBackground(Color.gray);
		this.tag.setFont(new Font("Courier", Font.PLAIN, 20));
		this.tag.setOpaque(true);
		this.tag.setBounds(this.getWidth()/4*3-70,10,150,25);
	}
	
	private void initStageTag() {
		this.stageTag.setBackground(Color.gray);
		this.stageTag.setOpaque(true);
		this.stageTag.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.stageTag.setBounds(this.getWidth() - 70, 0, 40, 20);

	}

	/*Set enabled state for the playplane*/
	public void setPlayplaneEnabled(boolean flag)
	{
		this.playPlane.setPlayplaneEnabled(flag);
	}
	
	/*Set enabled state for the selectarea*/
	public void setSelectareaEnabled(boolean flag)
	{
		this.tag.setEditable(flag);
		this.tag.setBackground((flag) ? Color.WHITE : Color.DARK_GRAY);
		this.tag.setForeground((flag) ? Color.BLACK : Color.WHITE);
		this.selectArea.setSelectAreaEnabled(flag);
	}
	
	/*Get tag info from stage components*/
	public JSONObject getTaginfo()
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Tag");
		response.put("Source", this.plane.getSource());
		response.put("Desc", this.tag.getText());
		response.put("Size", this.plane.getChunkWidth());
		response.put("Area", this.selectArea.getSelectedChunks());
		
		return response;
	}
	
	/*Highlight the playplane*/
	public void highlightPlayplane(int[] indexes)
	{
		this.playPlane.setHighlight(indexes);
		this.playPlane.highLightPlane();
		this.selectArea.setHighlight(indexes);
		this.selectArea.highLightPlane();
	}
	
	/*Set desc to the tag*/
	public void setTag(String desc)
	{
		this.tag.setForeground(Color.WHITE);
		this.tag.setText(desc);
	}
	
	public void setTargetPattern(int[] indexes)
	{
		int[][] selectedMatrix = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				selectedMatrix[i][j] = indexes[i * cols + j];
			}
		}
		
		this.plane.setTargetPattern(selectedMatrix);
	}
	
	public void setOver(boolean flag)
	{
		this.isOver = flag;
	}
	
	/************************************************************************************* flags below *********/
	public boolean hasLose(){
		if(this.isTimeUp()||this.isWrongMatch){
			return true;
		}
		return false;
	}
	
	public boolean hasOver()
	{
		return this.isOver;
	}
	
	public boolean hasWon() {
		return this.winFlag;
	}
	
	public boolean hasDonePlay(){
		return playPlane.hasDonePlay();
	}
	
	public boolean hasDoneRobotSummonAnimation() {
		return this.doneRobotSummonAnimationFlag;
	}
	
	public boolean isTimeUp(){
		if(time<0){this.isTimeUPFlag=true;}
		return this.isTimeUPFlag;
	}
	

	//the animation frame that makes left eye keep randomly changing its order and then, fixed!
	public void robotSummonAnimationFrame(){
		this.plane.randomImgPosition();
		playPlane.rearrangePlane();
		playPlane.validate();
	}
	
	public void robotBlinkAnimationFrame(boolean flag)
	{
		this.playPlane.blinkPlane(flag);
		this.playPlane.validate();
	}
	
	
	/** public void timeRunning()
	 * 
	 * 	a method that managing the playing time of left part
	 *  @param none
	 * */
	/*
	 * your are asked to implement your timeRunning method
	 *    
	 * */
	public void timeRunning()
	{
		try {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					time -= 1;
					timeBar.setValue(time);
				}

			});
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/*when user finished playing the puzzle*/
	public boolean submitStage(){
		verifier.setAnswer(this.plane.getTargetPattern());
		if(verifier.puzzleMatch(this.plane.getImgPosition())){
			this.winFlag=true;
		}
		else{
			this.isWrongMatch=true;
		}
		
		return this.winFlag;
	}

}