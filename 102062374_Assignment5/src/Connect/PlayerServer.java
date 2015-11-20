package Connect;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Game.Model;

//import PlayerServer.ConnectionThread;

@SuppressWarnings("serial")
public class PlayerServer extends JFrame {
	private ServerSocket serverSocket;
	private Socket serverToClient;
	private List<ConnectionThread> connections = new ArrayList<ConnectionThread>();
	
	private JTextArea textArea;
	private String numOfPlayers = "0";
	private Model model;
	
	
	public PlayerServer(int portNum) {
		try {
			this.serverSocket = new ServerSocket(portNum);
			System.out.printf("Server starts listening on port: %d.\n", portNum);
			
			this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("RobotServer");
			
			// Initialize textArea
			this.textArea = new JTextArea();
			this.textArea.setEditable(false);
			this.textArea.setPreferredSize(new Dimension(500,300));
			Font font = new Font("Verdana", Font.PLAIN, 16);
		    this.textArea.setFont(font);
			
			JScrollPane scrollPane = new JScrollPane(this.textArea);
		    this.add(scrollPane);
		    
		    this.textArea.append("Server starts listening on port: "+portNum+".\n");
		    this.pack();
		    this.setVisible(true);
		    

			model = new Model(
					"res/assignment4TestData/assignment4TestData.txt");
				    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void login() {
		System.out.println("Server starts waiting for client.");
		// Create a loop to make server wait for client forever (unless you stop it)
		// Make sure you do create a connectionThread and add it into "connections"
		while(!numOfPlayers.equals("2"))
		{
			try {
				int i = Integer.valueOf(numOfPlayers).intValue();
				i++;
				numOfPlayers = String.valueOf(i); 
				System.out.println("numOfPlayers = "+numOfPlayers);
				serverToClient = this.serverSocket.accept();
				String temp =  "Get connection from client " + 
						serverToClient.getInetAddress() + ": " +
						serverToClient.getPort()+"\n";
				System.out.print(temp);
				this.textArea.append(temp);
				ConnectionThread connThread = new ConnectionThread(serverToClient);
				this.connections.add(connThread);
				connThread.start();
				if(numOfPlayers.equals("2")) {
					this.broadcast("Game Ready!!");
					this.textArea.append("Game Ready!!\n");
					this.broadcast("Game Start!!");
					this.textArea.append("Game Start!!\n");
				}
				
			} catch (BindException e){
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	private void broadcast(String message) throws IOException {
		for (ConnectionThread connection: connections) {
			connection.sendMessage(message);
		}
	}
	
	/* Implement your inner class here
	 * 
	 * Define an inner class (name should be ConnectionThread ,Connection"Thread!!")
	 * for handling incoming sockets and sending message to other clients
	 * 
	 * Don't forget to perform "sendMessage", which is used in broadcast!!.
	 */
	
	class ConnectionThread extends Thread{
		
		private BufferedReader reader;
		private PrintWriter writer;

		public ConnectionThread(Socket socket) {
			try {
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				this.sendMessage(numOfPlayers);
				System.out.println("this.sendMessage("+numOfPlayers+");");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			while(true) {
				try {
					String line = this.reader.readLine();
					System.out.println(line);
					if(line.equals("setTagName"))
						setTagName(this.reader.readLine());
					else if(line.equals("setTagChunks"))
						setTagChunks(this.reader.readLine());
					else if(line.equals("hasWon"))
						hasWon();
					else if(line.equals("hasLose"))
						hasLose();
					else if(line.equals("findNewModel"))
						findNewModel();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void hasLose() {
			try {
				PlayerServer.this.broadcast("hasLose");
				findNewModel();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}

		private void hasWon() {
			try {
				PlayerServer.this.broadcast("hasWon");
				findNewModel();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void findNewModel() {
			try {
				PlayerServer.this.broadcast("NewModel\n"+model.getRandomPlane());
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		}

		private void setTagChunks(String line) {
			try {
				PlayerServer.this.broadcast("setTagChunks\n"+line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void setTagName(String line) {
			try {
				PlayerServer.this.broadcast("setTagName\n"+line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void sendMessage(String message) throws IOException {
			System.out.print("sendMessage: "+message+"\n");
			this.writer.println(message);
			this.writer.flush();

		}

	}
	
	
	public static void main(String[] args) {
		
		PlayerServer server = new PlayerServer(8000);
		server.login();
	}

}
