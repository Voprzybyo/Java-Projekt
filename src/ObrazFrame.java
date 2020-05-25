import javax.swing.*;
import java.io.IOException;
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class ObrazFrame extends JFrame {

    public ObrazFrame() throws IOException {
        super("COVID-19");		//Konstruktor klasy nadrz�dnej

        JPanel obrazPanel = new ObrazPanel();
        add(obrazPanel);
        setLocation(500,200);
        pack();					//Metoda pack() ustala rozmiar okna tak, aby mie�ci�y si� w nim wszystkie widoczne komponenty.
        setVisible(true);
    }
}