import javax.swing.*;
import java.io.IOException;
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class ObrazFrame extends JFrame {

    public ObrazFrame() throws IOException {
        super("COVID-19");		//Konstruktor klasy nadrz�dnej

        JPanel obrazPanel = new ObrazPanel();		//Utworzenie panelu w ramce
        add(obrazPanel);		//Dodanie panelu do Ramki
        setLocation(500,200);	//Po�o�enie lewego g�rnego rogu okna
        pack();					//Metoda pack() ustala rozmiar okna tak, aby mie�ci�y si� w nim wszystkie widoczne komponenty.
        setVisible(true);
    }
}