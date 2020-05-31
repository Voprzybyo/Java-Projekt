import javax.swing.*;
import java.io.IOException;

public class WykresFrame extends JFrame
{
    public WykresFrame(String[] countries, String[] countriesURL) throws IOException {
        super("Bar Chart");
        //JFrame frame = new JFrame("Histogram Panel");
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WykresPanel panel = new WykresPanel();
        add(panel);
        setLocationByPlatform(true);
        pack();
        setVisible(true);
        panel.createAndShowGUI(countries, countriesURL);
    }

}