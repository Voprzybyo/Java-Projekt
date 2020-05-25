import javax.swing.*;
import java.awt.*;
@SuppressWarnings("serial") // Pozbycie siê warningów...

public class ActionFrame extends JFrame {

    public ActionFrame() {
        super("COVID-19");				//Konstruktor nadrzêdny
        JPanel panel = new JPanel();	//Nowy panel
        add(panel);
        JLabel tekst = new JLabel("COVID-19");					//G³ówny napis menu
        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));
        tekst.setForeground( Color.red.darker());				//Kolor czcionki
        panel.add(tekst);				//Dodanie napisu g³ównego do panelu
        panel.setSize(200,200);
        
        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel);
        setLayout(new FlowLayout());	//Taki layout manager z biblioteki JPanel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();							//Metoda pack() ustala rozmiar okna tak, aby mieœci³y siê w nim wszystkie widoczne komponenty.
        setSize(300,200);				//Wymiary okna
        setLocation(600,300);			//Po³o¿enie lewego górnego rogu okna
        setVisible(true);
    }
}