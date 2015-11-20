import javax.swing.JProgressBar;

/*This class deals with time bar gui, please implement it*/
public class TimeBar extends JProgressBar {

	public void TimeBar() {
		this.setOrientation(JProgressBar.HORIZONTAL);
		this.setMinimum(0);
		this.setMaximum(100);
		this.setValue(100);
		// progressBar.setStringPainted(true);
		this.setBorderPainted(true);
	}
}
