package network;

import java.net.Socket;

public interface GameServerEventListener {
	public void onClientConnected(Connection client);
	public void onClientDisconnected(Connection client);
	public void onReceivedMessage(String msg);
}
