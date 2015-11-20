package Connect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

@SuppressWarnings("serial")
public class Login extends JFrame implements Runnable{
	
	private String serverIP = "";
	private String portNumber = "";
	private String message=null;
	JSONObject jsonObjectJacky;
	
	private JTextArea textArea1,textArea2,textArea3;
	private JTextField textField1,textField2;
	private JButton button;
	public Login() {
		this.setLayout(null);
		this.setSize(500, 550);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("RobotClient");
	    Font font = new Font("Verdana", Font.PLAIN, 16);
		
		// Initialize textArea
		this.textArea1 = new JTextArea();
		this.textArea1.setEditable(false);
		this.textArea1.setBackground(Color.WHITE);
	    this.textArea1.setBounds(0, 20,500,300);
		this.textArea1.setFont(font);
	    this.add(textArea1);
	    
	    this.textArea2 = new JTextArea();
		this.textArea2.setEditable(false);
		this.textArea2.setBackground(null);
		this.textArea2.setFont(font);
	    this.textArea2.setBounds(20, 350,120,30);
	    this.textArea2.setText("Server IP:");
	    this.add(textArea2);
	    
	    this.textArea3 = new JTextArea();
		this.textArea3.setEditable(false);
		this.textArea3.setBackground(null);
		this.textArea3.setFont(font);
	    this.textArea3.setBounds(20, 400,120,30);
	    this.textArea3.setText("Port Number:");
	    this.add(textArea3);
	    
	    // Initialize textField
	    this.textField1 = new JTextField();
	    this.textField1.setBounds(150, 350, 300, 30);
	    this.textField1.setFont(font);
	    this.textField1.setText("127.0.0.1");
	    this.add(textField1);
	    
	    this.textField2 = new JTextField();
	    this.textField2.setPreferredSize(new Dimension(500,40));
	    this.textField2.setBounds(150, 400, 300, 30);
	    this.textField2.setFont(font);
	    this.textField2.setText("8000");
	    this.add(textField2);

	    this.button = new JButton();
	    this.button.setText("Log in");
	    this.button.setBounds(210, 450, 70, 30);
	    this.add(button);
	    /*this.button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			*/
				try{
				System.out.println("Button Was Clicked!");
				String temp = Login.this.textField2.getText();
				Login.this.portNumber = temp;
				Login.this.serverIP = Login.this.textField1.getText();
				if(!Login.this.serverIP.equals("")&& Login.this.portNumber!="")
				{
					Login.this.button.setEnabled(false);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("serverIP", Login.this.serverIP);
					map.put("portNumber", Login.this.portNumber);
					jsonObjectJacky = new JSONObject(map);
					Login.this.setSource(jsonObjectJacky);
				}					
				//System.out.println("serverIP = "+Login.this.serverIP);
				//System.out.println("portNumber = "+Login.this.portNumber);
				}
				catch(Exception e1){}
/*			}	    	
	    });
*/
		this.setVisible(true);
	
		//System.out.println(SwingUtilities.isEventDispatchThread());
	}
	private void addLine(final String message) {
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				//System.out.print("addLine"+message+"\n");
				Login.this.textArea1.append(message); 
			}
			
		});
	}
	public void printToLogin(String message)
	{
		this.message = message;
		this.addLine(message);
	}
	public void setSource (JSONObject jsonObject)
	{
		this.jsonObjectJacky = jsonObject;
	}
	public JSONObject getSource ()
	{
		return this.jsonObjectJacky;
	}

	@Override
	public void run() {
		if(message!=null)
		{
		System.out.println(message);
		Login.this.addLine(message);
		message=null;
		}
	}
}
