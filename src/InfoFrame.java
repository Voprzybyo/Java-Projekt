import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JDialog;

import java.awt.*;

public class InfoFrame extends JFrame  {
	
	private static JDialog d;
	 private static String s;
	
	public InfoFrame() {
 		super("Ramka informacyjna");
 
 		JPanel buttonPanel = new ButtonPanel();
 		add(buttonPanel);
 
 		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 		pack();
 		setVisible(true);
 	}
	
	public static void RamkaInformacyjnaPolska() {
		 JFrame jf = new JFrame("STATYSTYKI");
		 jf.setLayout(new FlowLayout());

		 	d = new JDialog(jf , "STATYSTYKI W POLSCE", true);  
	        d.setLayout( new FlowLayout() );  
	       
	        s = "<html>Statystyki w Polsce<br><br>";
	        s += "ZAKA¯ENIA: " + MainClass.Dane1.get(0) + "<br>";
	        s += "ŒMIERCI: " + MainClass.Dane1.get(1) + "<br>";
	        s += "WYLECZONYCH: " + MainClass.Dane1.get(2) + "<br></html>\"";
	        
	        d.add( new JLabel (s) ,SwingConstants.CENTER);  
	        d.setSize(200,200);  
	        d.setLocation(600,400);
	        d.setFont(new Font("Helvetica", Font.BOLD, 35));
	        d.setVisible(true);  
	        
		 jf.pack();
		 jf.setVisible(true);
		 jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 d.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		 }
	
	public static void RamkaInformacyjnaSwiat() {
		 JFrame jf2 = new JFrame("RAMKA3");
		 jf2.setLayout(new FlowLayout());

		 	d = new JDialog(jf2 , "STATYSTYKI NA ŒWIECIE", true);  
	        d.setLayout( new FlowLayout() );  
	       
	        s = "<html>Statystyki na œwiecie<br><br>";
	        s += "ZAKA¯ENIA: " + MainClass.Dane2.get(0) + "<br>";
	        s += "ŒMIERCI: " + MainClass.Dane2.get(1) + "<br>";
	        s += "WYLECZONYCH: " + MainClass.Dane2.get(2) + "<br></html>\"";
	        
	        d.add( new JLabel (s) ,SwingConstants.CENTER); 
	        d.setLocation(600,400);
	        d.setSize(300,200);  
	       
	        d.setVisible(true);  
	        
		 jf2.pack();
		 jf2.setVisible(true);
		 jf2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 }
	
}
