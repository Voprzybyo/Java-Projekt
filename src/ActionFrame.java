import javax.swing.*;
import java.awt.*;
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class ActionFrame extends JFrame {

    public ActionFrame() {
        super("COVID-19");				//Konstruktor nadrz�dny
        JPanel panel = new JPanel();	//Nowy panel
        add(panel);
        JLabel tekst = new JLabel("COVID-19");					//G��wny napis menu
        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));
        tekst.setForeground( Color.red.darker());				//Kolor czcionki
        panel.add(tekst);				//Dodanie napisu g��wnego do panelu
        panel.setSize(200,200);
        
        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel);
        setLayout(new FlowLayout());	//Taki layout manager z biblioteki JPanel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();							//Metoda pack() ustala rozmiar okna tak, aby mie�ci�y si� w nim wszystkie widoczne komponenty.
        setSize(300,200);				//Wymiary okna
        setLocation(600,300);			//Po�o�enie lewego g�rnego rogu okna
        setVisible(true);
    }
}