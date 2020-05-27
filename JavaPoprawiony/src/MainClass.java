import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

public class MainClass {


    public static void main(String[] args) throws IOException {
    	UIManager.put("Combobox.selectionForeground", new ColorUIResource(Color.YELLOW));
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					CardPanel.CreateAndShowGUI();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
            }
        });
  
    }
}
