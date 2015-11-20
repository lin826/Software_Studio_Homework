package Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import GameForP1.GameForP1;
import GameForP2.GameForP2;

import org.json.JSONObject;

public class PlayerClient {
	private String destinationIPAddr;
	private int destinationPortNum;
	private Socket socket;
	private PrintWriter writer;
	private Login login;

	private String serverIP = "";
	private int portNumber;

	private boolean isReady = false;
	public GameForP1 game1;
	public GameForP2 game2;
	private String playerNumber;

	public PlayerClient() {
		;
	}

	/*
	 * public PlayerClient(String IPAddress, int portNum) { this();
	 * 
	 * this.destinationIPAddr = IPAddress; this.destinationPortNum = portNum; }
	 */

	/*
	 * private void serverToClient(String message) {
	 * 
	 * SwingUtilities.invokeLater(new Runnable(){
	 * 
	 * @Override public void run() {
	 * System.out.println("serverToClient="+SwingUtilities
	 * .isEventDispatchThread()); StringBuilder sBuilder = new StringBuilder();
	 * sBuilder.append(message);
	 * PlayerClient.this.login.printToLogin(sBuilder.toString()); }
	 * 
	 * }); }
	 */

	public PlayerClient setIPAddress(String IPAddress) {
		this.destinationIPAddr = IPAddress;
		return this;
	}

	public PlayerClient setPort(int portNum) {
		this.destinationPortNum = portNum;
		return this;
	}

	public void connect() {
		ReaderThread readerThread;
		JSONObject jsonObjectJackyFromString;
		try {
			while (this.serverIP.equals("") || this.portNumber == 0) {
				jsonObjectJackyFromString = this.login.getSource();
				try {
					// System.out.println(jsonObjectJackyFromString);
					System.out.println(jsonObjectJackyFromString
							.get("serverIP"));
					System.out.println(jsonObjectJackyFromString
							.get("portNumber"));
					this.serverIP += jsonObjectJackyFromString.get("serverIP");
					this.portNumber = Integer.valueOf(
							"" + jsonObjectJackyFromString.get("portNumber"))
							.intValue();
				} catch (Exception e) {
				}
				// System.out.println("portNumber = "+client.portNumber);
			}
			this.setIPAddress(this.serverIP).setPort(this.portNumber);
			System.out.print("connect()\n");
			this.socket = new Socket(this.destinationIPAddr,
					this.destinationPortNum);
			this.writer = new PrintWriter(new OutputStreamWriter(
					this.socket.getOutputStream()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			readerThread = new ReaderThread(reader, writer);
			StartThread();
			readerThread.start();
		} catch (BindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setPlayerNumber(String readLine) {
		this.playerNumber = readLine;
	}

	private void addLine(final String message) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// System.out.print("addLine"+message+"\n");
				PlayerClient.this.login.printToLogin(message + "\n");
			}

		});
	}

	private void StartThread() {
		if (this.playerNumber.equals("1") && !this.isReady) {
			this.login.printToLogin("Waiting for Player2\n");
		} else if (this.playerNumber.equals("2") && !this.isReady) {
			this.login.printToLogin("Player matched!\n");
		}
	}

	class ReaderThread extends Thread {
		private BufferedReader reader;
		private PrintWriter writer;
		private String line;

