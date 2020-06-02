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
import java.util.List;

public class WykresPanel extends JPanel {
    static Logger logger = Logger.getLogger(WykresPanel.class);
    private int histogramHeight = 300; // wysokosc histogramu
    private int barWidth = 50; // szerokosc slupków
    private int barGap = 10; // odstep słupkow od siebie


    private JPanel barPanel; // panel wykresu
    private JPanel labelPanel; // dolne podpisy panstw
    private JPanel descriptionPanel; // gorny opis

    // lista słupków
    private List<Bar> bars = new ArrayList<Bar>();

    // konstruktor
    public WykresPanel() {

        setBorder(new EmptyBorder(10, 10, 10, 10)); // odstep od zewnetrznej ramki
        setLayout(new BorderLayout());

        // wstepne ustawienia wykresu
        barPanel = new JPanel(new GridLayout(1, 0, barGap, 0)); // stworzenie panelu
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK); // grubosc ramki i kolor
        Border inner = new EmptyBorder(80, 10, 0, 10); // odległosc słupków od ramki
        Border compound = new CompoundBorder(outer, inner); // zewnetrzna granica i wewnetrzna granica
        barPanel.setBorder(compound); // dodanie tych ustawien do barPanel
        barPanel.setBackground(Color.WHITE);

        // wstepne ustawienia dolnych podpisow
        labelPanel = new JPanel(new GridLayout(1, 0, barGap, 0)); // stworzenie panelu
        labelPanel.setBorder(new EmptyBorder(5, 10, 0, 10)); // wstepne ustawienia
        labelPanel.setBackground(Color.WHITE);

        // wstepne ustawienia gornego opisu wykresu
        descriptionPanel = new JPanel(new GridLayout()); // stworzenie panelu
        descriptionPanel.setBorder(new EmptyBorder(5, 10, 10, 10)); //  wstepne ustawienia
        descriptionPanel.setBackground(Color.WHITE);

        // dodanie 3 paneli
        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
        add(descriptionPanel, BorderLayout.PAGE_START);
    }

    //funkcja tworzaca słupek (konkretny obiekt) o zadanym podpisie, wysokosci i kolorze
    public void addHistogramColumn(String label, int value, Color color) {
        Bar bar = new Bar(label, value, color);
        bars.add(bar); // dodanie slupka do listy słupków
    }

    // metoda odpowiedzialna za rysowanie słupków, górnego i dolnego opisu
    public void layoutHistogram(int option) {
        barPanel.removeAll();
        labelPanel.removeAll();
        descriptionPanel.removeAll();

        // w zaleznosci od opcji - inna nazwa gornego opisu wykresu
        JLabel desc;
        if (option == 0) {
            desc = new JLabel("Ilość osob zakażonych");
        } else if (option == 1) {
            desc = new JLabel("Ilość zgonów");
        } else {
            desc = new JLabel("Ilość osób wyleczonych");
        }
        desc.setHorizontalAlignment(JLabel.CENTER); // wysrodkowanie gornego opisu
        desc.setFont(new Font("Serif", Font.BOLD, 25)); // ustawienia czcionki
        descriptionPanel.add(desc); // dodanie labela 'desc' do gornego panelu

        int maxValue = 0;

        for (Bar bar : bars)
            maxValue = Math.max(maxValue, bar.getValue()); // szukanie najwyzszego slupka

        for (Bar bar : bars) {
            JLabel label = new JLabel(bar.getValue() + "");
            label.setHorizontalTextPosition(JLabel.CENTER); // pozioma pozycja tekstu
            label.setHorizontalAlignment(JLabel.CENTER); // poziome wyrownanie
            label.setVerticalTextPosition(JLabel.TOP); // pionowa pozycja tekstu
            label.setVerticalAlignment(JLabel.BOTTOM); // pionowe wyrownanie
            int barHeight = (bar.getValue() * histogramHeight) / maxValue; // dopasowanie rysowanej wysokosci slupka do ramki(histogramu)
            Icon icon = new ColorIcon(bar.getColor(), barWidth, barHeight); // stworzenie obiektu słupka (rysowanego)
            label.setIcon(icon);
            barPanel.add(label);

            JLabel barLabel = new JLabel(bar.getLabel());
            barLabel.setHorizontalAlignment(JLabel.CENTER);
            labelPanel.add(barLabel);
        }
    }

    // stworzenie typu Bar - obiektu posiadającego label, wartosc i kolor
    private class Bar {
        private String label;
        private int value;
        private Color color;

        // konstruktor
        public Bar(String label, int value, Color color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }

        // gettery
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

    // odpowiada konkretnie za rysowanie prostokątów i cienia, czyli całych słupków
    private class ColorIcon implements Icon {
        private int shadow = 6;

        private Color color;
        private int width;
        private int height;

        // konstruktor
        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        // gettery
        public int getIconWidth() {
            return width;
        }

        public int getIconHeight() {
            return height;
        }

        // metoda odpowiadajaca za rysowanie wykresu - słupków
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, width - shadow, height);
            g.setColor(Color.GRAY);
            g.fillRect(x + width - shadow, y + shadow, shadow, height - shadow);
        }
    }

    // metoda parsująca
    public ArrayList<ArrayList<String> > Parse(String[] countries, String[] countriesURL) throws MalformedURLException {
        URL url = null;
        ArrayList<ArrayList<String> > AllData = new ArrayList<ArrayList<String>>();
        for (int i = 1; i < countries.length; i++) {
            try {
                String URLaddress = countriesURL[i];
                url = new URL("https://www.worldometers.info/coronavirus/country/" + URLaddress + "/");
            } catch (MalformedURLException e) {
                logger.error("Problem with URL" + url, e);
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
                } catch (IOException e) {
                    logger.error("Nie mozna polaczyc sie z: " + url);
                }

                if (!connected) {
                    j++;
                    if (j < attempts + 1) {

                        try {
                            Thread.sleep(1000); //U�pienie w�tku na 1 sec
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        logger.fatal("Nie mo�na po��czy� si� z " + url + "... Zamykanie programu ");
                        return null;
                    }
                }
            }

            logger.info("Parsowanie informacji do wykresow");
            Document doc = null;

            try {
                doc = res.parse();
            } catch (IOException e) {
                logger.fatal("Nie mo�na parsowa� strony " + url, e);
                return null;
            }

            Elements tmp = doc.select("div.maincounter-number");
            ArrayList<String> Dane1 = new ArrayList<>();

            for (Element e : tmp) {
                Dane1.add(e.getElementsByTag("span").text());
            }
            AllData.add(Dane1);
        }

        for(int i=0; i<AllData.size(); i++)
        {
            logger.info("Kraj:" + countries[i+1] +", Liczba zakazonych:" + AllData.get(i).get(0) +", Liczba zgonow:" + AllData.get(i).get(1)+", Liczba uleczonych:" + AllData.get(i).get(2));
        }
        return AllData;
    }

    // w zależności od wybranej opcji (zakażenia/zgony/wyleczeni) rysuje odpowiedni wykres
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