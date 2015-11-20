package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle; //http://yowlab.shps.kh.edu.tw/javadocs/java/awt/Rectangle.html
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList; //http://emn178.pixnet.net/blog/post/93557502
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;

import model.Model;
import model.Sentence;

public class Game extends JFrame implements Runnable {

	private final static int STAGE_AMOUNT = 3; // 成員 (member) 宣告時若使用關鍵字 final
												// 修飾，表示該成員為常數 (constant) ，屬性
												// (field) 或區域變數 (local
												// variable) 都不能重新給值，而方法
												// (method) 繼承 (inherit) 後不可被改寫
												// (overload) 。
	private Rectangle bounds = new Rectangle(1000, 700); // 建構一個新的
															// Rectangle，其左上角的坐標為
															// (0,0)，其寬度和高度由同名的參數指定。
	private int maxLife = 3;
	int score = 0;
	private Topbar topbar; // JPanel
	private Thread gameThread = null;
	private final static int DELAY = 20;
	private Model model = new Model();
	private GameStage currentStage;
	/* Create a Queue called "stages" to put game stage in */
	private Queue<GameStage> stages = new LinkedList<GameStage>();

	/* Initialize everything you need in this constructor. */
	public Game() {
		super();
		this.setVisible(false);
		this.setBounds(this.bounds); // 設置此 Rectangle 的邊界 Rectangle，以比對指定的
										// Rectangle。
		this.setLayout(null); // 不設定排版管理員，此時 setLayout() 須設定成 null
								// ，然後自行設定元件於視窗中的座標。
								// http://pydoing.blogspot.tw/2012/06/java-api-awt-layout.html
		this.setResizable(false); // 不可由使用者更改大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 如果沒有加這行，視窗仍然可以關閉
																// (隱藏掉)，但程序仍然會在背景中沒有終止，程式實際上沒有完結。
		this.produceStages(STAGE_AMOUNT);
	}

	public void start() {

		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.topbar = new Topbar(this, new Rectangle(bounds.width, 40),
				maxLife, score);
		this.add(topbar);
		nextStage();
		this.add(currentStage);
		this.setVisible(true);
		
		
		if (this.gameThread == null) {
			this.gameThread = new Thread(this); // 產生Thread物件
		}
		this.gameThread.start(); // 開始執行gameThread.run()
		// this.setScore(10);

	}

	private void nextStage() {
		this.currentStage = this.stages.poll();
		this.currentStage.requestFocus();
	}

	public void setScore(int val) {
		this.score = val;
	}

	/**
	 * void produceStages(int num) Generate the stages before we start gaming.
	 * 
	 * @input num: number of stages that user wants to generate
	 * 
	 * */
	private void produceStages(int num) {
		try {
			Model.buildSentenceElementList();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Sentence> sentenceList = Model.shuffleSentenceList();
		System.out.println("\n\n");
		for (int i = 0; i < num; i++) {
			System.out.println(sentenceList.get(i).getEngSentence());
			GameStage e = new GameStage(bounds, sentenceList.get(i), i);
			this.stages.offer(e);
		}
	}

	@Override
	public void run() {
		long t = System.currentTimeMillis();
		while (Thread.currentThread() == this.gameThread) {
			try {
				for (int i = 0; i < 3; i++) {
					this.score += 10;
					this.topbar = new Topbar(this, new Rectangle(bounds.width,
							40), maxLife, score);
					this.topbar.repaint();
				}
				/*
				 * Something is missing!!
				 * 
				 * Add your code to finish remaining methods that control the
				 * game process, such as checking whether the player pass the
				 * stage or not, win the game or not etc.
				 */
				if (stages.size() != this.currentStage.getLevel()) // win the
																	// game or
																	// not
				/*
				 * if() { //if the player pass the stage //跳出確認視窗 } else { //if
				 * the player doesn't pass the stage
				 * 
				 * }
				 */
					this.repaint();
				t += DELAY;
				Thread.sleep(Math.max(0, t - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				break;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}
}
