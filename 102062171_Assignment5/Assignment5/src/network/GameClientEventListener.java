package network;

public interface GameClientEventListener {
	public void onServerConnected();
	public void onReceiveMessage(String msg);
}
