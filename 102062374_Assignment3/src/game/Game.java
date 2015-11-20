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

	private final static int STAGE_AMOUNT = 3; // ���� (member) �ŧi�ɭY�ϥ�����r final
												// �׹��A��ܸӦ������`�� (constant) �A�ݩ�
												// (field) �ΰϰ��ܼ� (local
												// variable) �����୫�s���ȡA�Ӥ�k
												// (method) �~�� (inherit) �ᤣ�i�Q��g
												// (overload) �C
	private Rectangle bounds = new Rectangle(1000, 700); // �غc�@�ӷs��
															// Rectangle�A�䥪�W�������Ь�
															// (0,0)�A��e�שM���ץѦP�W���Ѽƫ��w�C
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
		this.setBounds(this.bounds); // �]�m�� Rectangle ����� Rectangle�A�H�����w��
										// Rectangle�C
		this.setLayout(null); // ���]�w�ƪ��޲z���A���� setLayout() ���]�w�� null
								// �A�M��ۦ�]�w�������������y�СC
								// http://pydoing.blogspot.tw/2012/06/java-api-awt-layout.html
		this.setResizable(false); // ���i�ѨϥΪ̧��j�p
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �p�G�S���[�o��A�������M�i�H����
																// (���ñ�)�A���{�Ǥ��M�|�b�I�����S���פ�A�{����ڤW�S�������C
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
			this.gameThread = new Thread(this); // ����Thread����
		}
		this.gameThread.start(); // �}�l����gameThread.run()
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
				 * if() { //if the player pass the stage //���X�T�{���� } else { //if
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
