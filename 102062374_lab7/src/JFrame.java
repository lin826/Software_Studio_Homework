
public class JFrame {

	public static void main(String[] args) {
		Applet theApplet = new Applet();
		theApplet.init();
		theApplet.start();
		
		javax.swing.JFrame window = new javax.swing.JFrame("102062374_lab7");
		window.setContentPane(theApplet);
		window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		window.setSize(1000, 800);
		window.setVisible(true);

	}

}
