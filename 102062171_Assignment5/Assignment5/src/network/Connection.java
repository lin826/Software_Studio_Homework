package network;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*	Handling client connection	*/
public class Connection extends Thread {
	
	final public Socket socket;
	
	private PrintWriter writer;
	private BufferedReader reader;
	
	private MessageHandler msgHandler = null;
	private GameServerEventListener eventHandler = null;
	
	public Connection(Socket client) throws IOException {
		this.socket = client;
		this.writer = new PrintWriter(this.socket.getOutputStream());
		this.reader = new BufferedReader(new InputStreamReader(
				this.socket.getInputStream()));
	}
	
	public void setEventListener(GameServerEventListener eventHandler)
	{
		this.eventHandler = eventHandler;
	}
	
	public void setMessageHandler(MessageHandler msgHandler)
	{
		this.msgHandler = msgHandler;
	}
	
	public void run() {
		listen();
	}
	
	public void sendMessage(String msg)
	{
		this.writer.println(msg);
		this.writer.flush();
	}
	
	/* Listening Client */
	private void listen() {
		try {
			String msg;
			while (!this.socket.isClosed()) {
				if((msg = reader.readLine()) != null)
					triggerReceivedMessage(msg);
				else 
					break;
			}
			
			triggerClientDisconnected(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*Trigger event*/
	public void triggerClientConnected(Connection client)
	{
		if(this.eventHandler != null)
			this.eventHandler.onClientConnected(client);
		
		if(this.msgHandler != null)
			this.msgHandler.onClientConnected(client);
		
	}
	
	private void triggerClientDisconnected(Connection client)
	{
		if(this.eventHandler != null)
			this.eventHandler.onClientDisconnected(client);
		
		if(this.msgHandler != null)
			this.msgHandler.onClientDisconnected(client);
	}
	
	private void triggerReceivedMessage(String msg)
	{
		if(this.eventHandler != null)
			this.eventHandler.onReceivedMessage(msg);
		
		if(this.msgHandler != null)
			this.msgHandler.onReceivedMessage(msg);
	}
	
}
