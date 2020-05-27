import javax.swing.*;
import java.io.IOException;
@SuppressWarnings("serial") // Pozbycie siê warningów...

public class ObrazFrame extends JFrame {

    public ObrazFrame() throws IOException {
        super("COVID-19");		//Konstruktor klasy nadrzêdnej

        JPanel obrazPanel = new ObrazPanel();		//Utworzenie panelu w ramce
        add(obrazPanel);		//Dodanie panelu do Ramki
        setLocation(500,200);	//Po³o¿enie lewego górnego rogu okna
        pack();					//Metoda pack() ustala rozmiar okna tak, aby mieœci³y siê w nim wszystkie widoczne komponenty.
        setVisible(true);
    }
}