		public ReaderThread(BufferedReader reader, PrintWriter writer) {
			this.reader = reader;
			this.writer = writer;
			try {
				PlayerClient.this.setPlayerNumber(reader.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {
				try {
					System.out.print("ReaderThread\n");
					line = reader.readLine();
					System.out.print(line + "\n");
					PlayerClient.this.addLine(line);
					if (line.equals("Game Ready!!")) {
						PlayerClient.this.isReady = true;
						line = reader.readLine();
						PlayerClient.this.addLine(line);
						// System.out.println(line.equals("Game Ready!!"));
						if (PlayerClient.this.playerNumber.equals("1")) {
							try {
								System.out.print("StartG1\n");
								PlayerClient.this.game1 = new GameForP1();
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println("Game1 Start!");
							this.clientToServer("findNewModel");
							System.out.println("findNewModel");
							line = this.reader.readLine();
							if (line.equals("NewModel")) {
								System.out.println("NewModel");
								// this.clientToServer("findNewModel");
								line = this.reader.readLine();
								PlayerClient.this.game1
										.createCurrentModel(line);
								PlayerClient.this.game1.nextStage();
								PlayerClient.this.game1.start();
							}
						} else if (PlayerClient.this.playerNumber.equals("2")) {
							try {
								PlayerClient.this.game2 = new GameForP2();
								System.out.print("StartG2\n");
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println("Game2 Start!");
							line = this.reader.readLine();
							if (line.equals("NewModel")) {
								System.out.println("NewModel");
								line = this.reader.readLine();
								PlayerClient.this.game2
										.createCurrentModel(line);
								PlayerClient.this.game2.nextStage();
								PlayerClient.this.game2.start();
							}
						}
						WriterThread writerThread = new WriterThread(reader,
								writer);
						writerThread.start();
					}// else if(true);
					else if (PlayerClient.this.game1 != null
							|| PlayerClient.this.game2 != null) {
						if (PlayerClient.this.playerNumber.equals("1")) {
							if (line.equals("NewModel"))
								PlayerClient.this.game1
										.createCurrentModel(this.reader
												.readLine());
							else if (line.equals("hasWon"))
								PlayerClient.this.game1.hasWon();
							else if (line.equals("hasLose"))
								PlayerClient.this.game1.hasLose();

						} else if (PlayerClient.this.playerNumber.equals("2")) {
							if (line.equals("NewModel"))
								PlayerClient.this.game2
										.createCurrentModel(this.reader
												.readLine());
							else if (line.equals("setTagChunks"))
								PlayerClient.this.game2
										.setTagChunks(this.reader.readLine());
							else if (line.equals("setTagName"))
								PlayerClient.this.game2.setTagName(this.reader
										.readLine());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void clientToServer(String message) {
			System.out.println("clientToServer=" + message);
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append(message);
			this.writer.println(sBuilder.toString());
			this.writer.flush();
		}
	}

	class WriterThread extends Thread {
		private PrintWriter writer;

		public WriterThread(BufferedReader reader, PrintWriter writer) {
			this.writer = writer;
		}

		public void run() {
			while (true) {
				System.out.print("WriterThread\n");
				if (PlayerClient.this.playerNumber.equals("1")) {
					if (PlayerClient.this.game1.hasTagName()
							&& !PlayerClient.this.game1.isSubmitFlag()) {
						this.clientToServer("setTagName\n"
								+ PlayerClient.this.game1.tagName);
						this.clientToServer("setTagChunks\n"
								+ Arrays.toString(PlayerClient.this.game1
										.getChunks()));
						System.out.println("TagName = "
								+ PlayerClient.this.game1.tagName);
						System.out.println("TagBlocks = "
								+ Arrays.toString(PlayerClient.this.game1
										.getChunks()));
						PlayerClient.this.game1.tagName = "";
						PlayerClient.this.game1.setSubmitFlag(true);

					}
				} else if (PlayerClient.this.playerNumber.equals("2")) {
					if (PlayerClient.this.game2.getCurrentModel() == null) {
						this.clientToServer("findNewModel");
						System.out.println("findNewModel");
					} else if (PlayerClient.this.game2.currentStage.isWinFlag()) {
						this.clientToServer("hasWon");
					} else if (PlayerClient.this.game2.currentStage.hasLose()) {
						this.clientToServer("hasLose");
					}
				}
				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		private void clientToServer(String message) {
			System.out.println("clientToServer=" + message);
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append(message);
			this.writer.println(sBuilder.toString());
			this.writer.flush();
		}
	}

	public static void main(String[] args) {
		PlayerClient client = new PlayerClient();
		client.startLogin();
		client.connect();
		client.startGame();

		/* Equivalent of the above */
		// PlayerClient client = new PlayerClient("127.0.0.1", 8000);
		// client.connect();
	}

	private void startGame() {
		try {
			if (this.playerNumber.equals("1"))
				game1 = new GameForP1();
			else if (this.playerNumber.equals("2"))
				game2 = new GameForP2();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void startLogin() {
		this.login = new Login();
	}
}
