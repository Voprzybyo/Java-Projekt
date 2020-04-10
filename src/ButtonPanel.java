import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
 import javax.swing.JPanel;
 
 public class ButtonPanel extends JPanel{
 
 	public static final int HEIGHT = 50;
 	public static final int WIDTH = 400;
 	private JButton greenButton;
 	private JButton blueButton;
 	private JButton redButton;
 
 	private JPanel buttonPanel;
 
 	public ButtonPanel() {
 		greenButton = new GreenButton();
 		blueButton = new BlueButton();
 		redButton = new RedButton();
 
 		buttonPanel = this;
 
 		setLayout(new FlowLayout());
 		setPreferredSize(new Dimension(WIDTH, HEIGHT));
 		add(greenButton);
 		add(blueButton);
 		add(redButton);
 	}
 
 	class GreenButton extends JButton implements ActionListener {
 
 		GreenButton() {
 			super("Co to jest?");
 			setFont(new Font("Helvetica", Font.BOLD, 15));
 			addActionListener(this);
 		}
 
 		@Override
 		public void actionPerformed(ActionEvent e) {
 			try {
				new ObrazFrame();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
 		}
 	}
 
 	class BlueButton extends JButton implements ActionListener {
 
 		BlueButton() {
 			super("POLSKA");
 			setFont(new Font("Helvetica", Font.BOLD, 15));
 			addActionListener(this);
 		}
 
 		@Override
 		public void actionPerformed(ActionEvent e) {
 			
 			InfoFrame.RamkaInformacyjnaPolska();	
 		}
 	}
 
 	class RedButton extends JButton implements ActionListener {
 
 		RedButton() {
 			super("ŒWIAT");
 			setFont(new Font("Helvetica", Font.BOLD, 15));
 			addActionListener(this);
 		}
 
 		@Override
 		public void actionPerformed(ActionEvent e) {
 			InfoFrame.RamkaInformacyjnaSwiat();	
 		}
 	}
 
 }