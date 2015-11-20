package GameForP1;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import Connect.Login;
import GameForP1.GameStage;
import Game.ImgPlane;

public class GameForP1 extends JFrame implements Runnable {
	private Rectangle bounds = new Rectangle(1300, 800);
	private Rectangle stageBounds = new Rectangle(1250, 800);
	private int rows = 4;
	private int cols = 4;
	private int lives = 3;
	private int stageNumber = 0;

	private Thread gameThread = null;

	private GameStage currentStage;
	private Queue<GameStage> stages = new LinkedList<GameStage>();
	private JLabel gameOver;
	private JPanel lifePanel;
	private List<JLabel> lifeLabels = new ArrayList<JLabel>();
	private Queue<ImgPlane> currentModel = new LinkedList<ImgPlane>();
	public String tagName = "";
	private int[] chunks;
	private boolean submitFlag = false;
	private String sourceName;

	public GameForP1() throws IOException {
		super();
		this.setVisible(false);
		this.setBounds(this.bounds);
		this.setLayout(null);
		lifePanel = new JPanel();
		initLifePanel();
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//this.nextStage();
	}
	
/*	public void setAll(ImgPlane inputModel) {
		setCurrentModel(inputModel);

	}
*/
	private void initLifePanel() {
		this.lifePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
		this.lifePanel.setBounds(0, 0, 50, 800);
		this.lifePanel.setVisible(true);
		this.lifePanel.setBackground(Color.orange);
		this.lifePanel.setOpaque(true);
		this.lifePanel.removeAll();
		for (int i = 0; i < this.lives; ++i) {
			JLabel robotLife = new JLabel(new ImageIcon(
					"res/robot_head-128.png"));
			robotLife.setBounds(0, i * 50, 50, 50);
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

	public void nextStage() {
		this.addStage();
		this.currentStage = this.stages.poll();
		this.initLifePanel();
		this.add(currentStage);
		this.currentStage.requestFocus();
		this.tagName = "";
		submitFlag = false;
	}

	private void addStage() {
		ImgPlane tempModel = null;
		while(tempModel==null){
			tempModel = this.getCurrentModel();
		}
		GameStage stage = new GameStage(stageBounds, tempModel, rows, cols, ++stageNumber);
		stage.setVisible(true);
		stages.add(stage);

	}

	private void gameOver() {
		this.initLifePanel();
		this.gameOver = new JLabel(new ImageIcon("res/game_over.png"));
		this.gameOver.setBounds(this.bounds);
		this.gameOver.setBackground(Color.black);
		this.gameOver.setOpaque(true);
		this.setVisible(true);
		this.add(gameOver);
		this.repaint();
		System.out.println("GAME OVER");
	}

	@Override
	public void run() {
		while (Thread.currentThread() == this.gameThread) {
			try {
				if (this.currentStage.hasWon()) // this stage win
				{
					System.out.println("currentStage.hasWon()");
					int round = 0;
					while (round < 5) {
						this.currentStage.robotSummonAnimationFrame();
						Thread.sleep(100);
						round++;
					}
					this.remove(this.currentStage);
					if (this.rows <= 10) {
						this.rows++;
						this.cols++;
					}
					Thread.sleep(1000);
					this.nextStage();
				} else if (this.currentStage.hasLose()) // this stage lose
				{
					this.lives--;
					int round = 0;
					while (round < 5) {
						this.currentStage.robotSummonAnimationFrame();
						Thread.sleep(100);
						round++;
					}
					this.remove(currentStage);
					this.rows--;
					this.cols--;
					Thread.sleep(1000);
					if (lives > 0)
						this.nextStage(); // if there is still another stage
				}
				// if the player finished the stage determined whether the result is true
				else if (this.currentStage.IsSubmit()) { 
					this.tagName = this.currentStage.getTagText();
					this.chunks = (this.currentStage.getselectedChunks());
					if(!submitFlag)
					{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("Source Name", sourceName);
					map.put("Size", rows*cols);
					map.put("Chosen Area", Arrays.toString(chunks));
					map.put("Description", tagName);
					JSONObject jsonObjectOutput = new JSONObject(map);
					File log = new File("Output.txt");
					PrintWriter out = new PrintWriter(new FileWriter(log, true));
					out.append(jsonObjectOutput+"\n");
					out.close();
					}
				}
				else if (!this.currentStage.isTimeUp() && !this.currentStage.IsSubmit()) {
					this.currentStage.timeRunning();
				}

				if (this.lives <= 0) {
					this.gameOver();
				}
				this.repaint();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public void hasWon() {
		this.currentStage.setWinFlag(true);
	}

	public void hasLose() {
		this.currentStage.setWinFlag(false);
		this.currentStage.setWrongMatch(true);
	}

	public ImgPlane getCurrentModel() {
		return this.currentModel.peek();
	}

	public void setCurrentModel(ImgPlane currentModel) {
		this.currentModel.add(currentModel);
	}
	
	public void createCurrentModel(String name) {
		sourceName = name;
		ImgPlane newModel = new ImgPlane("res/assignment4TestData/"+name,this.rows,this.cols);
		this.setCurrentModel(newModel);
	}

	public int[] getChunks() {
		return this.chunks;
	}

	public boolean hasTagName() {
		if(this.tagName.equals(""))
			return false;
		else
			return true;
	}

	public boolean isSubmitFlag() {
		return submitFlag;
	}

	public void setSubmitFlag(boolean submitFlag) {
		this.submitFlag = submitFlag;
	}

}
