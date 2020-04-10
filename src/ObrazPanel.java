import java.awt.*;
 import java.awt.image.BufferedImage;
 import java.io.File;
 import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
 import javax.swing.JPanel;
 
public class ObrazPanel extends JPanel {
	private BufferedImage image;
	 
 	public ObrazPanel() throws IOException {
 		super();
 
 		URL url = new URL("https://ocdn.eu/pulscms-transforms/1/l8Ek9kpTURBXy9lMzcxZTU5ZWJlYzg2ZWQ3ZjFlOGRkODQ5ODYzMDUyOS5qcGeTlQMBAM0PoM0IypMFzQMUzQG8kwmmNzFhMDFlBoGhMAE/wizualizacja-koronawirusa.jpg"); 
 		try {
 		 image = ImageIO.read(url);
 		} catch (MalformedURLException e) {
 			System.err.println("Blad odczytu obrazka");
 		}
 	 
 		Dimension dimension = new Dimension(image.getWidth(), image.getHeight());
 		setPreferredSize(dimension);
 	}
 
 	@Override
 	public void paintComponent(Graphics g) {
 		Graphics2D g2d = (Graphics2D) g;
 		g2d.drawImage(image, 0, 0, this);
 	}
 }

