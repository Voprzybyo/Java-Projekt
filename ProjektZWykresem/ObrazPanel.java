import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class ObrazPanel extends JPanel {

    private BufferedImage image;

    static Logger logger = Logger.getLogger(ObrazPanel.class); 		//

    public ObrazPanel() throws IOException {
        super();

        String log4jConfigFile = System.getProperty("user.dir")		//�cie�ka do pliku log4j.properties
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);			//Wywo�anie z wy�ej z�o�on� �cie�k�

        /*adres strony ze zdj�ciem do pobrania*/
        URL url = new URL("https://ocdn.eu/pulscms-transforms/1/l8Ek9kpTURBXy9lMzcxZTU5ZWJlYzg2ZWQ3ZjFlOGRkODQ5ODYzMDUyOS5qcGeTlQMBAM0PoM0IypMFzQMUzQG8kwmmNzFhMDFlBoGhMAE/wizualizacja-koronawirusa.jpg");
        try {
            logger.debug("Za�adowanie obrazu");
            image = ImageIO.read(url); 					//Pr�ba odczytu obrazu z podanego url

        } catch (MalformedURLException e) {				//MalformedURLException jest zg�aszany, gdy wbudowana klasa URL napotka niepoprawny adres URL;
            logger.error("ERROR --> Nie uda�o si� pobra� i wy�wietli� obrazu"); //Wy�apanie wyj�tku w przypadku b��du odczytu ze strony
        }

        /*Dostosowanie wymiar�w ramki do wymiar�w obrazka*/
        Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
        setPreferredSize(dimension);					//Funkcja z JFrame
    }

    /*Wy�wietlenie obrazu z wykorzystaniem biblioteki Graphics2D*/
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;	//Utworzenie obiektu klasy Graphics2D i przypisanie mu zrzutowanego na t� klas� argumentu
        g2d.drawImage(image, 0, 0, this); 	// 0,0 --> pozycje lewego g�rnego rogu obrazu
    }
}

