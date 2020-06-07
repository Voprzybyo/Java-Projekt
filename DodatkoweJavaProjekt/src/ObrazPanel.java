import javax.imageio.ImageIO;
import javax.swing.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
@SuppressWarnings("serial") // Pozbycie się warningów...

public class ObrazPanel extends JPanel {

    private BufferedImage image;

    static Logger logger = Logger.getLogger(ObrazPanel.class);

    public ObrazPanel() throws IOException {
        super();

        String log4jConfigFile = System.getProperty("user.dir")		//Ścieżka do pliku log4j.properties
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);			//Wywołanie z wyżej złożoną ścieżką

        /*adres strony ze zdjęciem do pobrania*/
        URL url = new URL("https://ocdn.eu/pulscms-transforms/1/l8Ek9kpTURBXy9lMzcxZTU5ZWJlYzg2ZWQ3ZjFlOGRkODQ5ODYzMDUyOS5qcGeTlQMBAM0PoM0IypMFzQMUzQG8kwmmNzFhMDFlBoGhMAE/wizualizacja-koronawirusa.jpg");
        try {
            logger.info("Załadowanie obrazu");
            image = ImageIO.read(url); 					//Próba odczytu obrazu z podanego url

        } catch (MalformedURLException e) {				//MalformedURLException jest zgłaszany, gdy wbudowana klasa URL napotka niepoprawny adres URL;
            logger.error("ERROR --> Nie udało się pobrać i wyświetlić obrazu"); //Wyłapanie wyjątku w przypadku błędu odczytu ze strony
        }

        /*Dostosowanie wymiarów ramki do wymiarów obrazka*/
        Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
        setPreferredSize(dimension);					//Funkcja z JFrame
    }

    /*Wyświetlenie obrazu z wykorzystaniem biblioteki Graphics2D*/
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;	//Utworzenie obiektu klasy Graphics2D i przypisanie mu zrzutowanego na tę klasę argumentu
        g2d.drawImage(image, 0, 0, this); 	// 0,0 --> pozycje lewego górnego rogu obrazu
    }
}

