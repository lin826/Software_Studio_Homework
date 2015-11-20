package main;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import network.Connection;
import network.GameServer;
import network.GameServerEventListener;


public class ServerGUI extends JFrame implements ActionListener	,GameServerEventListener{
	
	//Binding game server
	final private GameServer server;
	
	//GUI Components
	final int width = 400 , height = 400;
	private JTextArea monitor;
	private JButton btnExit;
	private JButton btnStart;
	
	//Log file
	private PrintWriter log;
	
	public ServerGUI(GameServer server) throws FileNotFoundException
	{
		super("GameServer");
		this.server = server;
		this.log = new PrintWriter(new FileOutputStream(new File("log.txt")));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width , height);
		this.setLayout(new FlowLayout());
		
		this.monitor = new JTextArea(20 , 35);
		this.monitor.setEditable(false);
		this.monitor.setLineWrap(true);
		this.monitor.setWrapStyleWord(true);
		this.btnExit = new JButton("Stop");
		this.btnStart = new JButton("Start");
		
		this.btnExit.addActionListener(this);
		this.btnStart.addActionListener(this);
		
		this.add(new JScrollPane(monitor));
		this.add(btnExit);
		this.add(btnStart);
		
		this.setVisible(true);
	}
	
	/*Start server work*/
	public void start()
	{
		try{
			this.putMessage("Server starts listening on port " + this.server.port);
			this.server.listen();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*Put message into the monitor*/
	public void putMessage(final String msg)
	{
		this.log.println(msg);
		this.log.flush();
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				monitor.append(msg + "\n");
			}
			
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.btnStart){
			this.start();
			this.btnStart.setEnabled(false);
		}
	}

	@Override
	public void onClientConnected(Connection client) {
		// TODO Auto-generated method stub
		this.putMessage("Client " + client.socket.getLocalAddress() + " connected !");
		
	}

	@Override
	public void onClientDisconnected(Connection client) {
		// TODO Auto-generated method stub
		this.putMessage("Client " + client.socket.getLocalAddress() + " disconnected.");
	} 

	@Override
	public void onReceivedMessage(String msg) {
		// TODO Auto-generated method stub
		this.putMessage(msg);
	}
}
