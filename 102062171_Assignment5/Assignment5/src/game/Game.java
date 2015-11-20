package game;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import network.GameClient;
import network.GameClientEventListener;


public class Game extends JFrame implements Runnable , GameClientEventListener{
	
	//Binding client
	final GameClient client;
	
	//Tag side or Ans side
	private String side;
	private boolean isOver;
	
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

	
	public Game(GameClient client , String side) throws IOException {
		super();
		this.client = client;
		this.side = side;
		this.isOver = false;
		this.setVisible(false);
		this.setBounds(this.bounds);
		this.setLayout(null);
		lifePanel = new JPanel();
		initLifePanel();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.model = new Model("res/assignment4TestData/assignment4TestData.txt");
		
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
	}
	
	private void addStage(int index){
		GameStage stage = new GameStage(this , stageBounds, model.getPlaneByIndex(index , this.rows, this.cols), rows, cols,++stageNumber);
		stage.setVisible(true);
		stages.add(stage);
	}
	
	private void gameOver(){
		this.remove(this.currentStage);
		this.isOver = true;
		this.gameOver = new JLabel(new ImageIcon("res/game_over.png"));
		this.gameOver.setBounds(this.bounds);
		this.gameOver.setBackground(Color.black);
		this.gameOver.setOpaque(true);
		this.setVisible(true);
		this.add(gameOver);
		this.repaint();
		this.revalidate();
	}

	public void decreaseLife()
	{
		this.lives--;
		JLabel lifeLabel = this.lifeLabels.get(this.lives);
		lifeLabel.setVisible(false);
		this.lifePanel.revalidate();
	}
	
	/*Transition between stage*/
	public void transition(final boolean result)
	{
		new Thread(new Runnable(){
			@Override
			public void run() {
				if(result)
					winAnimation();
				else
					loseAnimation();
				sendReady();
			}
		}).start();;
	}
	
	public void loseAnimation()
	{
		try {
			int round = 0;
			while(!currentStage.hasDoneRobotSummonAnimation() && round < 5){
				currentStage.robotSummonAnimationFrame();
				round++;
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void winAnimation()
	{
		try {
			for(int i = 0 ; i < 5 ; i++){
				boolean flag = (i % 2 == 0) ? true : false;
				this.currentStage.robotBlinkAnimationFrame(flag);
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
	
	/*Process the final result*/
	public void processResult(boolean result)
	{
		//Mark current stage is over so the time's up over condition will be process only once
		this.currentStage.setOver(true);
		if(result){
			this.difficultyUp();
		}else{
			this.decreaseLife();
			this.difficultyDown();
		}
		this.transition(result); 
	}
	
	@Override
	public void run() {
		long t = System.currentTimeMillis();
		while (Thread.currentThread() == this.gameThread) {
            try {
            	if(this.currentStage != null){
            		//Time's up
            		if(this.currentStage.isTimeUp() && !this.currentStage.hasOver()){
            			this.processResult(false);
            		}
            		//Time running
            		else{
            			this.currentStage.timeRunning();
            		}
            		
            		//Game over
            		if(this.lives <= 0 && !this.isOver){
            			this.gameOver();
            		}
            		
            		this.currentStage.repaint();
            	}
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
	
	/*Called when receive stage index message*/
	private void receiveStageIndex(int index)
	{
		if(this.currentStage != null)
			this.remove(this.currentStage);
		this.addStage(index);
		this.nextStage();
	}
	
	/*Called when receive control message*/
	private void receiveControl(boolean flag)
	{
		if(this.side.equals("Tag")){
			this.currentStage.setSelectareaEnabled(flag);
		}
		else if(this.side.equals("Ans")){
			this.currentStage.setPlayplaneEnabled(flag);
		}
	}
	
	/*Called when receive tag message*/
	private void receiveTag(JSONObject response)
	{
		String desc = response.getString("Desc");
		JSONArray area = response.getJSONArray("Area");
		
		int[] indexes =  new int[area.length()];
		for(int i = 0 ; i < area.length() ; i++)
			indexes[i] = area.getInt(i);
		
		this.currentStage.highlightPlayplane(indexes);
		this.currentStage.setTag(desc);
		this.currentStage.setTargetPattern(indexes);
	}
	
	/*Called when receive answer message*/
	private void receiveAnswer(JSONObject response)
	{
		boolean result = response.getBoolean("Value");
		this.processResult(result);
	}
	
	/*Called when send tag info*/
	public void sendTag()
	{
		this.client.sendTag(this.currentStage.getTaginfo());
	}
	
	/*Called when send Result*/
	public void sendAnswer()
	{
		boolean result = this.currentStage.submitStage();
		this.client.sendAnswer(result);
		this.processResult(result);
	}
	
	/*Called when send ready signal*/
	public void sendReady()
	{
		this.client.sendReady();
	}
	
	/*Called when send end signal*/
	public void sendEnd()
	{
		this.client.sendEnd();
	}
	
	@Override
	public void onServerConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveMessage(String msg) {
		try{
			JSONObject response = new JSONObject(msg);
			String type = (String) response.get("Type");

			//Set the client's stage index
			if(type.equals("Stage")){
				this.receiveStageIndex(response.getInt("Value"));
			}
			//Set the client control
			else if(type.equals("Control")){
				this.receiveControl(response.getBoolean("Value"));
			}
			//Set the client's tag info (for answer side
			else if(type.equals("Tag")){
				this.receiveTag(response);
			}
			//Set the client's answer (for tag side
			else if(type.equals("Ans")){
				this.receiveAnswer(response);
			}
			else{
				System.out.println(msg);
			}
		}catch(JSONException e){
			System.out.println(msg);
		}
		
	}
	
	
}
