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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.log4j.PropertyConfigurator;
import java.io.File;


@SuppressWarnings("serial") // Pozbycie się warningów...

public class WykresPanel extends JPanel implements Serializable {
    static Logger logger = Logger.getLogger(WykresPanel.class);



    private int histogramHeight = 300; // wysokosc histogramu
    private int barWidth = 50; // szerokosc slupków
    private int barGap = 10; // odstep słupkow od siebie

    private JPanel barPanel; // panel wykresu
    private JPanel labelPanel; // dolne podpisy panstw
    private JPanel descriptionPanel; // gorny opis

    // lista słupków
    public List<Bar> bars = new ArrayList<Bar>();

    // konstruktor
    public WykresPanel() {
        String log4jConfigFile = System.getProperty("user.dir") + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
        setBorder(new EmptyBorder(10, 10, 10, 10)); // odstep od zewnetrznej ramki
        setLayout(new BorderLayout());

        // wstepne ustawienia wykresu
        barPanel = new JPanel(new GridLayout(1, 0, barGap, 0)); // stworzenie panelu
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK); // grubosc ramki i kolor
        Border inner = new EmptyBorder(40, 10, 0, 10); // odległosc słupków od ramki
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

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    // metoda odpowiedzialna za rysowanie słupków, górnego i dolnego opisu
    public void layoutHistogram(int option, String[] countries, String[] countriesURL, LocalDateTime DayAndTime) {

        barPanel.removeAll();
        labelPanel.removeAll();
        descriptionPanel.removeAll();

        // w zaleznosci od opcji - inna nazwa gornego opisu wykresu
        JLabel desc;
        if (option == 0) {
            desc = new JLabel("Liczba osob zakażonych w dniu: " + dtf.format(DayAndTime));
        } else if (option == 1) {
            desc = new JLabel("Liczba zgonów w dniu: " + dtf.format(DayAndTime));
        } else {
            desc = new JLabel("Liczba osób wyleczonych w dniu: " + dtf.format(DayAndTime));
        }
        desc.setHorizontalAlignment(JLabel.CENTER); // wysrodkowanie gornego opisu
        desc.setFont(new Font("Serif", Font.BOLD, 20)); // ustawienia czcionki
        descriptionPanel.add(desc); // dodanie labela 'desc' do gornego panelu

        final JButton Reload = new JButton();

        ActionListener MyActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(Reload)) {
                    try {
                        WykresFrame.panel1.bars.clear();
                        WykresFrame.panel2.bars.clear();
                        WykresFrame.panel3.bars.clear();
                        WykresFrame.panel1.barPanel.removeAll();
                        WykresFrame.panel2.barPanel.removeAll();
                        WykresFrame.panel3.barPanel.removeAll();
                        WykresFrame.panel1.labelPanel.removeAll();
                        WykresFrame.panel2.labelPanel.removeAll();
                        WykresFrame.panel3.labelPanel.removeAll();
                        CardPanel.parseList.clear();
                        for(int i=0; i<CardPanel.parseList.size()-1; i++) {
                            System.out.println(CardPanel.parseList.get(i).get(0));
                        }
                        CardPanel.parseList = Parse(countries, countriesURL);
                        logger.info("Ponowne parsowanie informacji do wykresow");
                        WykresFrame.panel1.DrawChart(CardPanel.parseList, countries, countriesURL, 0);
                        logger.info("Rysowanie wykresu zakazonych...");
                        WykresFrame.panel2.DrawChart(CardPanel.parseList, countries, countriesURL, 1);
                        logger.info("Rysowanie wykresu zgonow...");
                        WykresFrame.panel3.DrawChart(CardPanel.parseList, countries, countriesURL, 2);
                        logger.info("Rysowanie wykresu wyleczonych...");

                        return;
                    } catch (IOException exc) {
                        CardPanel.logger.error("Exception --> Can't display reloaded bar chart");
                    }
                }
            }
        };

        ImageIcon ReloadIcon = new ImageIcon("GRAPHICS/reload.png");
        Reload.setIcon(ReloadIcon);
        Reload.setBorderPainted(false);
        Reload.setContentAreaFilled(false);
        Reload.addActionListener(MyActionListener);
        descriptionPanel.add(Reload);

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

    String file = "C:\\Users\\MONIKA\\Desktop\\DodatkoweJavaProjekt\\ParsedData.dat";
    // metoda parsująca
    public ArrayList<ArrayList<String> > Parse(String[] countries, String[] countriesURL) throws MalformedURLException {
        URL url = null;
        this.dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.now = LocalDateTime.now();

        ArrayList<ArrayList<String> > AllData = new ArrayList<ArrayList<String>>();
        for (int i = 1; i < countries.length; i++) {
            try {
                String URLaddress = countriesURL[i];
                url = new URL("https://www.worldometers.info/coronavirus/country/" + URLaddress + "/");
            } catch (MalformedURLException e) {
                logger.error("Problem with URL" + url);
                return null;
            }

            boolean connected = false;
            Connection.Response res = null;
            int j = 1;
            int attempts = 5;

            while (!connected) {
                logger.info("Proba polaczenia z: " + url);

                try {
                    res = Jsoup.connect(url.toString()).referrer("http://www.google.pl").execute();
                    logger.info("Nawiazano polaczenie z: " + url);
                    connected = true;
                } catch (IOException e) {
                    logger.error("Nie mozna polaczyc sie z: " + url);
                }

                if (!connected) {
                    j++;
                    if (j < attempts + 1) {

                        try {
                            Thread.sleep(1000); //Uśpienie wątku na 1 sec
                        } catch (InterruptedException e1) {
                        }
                    } else {
                        logger.fatal("Nie można połączyć się z " + url + "... Zamykanie programu ");
                        return null;
                    }
                }
            }

            logger.info("Parsowanie informacji do wykresow");
            Document doc = null;

            try {
                doc = res.parse();
            } catch (IOException e) {
                logger.fatal("Nie można parsować strony " + url, e);
                return null;
            }
            Elements tmp = doc.select("div.maincounter-number");
            ArrayList<String> Dane1 = new ArrayList<>();

            for (Element e : tmp) {
                Dane1.add(e.getElementsByTag("span").text());
            }
            AllData.add(Dane1);
        }

        for(int i=0; i<AllData.size(); i++) {
            logger.info("Kraj:" + countries[i + 1] + ", Liczba zakazonych:" + AllData.get(i).get(0) + ", Liczba zgonow:" + AllData.get(i).get(1) + ", Liczba uleczonych:" + AllData.get(i).get(2));
        }


        try {
            FileOutputStream fos = new FileOutputStream(file);
            GZIPOutputStream gos = new GZIPOutputStream(fos, true);
            ObjectOutputStream oos = new ObjectOutputStream(gos);

            oos.writeObject(AllData);
            oos.writeObject(now);

            oos.close();
            fos.close();

        }catch(IOException ioe){
            logger.error("IOException - cannot open a file", ioe);

            return null;
        }

        return AllData;
    }

    // w zależności od wybranej opcji (zakażenia/zgony/wyleczeni) rysuje odpowiedni wykres
    public void DrawChart(ArrayList<ArrayList<String> > AllData, String[] countries, String[] countriesURL, int option) {
        ArrayList<ArrayList<String> > pomocniczaLista = new ArrayList<ArrayList<String>>();
        LocalDateTime ReadDate = LocalDateTime.now();

        try{
            FileInputStream fis = new FileInputStream(file);
            GZIPInputStream gis = new GZIPInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(gis);

                Object obj = ois.readObject();

                if (obj instanceof ArrayList<?>) {
                    pomocniczaLista = (ArrayList<ArrayList<String>>) obj;
                } else {
                    logger.info("It is not suitable object");
                    ois.close();
                    fis.close();
                    return;
                }

            obj = ois.readObject();

            if (obj instanceof LocalDateTime) {
                ReadDate = (LocalDateTime) obj;
            } else {
                logger.info("It is not suitable object");
                ois.close();
                fis.close();
                return;
            }

            ois.close();
            fis.close();

        }catch(IOException ioe){
            logger.error("IOException - cannot open a file", ioe);
        }catch(ClassNotFoundException c){
            logger.error("Not found", c);
        }

        for (int i = 0; i < pomocniczaLista.size(); i++) {
            if (option == 0) {
                String pomocnicza = pomocniczaLista.get(i).get(0);
                pomocnicza = pomocnicza.replace(",", "");
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i+1], value, Color.YELLOW);
                this.layoutHistogram(option, countries, countriesURL, ReadDate);
            } else if (option == 1) {
                String pomocnicza = pomocniczaLista.get(i).get(1);
                pomocnicza = pomocnicza.replace(",", "");
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i+1], value, Color.RED);
                this.layoutHistogram(option, countries, countriesURL, ReadDate);
            } else {
                String pomocnicza = pomocniczaLista.get(i).get(2);
                pomocnicza = pomocnicza.replace(",", "");
                if(pomocnicza.contentEquals("N/A")) {continue;}
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i+1], value, Color.GREEN);
                this.layoutHistogram(option, countries, countriesURL, ReadDate);
            }
        }
    }
}