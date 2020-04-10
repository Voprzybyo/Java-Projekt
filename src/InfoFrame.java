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
		
		 JFrame jf = new JFrame("POLSKA STATS");
		 jf.setLayout(new FlowLayout());
		 JPanel panel = new JPanel();
		 jf.add(panel);
		
	 
	        s = "<html>Statystyki w Polsce<br><br>";
	        s += "ZAKA¯ENIA: " + MainClass.Dane1.get(0) + "<br>";
	        s += "ŒMIERCI: " + MainClass.Dane1.get(1) + "<br>";
	        s += "WYLECZONYCH: " + MainClass.Dane1.get(2) + "<br></html>\"";
	        
	        JLabel tekst = new JLabel(s);
	        panel.add(tekst);  
	        panel.setSize(200,200);  
	      
	        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));
	        
	     jf.setLocation(600,400);
		 jf.pack();
		 jf.setVisible(true);
		 }
	
		 public static void RamkaInformacyjnaSwiat() {
			 JFrame jf = new JFrame("ŒWIAT STATS");
			 jf.setLayout(new FlowLayout());
			 JPanel panel = new JPanel();
			 jf.add(panel);
			
		 
		        s = "<html>Statystyki na œwiecie<br><br>";
		        s += "ZAKA¯ENIA: " + MainClass.Dane2.get(0) + "<br>";
		        s += "ŒMIERCI: " + MainClass.Dane2.get(1)+ "<br>";
		        s += "WYLECZONYCH: " + MainClass.Dane2.get(2) + "<br></html>\"";
		        
		        JLabel tekst = new JLabel(s);
		        panel.add(tekst);  
		        panel.setSize(200,200);  
		       
		        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));
		        
		     jf.setLocation(600,400);
			 jf.pack();
			 jf.setVisible(true);
		 }
	
}
