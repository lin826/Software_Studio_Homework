package network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*	GameServer	
 * 	@port: socket is listening on
 * 	@serversocket: accept client
 * 	@clients: a list of connections
 * */

public class GameServer{
	
	final public int port = 8080;
	final private ServerSocket serverSocket;
	final private List<Connection> clients;
	final private ListenThread listenThread; 

	//Event handler
	private GameServerEventListener eventHandler = null;
	private MessageHandler msgHandler;
	
	public GameServer() throws IOException
	{
		this.serverSocket = new ServerSocket(port);
		this.clients = new ArrayList<Connection>();	
		this.listenThread = new ListenThread();
		this.msgHandler = new MessageHandler();
	}
	
	public void addEventListener(GameServerEventListener eventHandler)
	{
		this.eventHandler = eventHandler;
	}
	
	/*Start to listen*/
	public void listen()
	{
		this.listenThread.start();
	}
	
	/*	Listening clients	*/
	private class ListenThread extends Thread
	{
		public void run()
		{
			try{
				while(!serverSocket.isClosed()){
					Socket clientSocket = serverSocket.accept();
					Connection clientConnection = new Connection(clientSocket);
					clientConnection.setMessageHandler(msgHandler);
					clientConnection.setEventListener(eventHandler);
					clientConnection.triggerClientConnected(clientConnection);
					clientConnection.start();
					
					clients.add(clientConnection);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
