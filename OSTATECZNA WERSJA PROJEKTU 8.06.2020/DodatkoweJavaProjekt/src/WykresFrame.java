import org.apache.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

@SuppressWarnings("serial") // Pozbycie się warningów...

public class WykresFrame extends JFrame
{
    static Logger logger = Logger.getLogger(WykresFrame.class);
    public static WykresPanel panel1 = new WykresPanel();
    public static WykresPanel panel2 = new WykresPanel();
    public static WykresPanel panel3 = new WykresPanel();
    // konstruktor
    public WykresFrame(String[] countries, String[] countriesURL) throws IOException {
        super("Statystyki");

        // panel do przełączania
        final JPanel CasesVsDeathVsRecovered = new JPanel(new CardLayout());
        CasesVsDeathVsRecovered.setPreferredSize(new Dimension(1000, 500));

        MouseListener myMouseListener2 = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                CardLayout card = (CardLayout) CasesVsDeathVsRecovered.getLayout();
                card.previous(CasesVsDeathVsRecovered);
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        };


        // 3 osobne panele do wyswietlania osobno 3 wykresow

        panel1.setBackground(Color.WHITE);
        panel1.setPreferredSize(new Dimension(1000, 500));
        panel1.addMouseListener(myMouseListener2);
        CasesVsDeathVsRecovered.add(panel1);

        panel2.setBackground(Color.WHITE);
        panel2.setPreferredSize(new Dimension(1000, 500));
        panel2.addMouseListener(myMouseListener2);
        CasesVsDeathVsRecovered.add(panel2);

        panel3.setBackground(Color.WHITE);
        panel3.setPreferredSize(new Dimension(1000, 500));
        panel3.addMouseListener(myMouseListener2);
        CasesVsDeathVsRecovered.add(panel3);

        this.add(CasesVsDeathVsRecovered);
        setLocationByPlatform(true);
        pack();
        setVisible(true);




        // parsowanie
        if(CardPanel.parseList.size()==0) {

            CardPanel.parseList = panel1.Parse(countries, countriesURL);
        }

        panel1.bars.clear();
        panel2.bars.clear();
        panel3.bars.clear();

        logger.info("Parsowanie informacji do wykresow");
        // rysowanie wykresow
        panel1.DrawChart(CardPanel.parseList, countries, countriesURL, 0);
        logger.info("Rysowanie wykresu zakazonych...");
        panel2.DrawChart(CardPanel.parseList, countries, countriesURL, 1);
        logger.info("Rysowanie wykresu zgonow...");
        panel3.DrawChart(CardPanel.parseList, countries, countriesURL, 2);
        logger.info("Rysowanie wykresu wyleczonych...");
    }
}