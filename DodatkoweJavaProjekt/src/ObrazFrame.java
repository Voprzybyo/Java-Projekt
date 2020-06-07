import javax.swing.*;
import java.io.IOException;
@SuppressWarnings("serial") // Pozbycie się warningów...

public class ObrazFrame extends JFrame {

    public ObrazFrame() throws IOException {
        super("COVID-19");		//Konstruktor klasy nadrzędnej

        JPanel obrazPanel = new ObrazPanel();		//Utworzenie panelu w ramce
        add(obrazPanel);		//Dodanie panelu do Ramki
        setLocation(500,200);	//Położenie lewego górnego rogu okna
        pack();					//Metoda pack() ustala rozmiar okna tak, aby mieściły się w nim wszystkie widoczne komponenty.
        setVisible(true);
    }
}