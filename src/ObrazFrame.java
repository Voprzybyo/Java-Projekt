import java.io.IOException;

import javax.swing.JFrame;
 import javax.swing.JPanel;
 
 public class ObrazFrame extends JFrame {
 
 	public ObrazFrame() throws IOException {
 		super("COVID-19");
 
 		JPanel obrazPanel = new ObrazPanel();
 		add(obrazPanel);
 
 		pack();
 		setVisible(true);
 	}
 }