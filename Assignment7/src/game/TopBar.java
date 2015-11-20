package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import processing.core.PApplet;

public class TopBar extends JPanel implements TopBarDelegate{
	
	private JLabel label;
	
	public TopBar(Rectangle bounds){
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setBounds(bounds);
		this.setBackground(new Color(63,90,108));
		this.initComponents();
		this.setVisible(true);
		this.requestFocus();
	}
	
	/**
	 * A method for initializing what top bar contains.
	 */
	private void initComponents(){
		this.label = new JLabel("Rock count: 0, Height: 0");
		this.label.setFont(new Font("Arial", 0, 30));
		this.label.setForeground(new Color(222,217,214));
		this.add(label);
	}
	
	// To print information of Rock count and Height on TopBar
	@Override
	public void setLabel(int rockCount, float height) {
		this.label.setText("Rock count: "+rockCount+", Height: "+height);		
	}
	
}
