import javax.swing.*;
import java.io.IOException;

public class ObrazFrame extends JFrame {

    public ObrazFrame() throws IOException {
        super("COVID-19");

        JPanel obrazPanel = new ObrazPanel();
        add(obrazPanel);
        setLocation(500,200);
        pack();
        setVisible(true);
    }
}