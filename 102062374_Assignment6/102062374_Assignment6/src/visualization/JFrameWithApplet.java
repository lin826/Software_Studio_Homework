package visualization;
import javax.swing.JFrame;


public class JFrameWithApplet {

	public static void main(String[] args) {
		
		MyApplet theApplet = new MyApplet();
		theApplet.init();
		theApplet.start();
		
		JFrame window = new JFrame("102062374_Assignment6");
		window.setContentPane(theApplet);
		window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		window.setSize(1020, 770);
		window.setVisible(true);
		
	}

}
