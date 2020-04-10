import javax.swing.*;
 
 public class ActionFrame extends JFrame {
 
 	public ActionFrame() {
 		super("COVID-19");
 
 		JPanel buttonPanel = new ButtonPanel();
 		add(buttonPanel);
 
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		pack();
 		setLocation(600,300);
 		setVisible(true);
 	}
 }