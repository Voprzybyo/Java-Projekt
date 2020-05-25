import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;
import org.apache.log4j.Logger; 
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class ButtonPanel extends JPanel{

	/*Sta�e okre�laj�ce wysoko�� i szeroko�� panelu*/
    public static final int HEIGHT = 100;
    public static final int WIDTH = 200;
    
    /*Przyciski na panelu*/
    private JButton Button1;
    private JButton Button2;
    private JButton Button3;

    static Logger logger = Logger.getLogger(ButtonPanel.class);
    
    public ButtonPanel() {

    	/*Utworzenie 3 przycisk�w + wywo�anie ich ActionListener'�w*/
        Button1 = new Button1();
        Button2 = new Button2();
        Button3 = new Button3();

        /*Wygl�d GUI*/
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(Button1);
        add(Button2);
        add(Button3);
        
        String log4jConfigFile = System.getProperty("user.dir")		//�cie�ka do pliku log4j.properties
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);			//Wywo�anie z wy�ej z�o�on� �cie�k�
    }

    
    /*Przycisk 1 --> po wci�ni�ciu wywo�ywana jest metoda wy�wietlaj�ca obraz z podanego adresu url*/
    class Button1 extends JButton implements ActionListener {

        Button1() {
            super("Co to jest?"); 							//Konstruktor klasy nadrz�dnej
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this); 						//event handler
        }

        public void actionPerformed(ActionEvent e) {
            try {
            	logger.info("Przycisk 1 --> Wywo�ano metode wy�wietlaj�c� obraz");
                new ObrazFrame();							//Wywo�anie metody otwieraj�cej obraz
                
            } catch (IOException e1) {
                e1.printStackTrace();						//Pomaga �ledzi� wyj�tek --> M�wi, kt�ra metoda powoduje b��d
                
            }
        }
    }
    
    /*Przycisk 2 --> po wci�ni�ciu wywo�ywana jest metoda wy�wietlaj�ca now� ramk� z informacjami*/
    class Button2 extends JButton implements ActionListener {

        Button2() {
            super("POLSKA");								//Konstruktor klasy nadrz�dnej
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this);						//event handler
        }

        public void actionPerformed(ActionEvent e) {
        	
        	logger.info("Przycisk 2 --> Wywo�ano metode wy�wietlaj�c� info o Polsce");
            InfoFrame.RamkaInformacyjnaPolska();			//Wywo�anie metody wy�wietlaj�cej ramke
        }
    }

    /*Przycisk 3 --> po wci�ni�ciu wywo�ywana jest metoda wy�wietlaj�ca now� ramk� z informacjami*/
    class Button3 extends JButton implements ActionListener {

        Button3() {
            super("�WIAT");									//Konstruktor klasy nadrz�dnej
            setFont(new Font("Helvetica", Font.BOLD, 15));
            addActionListener(this); 						//event handler
        }

        public void actionPerformed(ActionEvent e) {
            logger.info("Przycisk 3 --> Wywo�ano metode wy�wietlaj�c� info o �wiecie");
            InfoFrame.RamkaInformacyjnaSwiat();				//Wywo�anie metody wy�wietlaj�cej ramke
        }
    }

}