

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Game extends JFrame implements Runnable {
	private Rectangle bounds = new Rectangle(1300,800);
	private Rectangle stageBounds = new Rectangle(1250,800);
	private int rows = 4;
	private int cols = 4;
	private int lives =3;
	private int stageNumber =0;
	
	private Thread gameThread = null;
	
	private Model model;
	private GameStage currentStage;
	private Queue<GameStage> stages = new LinkedList<GameStage>();
	private JLabel gameOver;
	private JPanel lifePanel;
	private List<JLabel> lifeLabels = new ArrayList<JLabel>();

	
	public Game() throws IOException {
		super();
		this.setVisible(false);
		this.setBounds(this.bounds);
		this.setLayout(null);
		lifePanel = new JPanel();
		initLifePanel();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.model = new Model("res/assignment4TestData/assignment4TestData.txt");
		
		this.addStage();
		this.nextStage();

	}
	
	private void initLifePanel(){
		this.lifePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,5));
		this.lifePanel.setBounds(0,0,50,800);
		this.lifePanel.setVisible(true);
		this.lifePanel.setBackground(Color.orange);
		this.lifePanel.setOpaque(true);
		this.lifePanel.removeAll();
		for(int i=0;i<this.lives;++i){
			JLabel robotLife = new JLabel(new ImageIcon("res/robot_head-128.png"));
			robotLife.setBounds(0, i*50, 50, 50);
			lifeLabels.add(robotLife);
			lifePanel.add(robotLife);
		}
		this.add(lifePanel);
	}
	
	public void start() {
		if (this.gameThread == null) {
            this.gameThread = new Thread(this);
        }
		this.gameThread.start();
		this.setVisible(true);
	}
	
	
	private void nextStage() {
		
		this.currentStage = this.stages.poll();
		this.add(currentStage);
		this.currentStage.requestFocus();
		this.addStage();
	}
	
	private void addStage(){
		GameStage stage = new GameStage(stageBounds, model.getRandomPlane(this.rows, this.cols), rows, cols,++stageNumber);
		stage.setVisible(true);
		stages.add(stage);
	}
	
	private void gameOver(){
		this.remove(this.currentStage);
		this.gameOver = new JLabel(new ImageIcon("res/game_over.png"));
		this.gameOver.setBounds(this.bounds);
		this.gameOver.setBackground(Color.black);
		this.gameOver.setOpaque(true);
		this.setVisible(true);
		this.add(gameOver);
		this.repaint();
		this.revalidate();
		System.out.println("GAME OVER");
	}

	public void decreaseLife()
	{
		this.lives--;
		JLabel lifeLabel = this.lifeLabels.get(this.lives);
		lifeLabel.setVisible(false);
		this.lifePanel.revalidate();
		System.out.println("Life: " + lives);
	}

	public void performAnimation()
	{
		try {
			int round = 0;
			while(!this.currentStage.hasDoneRobotSummonAnimation() && round < 5){
				this.currentStage.robotSummonAnimationFrame();
				round++;
				Thread.sleep(100);
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void difficultyUp()
	{
		this.rows++;
		this.cols++;
	}
	
	public void difficultyDown()
	{
		this.rows--;
		this.cols--;
	}
	
	@Override
	public void run() {
		long t = System.currentTimeMillis();
		while (Thread.currentThread() == this.gameThread) {
            try {
            	
          	  /*
               * 	Hint Pseudo code in try 
               *  if this stage lose
               *  	decrease a live one time
               *  	while Summon Animation is not performed and round < 5
               *  		perform some animation(optional)
               *  	remove current stage
               *  	lower the difficulty by decrease the number of rows and columns
               *  	if there is still another stage
               *  		get next stage
               *  else if this stage win
               *  	set higher difficulty by increase the number of rows and columns
               *  	get next stages
               *  else if time haven't expired
               *  	perform time running
               *  else 
               *  	nothing
               *  
               *  if there is no life 
               *  	game over
               *  
               *  perform repaint method and set some delay by thread sleep
               * */
            	
            	//Player lose
            	if(this.currentStage.hasLose()){
            		System.out.println("lose");
            		this.performAnimation();
            		this.remove(this.currentStage);
            		this.decreaseLife();
            		if(!this.stages.isEmpty()){
            			difficultyDown();
            			this.nextStage();
            		}
            	}
            	//Player won
            	else if(this.currentStage.hasWon()){
            		System.out.println("won");
            		this.performAnimation();
            		this.remove(this.currentStage);
            		if(!this.stages.isEmpty()){
               			difficultyUp();
            			this.nextStage();
            		}
            	}
            	//Submit stage
            	else if(this.currentStage.hasDonePlay()){
            		this.currentStage.submitStage();
            	}
            	else{
            		this.currentStage.timeRunning();
            	}
            	
            	//End judging
            	if(this.lives <= 0){
            		this.gameOver();
            	}
            	
            	this.currentStage.repaint();
            	Thread.sleep(20);
            }
            catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
	}

	
}
