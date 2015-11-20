package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONObject;

public class GameClient {
	
	final int port = 8080;
	
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private ReceiveThread receiveThread;
	
	private Queue<GameClientEventListener> eventHandlers = null;
	
	public GameClient()
	{
		this.receiveThread = new ReceiveThread();
		this.eventHandlers = new ConcurrentLinkedQueue<GameClientEventListener>();
	}
	
	public void addEventListener(GameClientEventListener eventHandler)
	{
		this.eventHandlers.add(eventHandler);
	}
	
	/*Init all networking variables*/
	public void connect(String addr) throws UnknownHostException, IOException
	{
		this.socket = new Socket(addr , port);
		this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		this.writer = new PrintWriter(this.socket.getOutputStream());
		this.receiveThread.start();
		
		this.triggerServerConnected();
		
	}
		
	/*Send tag message to another client via server*/
	public void sendTag(JSONObject response)
	{
		sendMessage(response.toString());
	}
	
	/*Send ready message to the server*/
	public void sendReady()
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Ready");
		
		sendMessage(response.toString());
	}
	
	/*Send answer message to another client via server*/
	public void sendAnswer(boolean flag)
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Ans");
		response.put("Value", flag);
		
		sendMessage(response.toString());
	}
	
	/*Send end signal to the server*/
	public void sendEnd()
	{
		JSONObject response = new JSONObject();
		response.put("Type", "End");
		
		sendMessage(response.toString());
	}
	
	/*Send message to the server*/
	public void sendMessage(String msg)
	{
		this.writer.println(msg);
		this.writer.flush();
	}
	
	/*Trigger Events*/
	public void triggerServerConnected()
	{
		for(GameClientEventListener handler : this.eventHandlers)
			handler.onServerConnected();
	}
	
	public void triggerReceivedMessage(String msg)
	{
		for(GameClientEventListener handler : this.eventHandlers)
			handler.onReceiveMessage(msg);
	}
	
	/*Continuously receive message from server*/
	private class ReceiveThread extends Thread
	{
		public void run()
		{
			try{
				String msg;
				while(!socket.isClosed()){
					if((msg = reader.readLine()) != null)
						triggerReceivedMessage(msg);
					else 
						break;
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
