import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;
import org.apache.log4j.Logger; 
@SuppressWarnings("serial") // Pozbycie siê warningów...

public class ButtonPanel extends JPanel{

	/*Sta³e okreœlaj¹ce wysokoœæ i szerokoœæ panelu*/
    public static final int HEIGHT = 100;
    public static final int WIDTH = 200;
    
    /*Przyciski na panelu*/
    private JButton Button1;
    private JButton Button2;
    private JButton Button3;

    static Logger logger = Logger.getLogger(ButtonPanel.class);
    
    public ButtonPanel() {

    	/*Utworzenie 3 przycisków + wywo³anie ich ActionListener'ów*/
        Button1 = new Button1();
        Button2 = new Button2();
        Button3 = new Button3();

        /*Wygl¹d GUI*/
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(Button1);
        add(Button2);
        add(Button3);
        
        String log4jConfigFile = System.getProperty("user.dir")		//Œcie¿ka do pliku log4j.properties
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);			//Wywo³anie z wy¿ej z³o¿on¹ œcie¿k¹
    }

    
    /*Przycisk 1 --> po wciœniêciu wywo³ywana jest metoda wyœwietlaj¹ca obraz z podanego adresu url*/
    class Button1 extends JButton implements ActionListener {

        Button1() {
            super("Co to jest?"); 							//Konstruktor klasy nadrzêdnej
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this); 						//event handler
        }

        public void actionPerformed(ActionEvent e) {
            try {
            	logger.info("Przycisk 1 --> Wywo³ano metode wyœwietlaj¹c¹ obraz");
                new ObrazFrame();							//Wywo³anie metody otwieraj¹cej obraz
                
            } catch (IOException e1) {
                e1.printStackTrace();						//Pomaga œledziæ wyj¹tek --> Mówi, która metoda powoduje b³¹d
                
            }
        }
    }
    
    /*Przycisk 2 --> po wciœniêciu wywo³ywana jest metoda wyœwietlaj¹ca now¹ ramkê z informacjami*/
    class Button2 extends JButton implements ActionListener {

        Button2() {
            super("POLSKA");								//Konstruktor klasy nadrzêdnej
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this);						//event handler
        }

        public void actionPerformed(ActionEvent e) {
        	
        	logger.info("Przycisk 2 --> Wywo³ano metode wyœwietlaj¹c¹ info o Polsce");
            InfoFrame.RamkaInformacyjnaPolska();			//Wywo³anie metody wyœwietlaj¹cej ramke
        }
    }

    /*Przycisk 3 --> po wciœniêciu wywo³ywana jest metoda wyœwietlaj¹ca now¹ ramkê z informacjami*/
    class Button3 extends JButton implements ActionListener {

        Button3() {
            super("ŒWIAT");									//Konstruktor klasy nadrzêdnej
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this); 						//event handler
        }

        public void actionPerformed(ActionEvent e) {
            logger.info("Przycisk 3 --> Wywo³ano metode wyœwietlaj¹c¹ info o Œwiecie");
            InfoFrame.RamkaInformacyjnaSwiat();				//Wywo³anie metody wyœwietlaj¹cej ramke
        }
    }

}