import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.log4j.Logger;

public class MainClass {
    static Logger logger = Logger.getLogger(MainClass.class);

    public static void main(String[] args) throws IOException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    CardPanel.CreateAndShowGUI();
                } catch (MalformedURLException e) {
                    logger.fatal("Main-GUI error!");
                    e.printStackTrace();
                }
            }
        });

    }
}
