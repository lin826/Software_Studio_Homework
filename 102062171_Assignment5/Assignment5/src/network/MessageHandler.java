package network;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONException;
import org.json.JSONObject;


/*	MessageHandler
 * 	Client Requests:
 *  
 *  Type : Init
 *  initial state of the server (matched/wait) , this just informs client of the server's state
 *  
 *  Type : Ready
 *  ready for the next stage
 *  value : boolean (all false means that the game is over)
 *        
 *  Type : Tag
 *  send the tag index to the server , and then the server transfer these indexes to another client
 *  @Source: string
 *  @Desc: string
 *  @size: int
 *  @area: int array
 *     
 *  Type : Ans
 *  the solving result
 *  Value : boolean
 *      
 * 	Server Message:
 * 	
 * 	Type : Start
 * 	inform the two client to start the game
 *  value : null
 *     
 *  Type : Stage   
 *  give both client a next stage index
 *  value : int
 *  
 *  Type : Control
 *  set control 
 *  value : null
 *     
 *  Server State:
 *  WAIT : wait for two players
 *  READY : ready to processing game stage info
 *  END : ending
 * */

public class MessageHandler implements GameServerEventListener
{	
	final int MAX_CLIENT = 2;
	private int index = 0 , readyCount = 0 , stageCount = 7 ;
	private List<Connection> clients;
	private Connection tagClient = null;
	private Connection ansClient = null;
	
	public MessageHandler()
	{
		this.clients = new ArrayList<Connection>();
	}
	
	/*Add connection to the handler
	 * Call by server listening thread*/
	public void addClient(Connection client)
	{
		if(index < 2){
			this.clients.add(client);
			index++;
		}
	}
		
	/*Process message from client and dispatch a response to both or one*/
	private void processMessage(String msg)
	{
		try{
			JSONObject request = new JSONObject(msg);
			String type = (String) request.get("Type");
			
			if(type.equals("Ready")){
				this.readyCount++;
				if(this.readyCount >= 2){
					this.sendStageIndex();
					this.sendResetControl();
					this.readyCount = 0;
				}
			}
			else if(type.equals("Ans")){
				this.sendResult(request);
				sendControl(this.ansClient , false);
				sendControl(this.tagClient , true);
			}
			else if(type.equals("Tag")){
				this.sendTag(request);
				sendControl(this.ansClient , true);
				sendControl(this.tagClient , false);
			}
			
		} catch(JSONException e){
			//Plain text test
			this.sendBroadcast(msg);	
		}
	}
	
	/*	initialize the game*/
	private void initGame()
	{
		this.markClients();
		this.sendStart();
	}
	
	/* 	mark clients */
	private void markClients()
	{
		try{
			this.tagClient = this.clients.get(0);
			this.ansClient = this.clients.get(1);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*	check game client count*/
	private boolean isMatched()
	{
		return this.index >= 2;
	}
	
	/* Send the control side of the client*/
	private void sendSide(Connection client , String side)
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Side");
		response.put("Value", side);
		
		sendResponseTo(client ,response);
	}
	
	/* Send the wait signal if not match	*/
	private void sendInitState(Connection client)
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Init");
		response.put("Value", isMatched());
		
		sendResponseTo(client ,response);
	}
	
	/* Send a random stage index*/
	private void sendStageIndex()
	{
		Random rnd = new Random();
		int index = rnd.nextInt(this.stageCount);
		JSONObject response = new JSONObject();
		response.put("Type", "Stage");
		response.put("Value", index);
		System.out.println("Next stage : Stage " + index);
		sendBroadcast(response);
	}
	
	/*Send the result to another client*/
	private void sendResult(JSONObject response)
	{
		sendResponseTo(this.tagClient , response);
	}
	
	/*Send a control signal to switch the control between clients*/
	private void sendControl(Connection client , boolean val)
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Control");
		response.put("Value", val);
		
		sendResponseTo(client , response);
	}
	
	
	/* 	reset the control state*/
	private void sendResetControl()
	{
		this.sendControl(this.ansClient, false);
		this.sendControl(this.tagClient , true);
	}
	
	/*Send a start signal to both*/
	private void sendStart()
	{
		JSONObject response = new JSONObject();
		response.put("Type", "Start");
		
		sendBroadcast(response);
	}
	
	/*Send tag info to ansClient*/
	private void sendTag(JSONObject request)
	{
		sendResponseTo(this.ansClient , request);
	}
	
	/*Send response to one client by transforming jsonobject to string*/
	private void sendResponseTo(Connection client , JSONObject response)
	{
		String responseString = response.toString();
		client.sendMessage(responseString);
	}
	
	/*Send response to both client*/
	private void sendBroadcast(JSONObject response)
	{
		for(Connection client : this.clients){
			sendResponseTo(client, response);
		}
	}
	
	/*Send plain text response to both client*/
	private void sendBroadcast(String msg)
	{
		for(Connection client : this.clients){
			client.sendMessage(msg);
		}
	}

	@Override
	public void onClientConnected(Connection client) {
		// TODO Auto-generated method stub
		this.addClient(client);
		this.sendInitState(client);
		
		if(this.isMatched()){
			this.sendSide(client, "Ans");
			this.initGame();
		}else{
			this.sendSide(client, "Tag");
		}
	}

	@Override
	public void onClientDisconnected(Connection client) {
		// TODO Auto-generated method stub
		this.clients.remove(client);
	}

	@Override
	public void onReceivedMessage(String msg) {
		// TODO Auto-generated method stub
		this.processMessage(msg);
	}
}
