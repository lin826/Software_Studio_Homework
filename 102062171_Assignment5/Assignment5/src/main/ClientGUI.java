package main;
import game.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import network.GameClient;
import network.GameClientEventListener;

import org.json.JSONException;
import org.json.JSONObject;


public class ClientGUI extends JFrame implements ActionListener ,GameClientEventListener{
	
	//Binding client
	final GameClient client;
	
	//Game side (Tag/Ans)
	private String side;
	
	//GUI Components
	final int width = 400 , height = 400;
	private JTextArea monitor;
	private JTextField edtAddr;
	private JTextField edtPort;
	private JButton btnLogin;
	
	public ClientGUI(GameClient client)
	{
		super("GameClient");
		this.client = client;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width , height);
		this.setLayout(new FlowLayout());
		
		this.monitor = new JTextArea(18,35);
		this.monitor.setLineWrap(true);
		this.monitor.setWrapStyleWord(true);
		this.monitor.setEditable(false);
		
		this.edtAddr = new JTextField(10);
		this.edtAddr.setText("127.0.0.1");
		this.edtPort = new JTextField(10);
		this.edtPort.setText("8080");
		
		this.btnLogin = new JButton("Login");
		this.btnLogin.addActionListener(this);
		
		this.add(new JScrollPane(monitor));
		this.add(new JLabel("Server IP"));
		this.add(edtAddr);
		this.add(new JLabel("Server Port"));
		this.add(edtPort);
		this.add(btnLogin);
		
		this.setVisible(true);
	}
		
	/*Put message into the monitor*/
	public void putMessage(final String msg)
	{
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				monitor.append(msg + "\n");
			}
			
		});
	}
	
	/*Called when pressed the login button*/
	public void clientLogin()
	{
		String addr = this.edtAddr.getText();
		String port = this.edtPort.getText();
		
		if(!addr.equals("") && !port.equals("")){
			try {
				this.client.connect(addr);
				this.btnLogin.setEnabled(false);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/*Called when receive init message*/
	public void init(boolean flag)
	{
		if(flag){
			this.putMessage("Players matched !");
		}else{
			this.putMessage("Wait for another player ...");
		}
	}
	
	/*Called when receive side message*/
	public void receiveSide(String val)
	{
		this.side = val;
		this.putMessage("Here is " + val + " side.");
	}
	
	/*Called when receive start message*/
	public void receiveStart()
	{
		this.putMessage("Game is ready to start !");
		this.client.sendReady();
		try {
			Game game = new Game(this.client , this.side);
			this.client.addEventListener(game);
			game.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("clientStart(): failed to initialize the game");
			e.printStackTrace();
		}
	}
	
	/*Called when receive stage index*/
	public void receiveStageIndex(int index)
	{
		this.putMessage("Stage : " + index);
	}
	
	/*Called when receive control message*/
	public void receiveControl(boolean flag)
	{
		this.putMessage("Control state: " + flag);
	}
	
	/*Called when receive answer message*/
	public void receieveAnswer(boolean flag)
	{
		this.putMessage("Result: " + flag);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					client.sendReady();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
	}
	
	@Override
	public void onServerConnected() {
		// TODO Auto-generated method stub
		this.putMessage("Server Connected !");
	}
	
	@Override
	public void onReceiveMessage(String msg) {
		// TODO Auto-generated method stub
		try{
			JSONObject response = new JSONObject(msg);
			String type = (String) response.get("Type");

			//Receive the initial state of server
			if(type.equals("Init")){
				this.init(response.getBoolean("Value"));
			}
			//Start the game
			else if(type.equals("Start")){
				this.receiveStart();
			}
			//Set the client's side
			else if(type.equals("Side")){
				this.receiveSide((String) response.get("Value"));
			}
			//Set the client's stage index
			else if(type.equals("Stage")){
				this.receiveStageIndex(response.getInt("Value"));
			}
			//Set the client control
			else if(type.equals("Control")){
				this.receiveControl(response.getBoolean("Value"));
			}
			//Set the client result and send ready for the next
			else if(type.equals("Ans")){
				this.receieveAnswer(response.getBoolean("Value"));
			}
			else{
				this.putMessage(msg);
			}
		}catch(JSONException e){
			this.putMessage(msg);
		}

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.btnLogin){
			this.clientLogin();
		}
	}
}
