package game;

/**
 * The class "GameStage" is an object of an individual stage, several stages will
 * establish a game.
 * All of the method in this class is related to generate UI we use
 * 
 * */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.AbstractSentence;

public class GameStage extends JPanel implements KeyListener {
	/*
	 * While "Keyboard" is no more than a recommend gaming method, others are
	 * available and you can implement yours.
	 */
	private List<String> chineseSpans = null; //�U��r��ǦC
	private List<String> determinedSpans = new ArrayList<String>(); //�W��r��ǦC
	private Rectangle bounds;
	private Wheel wheelLabel;
	private JLabel engLabel = new JLabel();
	private JPanel wordsPanel = new JPanel(); //�U��Spans��Panel
	private JPanel determinedWordsPanel = new JPanel(); //�W��Spans��Panel
	List<JLabel> answers = new ArrayList<JLabel>();
	List<JLabel> rndChiSpans = new ArrayList<JLabel>();

	private int level = 0;
	private int cursorIndex = 0; // �ثe��m
	private int stageScore = 0;
	private AbstractSentence sentence;

	/* We provided some basic things, you can add any other things here. */
	public GameStage(Rectangle bounds, AbstractSentence sentence, int level) {

		this.level = level;
		this.bounds = bounds;
		this.sentence = sentence;

		this.setFocusable(true);
		this.setBounds(bounds);
		this.setBackground(Color.GRAY);
		this.setPreferredSize(new Dimension(bounds.width, bounds.height - 50));
		this.setLayout(null);

		addKeyListener(this);

		this.initWordspans();
		this.initWheel();
		this.paintImmediately(bounds);
	}

	public int getLevel() {
		return this.level;
	}

	private void initWheel() {
		this.wheelLabel = new Wheel(this.bounds);
		this.wheelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(wheelLabel);
	}

	private void initWordspans() {

		this.initDeterminedWordsPanel();

		this.chineseSpans = this.sentence
				.getRandomizedChineseSpansWithLevel(this.level);
		wordsPanel.removeAll();
		wordsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
		wordsPanel.setBackground(Color.GRAY);
		for(int i = 0 , lastX = 10 ; i < chineseSpans.size() ; i++){
			JLabel l = new JLabel(chineseSpans.get(i));
			l.setBorder(new EmptyBorder(5 , 5 , 5 , 5));
			l.setOpaque(true);
			l.setBounds(new Rectangle(l.getPreferredSize()));
			l.setLocation(lastX , 600);
			l.setFont(new Font("�L�n������", Font.PLAIN, 20));
			this.wordsPanel.add(l);
			this.rndChiSpans.add(l);
			lastX += l.getWidth() + 10;
		}
		this.setColor();
		this.selectSpan(this.cursorIndex);
		wordsPanel.setBounds(bounds.width, 40, bounds.width, 30);
		wordsPanel.setBorder(new EmptyBorder(0, 10, 10, 10)); // �]�w�d��
		wordsPanel.setOpaque(true); // �]�w�e�X�C�ӹ���
		wordsPanel.setBackground(Color.GRAY); // �]�w�I���C��
		wordsPanel.setBounds(new Rectangle(this.wordsPanel.getPreferredSize())); // �]�w�j�p
		wordsPanel.setLocation(10, 500); // �]�w��m
		this.add(wordsPanel);
	}

	private void setColor() {
		for(int i = 0 ; i < this.rndChiSpans.size() ; i++){
			if(i == this.cursorIndex) {
				this.rndChiSpans.get(i).setBackground(Color.orange);
				this.rndChiSpans.get(i).setForeground(Color.white);
			}
			else {
				this.rndChiSpans.get(i).setBackground(Color.yellow);
				this.rndChiSpans.get(i).setForeground(Color.black);
			}
		}
	}

