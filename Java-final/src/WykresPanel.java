import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial") // Pozbycie siê warningów...

public class WykresPanel extends JPanel {
    static Logger logger = Logger.getLogger(WykresPanel.class);
    private int histogramHeight = 300;
    private int barWidth = 50;
    private int barGap = 10;


    private JPanel barPanel; // s³upki
    private JPanel labelPanel; // dolne podpisy panstw
    private JPanel descriptionPanel; // gorny opis


    private List<Bar> bars = new ArrayList<Bar>();

    public WykresPanel() {
        setBorder(new EmptyBorder(10, 10, 10, 10)); // ramka czarna
        setLayout(new BorderLayout());

        // wstepne ustawienia wykresu
        barPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK); // grubosc ramki i kolor
        Border inner = new EmptyBorder(80, 10, 0, 10); // odleg³osc s³upków od ramki
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder(compound);
        barPanel.setBackground(Color.WHITE);

        // wstepne ustawienia dolnych podpisow
        labelPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        labelPanel.setBorder(new EmptyBorder(5, 10, 0, 10));
        labelPanel.setBackground(Color.WHITE);

        // wstepne ustawienia gornego opisu wykresu
        descriptionPanel = new JPanel(new GridLayout());
        descriptionPanel.setBorder(new EmptyBorder(5, 10, 10, 10));
        descriptionPanel.setBackground(Color.WHITE);

        // dodanie 3 paneli
        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
        add(descriptionPanel, BorderLayout.PAGE_START);
    }

    public void addHistogramColumn(String label, int value, Color color) {
        Bar bar = new Bar(label, value, color);
        bars.add(bar);
    }

    // metoda odpowiedzialna za rysowanie s³upków, górnego i dolnego opisu
    public void layoutHistogram(int option) {
        barPanel.removeAll();
        labelPanel.removeAll();
        descriptionPanel.removeAll();
        JLabel desc;
        if (option == 0) {
            desc = new JLabel("Liczba osob zaka¿onych");
        } else if (option == 1) {
            desc = new JLabel("Liczba zgonów");
        } else {
            desc = new JLabel("Liczba osób wyleczonych");
        }
        desc.setHorizontalAlignment(JLabel.CENTER);
        desc.setFont(new Font("Serif", Font.BOLD, 25));
        descriptionPanel.add(desc);

        int maxValue = 0;

        for (Bar bar : bars)
            maxValue = Math.max(maxValue, bar.getValue());

        for (Bar bar : bars) {
            JLabel label = new JLabel(bar.getValue() + "");
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.TOP);
            label.setVerticalAlignment(JLabel.BOTTOM);
            int barHeight = (bar.getValue() * histogramHeight) / maxValue;
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight);
            label.setIcon(icon);
            barPanel.add(label);

            JLabel barLabel = new JLabel(bar.getLabel());
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            labelPanel.add(barLabel);
        }
    }

    // stworzenie typu Bar
    private class Bar {
        private String label;
        private int value;
        private Color color;

        public Bar(String label, int value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public int getValue() {
            return value;
        }

        public Color getColor() {
            return color;
        }
    }

    // odpowiada tez za rysowanie prostok¹tów i cienia, czyli ca³ych s³upków
    private class ColorIcon implements Icon {
        private int shadow = 6;

        private Color color;
        private int width;
        private int height;

        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shadow, y + shadow, shadow, height - shadow);
        }
    }

    // metoda parsuj¹ca
    public ArrayList<ArrayList<String> > Parse(String[] countries, String[] countriesURL) throws MalformedURLException {
        URL url = null;
        ArrayList<ArrayList<String> > AllData = new ArrayList<ArrayList<String>>();
        for (int i = 1; i < countries.length; i++) {
            try {
                String var10002 = countriesURL[i];
                url = new URL("https://www.worldometers.info/coronavirus/country/" + var10002 + "/");
            } catch (MalformedURLException var15) {
                logger.error("Problem with URL" + url, var15);
                return null;
            }

            boolean connected = false;
            Connection.Response res = null;
            int j = 0;
            byte attempts = 5;

            while (!connected) {
                logger.info("Proba polaczenia z: " + url);

                try {
                    res = Jsoup.connect(url.toString()).execute();
                    logger.info("Nawiazano polaczenie z: " + url);
                    connected = true;
                } catch (IOException var14) {
                    logger.error("Nie mozna polaczyc sie z: " + url);
                }

                if (!connected) {
                    if (j >= attempts) {
                        logger.fatal("Nie mozna po³¹czyæ siê z " + url + "... Zamykanie programu ");
                        return null;
                    }

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException var13) {
                    }
                }
            }

            logger.info("Parsowanie informacji do wykresow");
            Document doc = null;

            try {
                doc = res.parse();
            } catch (IOException var12) {
                logger.fatal("Nie mo¿na parsowaæ strony " + url, var12);
                return null;
            }
            Elements tmp = doc.select("div.maincounter-number");
            ArrayList<String> Dane1 = new ArrayList();
            Iterator var10 = tmp.iterator();
            while (var10.hasNext()) {
                Element e = (Element) var10.next();
                Dane1.add(e.getElementsByTag("span").text());
            }
            AllData.add(Dane1);
        }

        for(int i=0; i<AllData.size(); i++)
        {
            System.out.println(AllData.get(i).get(1));
        }
        return AllData;
    }

    // w zale¿noœci od wybranej opcji (zaka¿enia/zgony/wyleczeni) rysuje odpowiedni wykres
    public void DrawChart(ArrayList<ArrayList<String> > AllData, String[] countries, String[] countriesURL, int option) {

        for (int i = 0; i < AllData.size(); i++) {
            if (option == 0) {
                String pomocnicza = AllData.get(i).get(0);
                pomocnicza = pomocnicza.replace(",", "");
                //System.out.println(pomocnicza);
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i+1], value, Color.YELLOW);
                this.layoutHistogram(option);
            } else if (option == 1) {
                String pomocnicza = AllData.get(i).get(1);
                pomocnicza = pomocnicza.replace(",", "");
                //System.out.println(pomocnicza);
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i+1], value, Color.RED);
                this.layoutHistogram(option);
            } else {
                String pomocnicza = AllData.get(i).get(2);
                pomocnicza = pomocnicza.replace(",", "");
                System.out.println(pomocnicza);
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i+1], value, Color.GREEN);
                this.layoutHistogram(option);
            }
        }
    }
}