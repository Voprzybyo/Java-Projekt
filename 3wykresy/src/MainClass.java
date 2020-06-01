import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;

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