	/* Initiate determinedWordsPanel, you can add something here if needed. */
	private void initDeterminedWordsPanel() {

		this.engLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.engLabel.setText(this.sentence.getEngSentence());
		this.engLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		this.engLabel.setForeground(Color.white);
		this.engLabel.setBorder(new EmptyBorder(10, 10, 10, 10)); // �]�w�d��
		this.engLabel.setOpaque(true); // �]�w�e�X�C�ӹ���
		this.engLabel.setBackground(Color.GRAY); // �]�w�I���C��
		this.engLabel.setForeground(Color.WHITE); // �]�w�e���C��]��r�C��
		this.engLabel
				.setBounds(new Rectangle(this.engLabel.getPreferredSize())); // �]�w�j�p
		this.engLabel.setLocation(10, 0); // �]�w��m
		this.add(engLabel);
		
		this.determinedWordsPanel.removeAll();
		this.determinedWordsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.determinedWordsPanel.setLayout(new FlowLayout(FlowLayout.LEADING,
				10, 10));
		// String spans = "����½Ķ";
		for (String spans : determinedSpans) {
			JLabel spansLabel = new JLabel(spans);
			spansLabel.setBorder(new EmptyBorder(10, 0, 0, 0)); // �]�w�d��
			spansLabel.setOpaque(true); // �]�w�e�X�C�ӹ���
			spansLabel.setBackground(Color.GRAY); // �]�w�I���C��
			spansLabel.setForeground(Color.WHITE); // �]�w�e���C��]��r�C��
			spansLabel.setBounds(new Rectangle(spansLabel.getPreferredSize())); // �]�w�j�p
			spansLabel.setLocation(0, 0); // �]�w��m
			spansLabel.setFont(new Font("�L�n������", Font.PLAIN, 20));
			Font font = spansLabel.getFont();
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
			spansLabel.setFont(font.deriveFont(attributes));
			this.determinedWordsPanel.add(spansLabel);
		}
		this.determinedWordsPanel.setBounds(bounds.width, 40, bounds.width, 30);
		this.determinedWordsPanel.setForeground(Color.white);
		this.determinedWordsPanel.setBorder(new EmptyBorder(0, 10, 10, 10)); // �]�w�d��
		this.determinedWordsPanel.setOpaque(true); // �]�w�e�X�C�ӹ���
		this.determinedWordsPanel.setBackground(Color.GRAY); // �]�w�I���C��
		this.determinedWordsPanel.setForeground(Color.WHITE); // �]�w�e���C��]��r�C��
		this.determinedWordsPanel.setBounds(new Rectangle(
				this.determinedWordsPanel.getPreferredSize())); // �]�w�j�p
		this.determinedWordsPanel.setLocation(0, 20); // �]�w��m
		this.add(determinedWordsPanel);
	}

	/* You can add something here if needed. */
	private void restart() {
		this.determinedWordsPanel.removeAll();
		this.wordsPanel.removeAll();
		this.rndChiSpans.clear();
		this.determinedSpans.clear();
		this.cursorIndex = 0;
		initWordspans();
	}

	private void selectSpan(int spanIndex) {

	}

	/**
	 * void confirmSpan(int spanIndex) The action to do when a span is being
	 * sent to the determined area.
	 **/
	private void confirmSpan(int spanIndex) {
		this.determinedSpans.add(this.chineseSpans.get(spanIndex));
		JLabel spansLabel= new JLabel(this.chineseSpans.get(spanIndex));
		this.determinedWordsPanel.add(spansLabel);
		this.wordsPanel.remove(spanIndex);
		this.chineseSpans.remove(spanIndex);
		this.rndChiSpans.remove(spanIndex);
		this.setColor();
		//System.out.println(this.chineseSpans.get(spanIndex));
	}
	private void submit(int level) {
		for(int i = 0 ; i < determinedWordsPanel.getComponentCount() ; i++){
//			JLabel lb = determinedWordsPanel.get
//			this.answers.add(index, element);
		}
		
		
		this.level++;
	}

	/*
	 * It's a action that when a sentence has been submit,a window will pop out
	 * to ask the player to confirm if he/she really want to submit.
	 */
	private int confirmSubmit() {
		String[] options = { "Cancel", "Yes" };
		int n = JOptionPane.showOptionDialog(this, "Are you sure?",
				"Confirmation", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if(n==1)
			submit(level);
		return n;
	}

	/*
	 * Make the frame of "open door animation" While animation consist of many
	 * static pictures, that we usually call it as frame. You may implement
	 * procedures that generates individual frames of moving door
	 * 
	 * If it's successfully called by run() in game.java, there must have
	 * desired animation
	 */
	public void openDoor() {
	}

	public int getStageScore() {
		return this.stageScore;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			shifLeft();
			System.out.println(this.cursorIndex);
			for (String span : this.chineseSpans)
				this.setColor();
			break;
		case KeyEvent.VK_RIGHT:
			shifRight();
			System.out.println(this.cursorIndex);
			for (String span : this.chineseSpans)
				this.setColor();
			break;
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_ESCAPE:
			restart();
		default:
			break;
		}
	}

	private void select() {
		confirmSpan(this.cursorIndex);
		initDeterminedWordsPanel();
		if(this.chineseSpans.size()==0)
			confirmSubmit();
		else
			selectSpan(this.cursorIndex);
	}

	public void shifRight() {
		wheelLabel.rotateClockwise();
		this.cursorIndex = (this.cursorIndex + 1) % this.chineseSpans.size();
	}

	public void shifLeft() {
		wheelLabel.rotateCounterClockwise();
		this.cursorIndex = (this.cursorIndex > 0) ? this.cursorIndex - 1 : this.chineseSpans.size() - 1;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
