import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class ButtonPanel extends JPanel{

	/*Sta�e okre�laj�ce wysoko�� i szeroko�� panelu*/
    public static final int HEIGHT = 100;
    public static final int WIDTH = 200;
    
    /*Przyciski na panelu*/
    private JButton Button1;
    private JButton Button2;
    private JButton Button3;


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
            InfoFrame.RamkaInformacyjnaSwiat();				//Wywo�anie metody wy�wietlaj�cej ramke
        }
    }

}