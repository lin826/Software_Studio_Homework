package main;
import java.io.IOException;

import network.GameServer;


public class MainServer {
	
	public static void main(String[] args)
	{
		try {
			GameServer server = new GameServer();
			ServerGUI frame = new ServerGUI(server);
			server.addEventListener(frame);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
