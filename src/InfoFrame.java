import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InfoFrame extends JFrame  {

    private static JDialog d;
    private static String s;
    static Logger logger = Logger.getLogger(InfoFrame.class);

    public InfoFrame() {
        super("Ramka informacyjna");

        JPanel buttonPanel = new ButtonPanel();
        add(buttonPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        String log4jConfigFile = System.getProperty("user.dir")
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
        logger.debug("this is a debug log message");
    }

    public static void RamkaInformacyjnaPolska() {

        JFrame jf = new JFrame("POLSKA STATS");
        jf.setLayout(new FlowLayout());
        JPanel panel = new JPanel();
        jf.add(panel);


        s = "<html>Statystyki w Polsce<br><br>";
        s += "ZAKAŻENIA: " + MainClass.Dane1.get(0) + "<br>";
        s += "ŚMIERCI: " + MainClass.Dane1.get(1) + "<br>";
        s += "WYLECZONYCH: " + MainClass.Dane1.get(2) + "<br></html>\"";

        JLabel tekst = new JLabel(s);
        panel.add(tekst);
        panel.setSize(200,200);

        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));

        jf.setLocation(600,400);
        jf.pack();
        jf.setVisible(true);
    }

    public static void RamkaInformacyjnaSwiat() {
        JFrame jf = new JFrame("ŚWIAT STATS");
        jf.setLayout(new FlowLayout());
        JPanel panel = new JPanel();
        jf.add(panel);


        s = "<html>Statystyki na świecie<br><br>";
        s += "ZAKAŻENIA: " + MainClass.Dane2.get(0) + "<br>";
        s += "ŚMIERCI: " + MainClass.Dane2.get(1)+ "<br>";
        s += "WYLECZONYCH: " + MainClass.Dane2.get(2) + "<br></html>\"";

        JLabel tekst = new JLabel(s);
        panel.add(tekst);
        panel.setSize(200,200);

        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));

        jf.setLocation(600,400);
        jf.pack();
        jf.setVisible(true);
    }

}
