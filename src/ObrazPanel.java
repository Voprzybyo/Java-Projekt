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
@SuppressWarnings("serial") // Pozbycie siê warningów...

public class ObrazPanel extends JPanel {
	
    private BufferedImage image;

    static Logger logger = Logger.getLogger(ObrazPanel.class);
    
    public ObrazPanel() throws IOException {
        super();													

        String log4jConfigFile = System.getProperty("user.dir")		//Œcie¿ka do pliku log4j.properties
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);			//Wywo³anie z wy¿ej z³o¿on¹ œcie¿k¹
        
        /*adres strony ze zdjêciem do pobrania*/
        URL url = new URL("https://ocdn.eu/pulscms-transforms/1/l8Ek9kpTURBXy9lMzcxZTU5ZWJlYzg2ZWQ3ZjFlOGRkODQ5ODYzMDUyOS5qcGeTlQMBAM0PoM0IypMFzQMUzQG8kwmmNzFhMDFlBoGhMAE/wizualizacja-koronawirusa.jpg");
        try {
        	logger.debug("Za³adowanie obrazu");
            image = ImageIO.read(url); 					//Próba odczytu obrazu z podanego url
            
        } catch (MalformedURLException e) {				//MalformedURLException jest zg³aszany, gdy wbudowana klasa URL napotka niepoprawny adres URL;
        	logger.error("ERROR --> Nie uda³o siê pobraæ i wyœwietliæ obrazu"); //Wy³apanie wyj¹tku w przypadku b³êdu odczytu ze strony
        }
        
        /*Dostosowanie wymiarów ramki do wymiarów obrazka*/
        Dimension dimension = new Dimension(image.getWidth(), image.getHeight()); 
        setPreferredSize(dimension);					//Funkcja z JFrame
    }

    /*Wyœwietlenie obrazu z wykorzystaniem biblioteki Graphics2D*/
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;	//Utworzenie obiektu klasy Graphics2D i przypisanie mu zrzutowanego na tê klasê argumentu
        g2d.drawImage(image, 0, 0, this); 	// 0,0 --> pozycje lewego górnego rogu obrazu
    }
}

