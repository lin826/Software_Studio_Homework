package GameForP1;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import Game.ImgPlane;
import Game.TimeBar;

public class GameStage extends JPanel {

	private int rows;
	private int cols;
	private int time = 2000;

	private Rectangle bounds;
	private ImgPlane plane;
	private JTextArea tag;
	private JLabel stageTag;
	//private Verifier verifier;

	private SelectArea selectArea;
	private PlayPlane playPlane;
	private TimeBar timeBar;

	private boolean winFlag;
	private boolean doneRobotSummonAnimationFlag;
	private boolean isTimeUPFlag;
	private boolean isWrongMatch;

	/**
	 * Constructor GameStage(Rectangle bounds, ImgPlane plane, int rows, int
	 * cols) Generate a GameStage, initialize all the instance.
	 * 
	 * @param Rectangle
	 *            bounds - the size of image chunk
	 * @param ImgPlane
	 *            plane - the select image
	 * @param int rows - number of rows
	 * @param int cols - number of columns
	 * */
	public GameStage(Rectangle bounds, ImgPlane plane, int rows, int cols,
			int stageNumber) {
		this.setWinFlag(false);
		this.doneRobotSummonAnimationFlag = false;
		this.isTimeUPFlag = false;
		this.setWrongMatch(false);

		this.rows = rows;
		this.cols = cols;
		this.plane = plane;
		this.bounds = bounds;

		this.setLayout(null);
		this.setBounds(50, 0, this.bounds.width, this.bounds.height);
		Random Rcolor = new Random();
		this.setBackground(new Color(252, 200 + Rcolor.nextInt(40), 60 + Rcolor
				.nextInt(100)));
		this.setVisible(true);

		this.tag = new JTextArea();
		this.tag.getFocusAccelerator();
		this.stageTag = new JLabel("No." + stageNumber);
		this.selectArea = new SelectArea(plane, rows, cols, this.bounds);
		this.playPlane = new PlayPlane(plane, rows, cols);
		this.timeBar = new TimeBar();

		this.initTag();
		this.initStageTag();

		this.playPlane.setBounds((this.getWidth() / 2 - plane.getChunkWidth()
				* cols) / 2, 60, plane.getChunkWidth() * (cols),
				plane.getChunkHeight() * (rows));

		this.timeBar.setBounds(50, 700, this.getWidth() - 100, 50);
		this.timeBar.setValue(time);
		this.timeBar.setOpaque(true);

		this.add(tag);
		this.add(stageTag);
		this.add(selectArea);
		this.add(playPlane);
		this.add(timeBar);

	}


	private void initTag() {
		this.tag.setFont(new Font("Courier", Font.PLAIN, 20));
		this.tag.setOpaque(true);
		this.tag.setBounds(this.getWidth() / 4 * 3 - 70, 10, 150, 25);
	}

	private void initStageTag() {
		this.stageTag.setBackground(Color.gray);
		this.stageTag.setOpaque(true);
		this.stageTag.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.stageTag.setBounds(this.getWidth() - 70, 0, 40, 20);

	}

	/************************************************************************************* flags below *********/
	public boolean hasLose() {
		if (this.isTimeUp() || this.isWrongMatch()) {
			return true;
		}
		return false;
	}

	public boolean hasWon() {
		return this.isWinFlag();
	}

	public boolean hasDonePlay() {
		return playPlane.hasDonePlay();
	}

	public boolean hasDoneRobotSummonAnimation() {
		return this.doneRobotSummonAnimationFlag;
	}

	public boolean isTimeUp() {
		if (time < 0) {
			this.isTimeUPFlag = true;
		}
		return this.isTimeUPFlag;
	}

	// the animation frame that makes left eye keep randomly changing its order
	// and then, fixed!
	public void robotSummonAnimationFrame() {
		this.plane.randomImgPosition();
		playPlane.rearrangePlane();
		playPlane.validate();
	}

	/**
	 * public void timeRunning()
	 * 
	 * a method that managing the playing time of left part
	 * 
	 * @param none
	 * */
	/*
	 * your are asked to implement your timeRunning method
	 */
	public void timeRunning() {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					time = timeBar.getValue();
					if (time >= 0) {
						timeBar.setValue(--time);
					}
				}
			});
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isWinFlag() {
		return winFlag;
	}

	public void setWinFlag(boolean winFlag) {
		this.winFlag = winFlag;
	}

	public boolean isWrongMatch() {
		return isWrongMatch;
	}

	public void setWrongMatch(boolean isWrongMatch) {
		this.isWrongMatch = isWrongMatch;
	}

	public String getTagText() {
		return tag.getText();
	}

	public void setTag(JTextArea tag) {
		this.tag = tag;
	}

	public int[] getselectedChunks() {
		return this.selectArea.getSelectedChunks();
	}
	
	public boolean IsSubmit() {
		return this.selectArea.isSubmit();
	}


}