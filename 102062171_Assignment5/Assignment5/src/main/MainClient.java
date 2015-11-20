package main;
import network.GameClient;

import org.json.JSONObject;


public class MainClient {
	public static void main(String[] args)
	{
		GameClient client = new GameClient();
		ClientGUI frame = new ClientGUI(client);
		client.addEventListener(frame);
		
	}
}
