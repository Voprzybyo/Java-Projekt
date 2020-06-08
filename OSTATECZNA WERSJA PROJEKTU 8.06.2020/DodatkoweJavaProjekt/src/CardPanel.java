import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

@SuppressWarnings("serial") // Pozbycie się warningów...

public class CardPanel extends JPanel {

    static Logger logger = Logger.getLogger(CardPanel.class);


    /* Rozmiar okna */
    private final static int Width = 320;
    private final static int Height = 440;

    private final static int WORLD = 0;

    private final static String[] countries = { "Świat", "Niemcy", "Rosja", "Polska", "Słowacja", "USA", "Brazylia",
            "Hiszpania", "UK", "Wlochy", "Francja", "Turcja", "Indie", "Iran", "Kanada", "Chiny", "Chile" };

    private final static String[] countriesURL = { "world", "germany", "russia", "poland", "slovakia", "us", "brazil", "spain",
            "uk", "italy", "france", "turkey", "india", "iran", "canada", "china", "chile" };

    public static ArrayList<ArrayList<String>> parseList = new ArrayList<ArrayList<String> >();

    private static String[] COVIDInfo = new String[countries.length];

    private static Icon[] Flags = new Icon[countries.length]; 	//Tablica z flagami

    /*Tworzymy Comboboxa, któremu w konstruktorze przekazujemy tablicę z nazwami Państw*/
    final static JComboBox<String> roll = new JComboBox<String>(countries);
    private static JLabel detailedInfoLabel = new JLabel("Parsowanie...");
    private static ExecutorService pool = Executors.newFixedThreadPool(4); //Wielowątkowość do obsługi wywołań

    public static void CreateAndShowGUI() throws MalformedURLException {

        /*Utworzenie ramki głównej --> W konstruktorze napis w belce*/
        JFrame f = new JFrame("COVID-19");
        String log4jConfigFile = System.getProperty("user.dir") + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);

        //final, bo wszystko wewnątrz (takie elementy, które nie zmienią powiązania)
        final JPanel flagVsStats = new JPanel(new CardLayout());	//Panel przełączający między flagami a statystykami
        final JButton NextButton = new JButton();					//NASTĘPNY
        final JButton PreviousButton = new JButton();				//POPRZEDNI
        final JButton PicFromURL = new JButton();					//Przycisk pobierający grafike z URL
        final JButton MainTitle = new JButton();					//Nagłówek
        final JButton BarChart = new JButton();

        /* ACTION LISTENER --> odpowiada za obsługę zdarzeń*/
        ActionListener MyActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                int i = roll.getSelectedIndex();	//Aby nie przekroczyć zakresu --> przypisujemy do 'i' ten element, który obecnie jest zaznaczony na roll'u

                /*MAIN TITLE LISTENER --> Powrót do statystyk ze świata*/
                if (e.getSource().equals(MainTitle)) {					//equals() porównuje 2 teksty! Możliwe tylko z "final"!
                    roll.setSelectedIndex(WORLD);
                    return;
                }

                /*MAIN PICTURE LISTENER --> Wyświetlenie grafiki z URL*/
                if (e.getSource().equals(PicFromURL)) {

                    try {
                        new ObrazFrame();
                        return;

                    } catch (IOException exc) {
                        logger.error("Exception --> Can't display pic from URL");
                    }
                }

                /*BAR PIC LISTENER --> Wyświetlenie wykresu słupkowego*/
                if(e.getSource().equals(BarChart)){
                    try {
                        new WykresFrame(countries, countriesURL);
                        return;
                    } catch (IOException exc) {
                        CardPanel.logger.error("Exception --> Can't display bar chart");
                    }
                }

                /*NEXT BUTTON LISTENER*/
                if (e.getSource().equals(NextButton)) {
                    i++;
                    if (i >= countries.length) {i = 0;} // Aby nie przekroczyć zakresu "zapętlono" liste na początek
                    roll.setSelectedIndex(i);
                    logger.trace("Pressed Next --> " + i);
                    return;
                }

                /*PREVIOUS BUTTON LISTENER*/
                if (e.getSource().equals(PreviousButton)) {
                    i--;
                    if (i < 0) {i = countries.length - 1;}
                    roll.setSelectedIndex(i);
                    logger.trace("Pressed Previous --> " + i);
                    return;
                }
                logger.error("There is no action listener for that button!!!");
            }
        };


        MouseListener myMouseListener = new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            /*Jeżli użytkownik naciśnie na panel to przełączy na następną kartę*/
            public void mousePressed(MouseEvent e) {
                CardLayout cardlay = (CardLayout) flagVsStats.getLayout();		//getLayout() zwraca LayoutManager (klase nadrzędną), więc rzutujemy na CardLayout
                cardlay.previous(flagVsStats);									//Przełączenie pomiędzy 2 kartami (flaga i statystyki)
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        };

        /*Grafiki*/
        ImageIcon PicFromUrlIcon = new ImageIcon("GRAPHICS/cor3.png");
        ImageIcon TitleIcon = new ImageIcon("GRAPHICS/title2.png");
        ImageIcon PreviousIcon = new ImageIcon("GRAPHICS/left4.png");
        ImageIcon NextIcon = new ImageIcon("GRAPHICS/right4.png");
        ImageIcon BarChartIcon = new ImageIcon("GRAPHICS/wykr3.png");

        /*Przypisanie grafik do przycisków*/
        NextButton.setIcon(NextIcon);
        PreviousButton.setIcon(PreviousIcon);
        PicFromURL.setIcon(PicFromUrlIcon);
        MainTitle.setIcon(TitleIcon);
        BarChart.setIcon(BarChartIcon);

        /*Właściwości poszczególnych przycisków --> wypełnienie tła, usunięcie obramowań*/
        NextButton.setBorderPainted(false);
        NextButton.setContentAreaFilled(false);
        NextButton.addActionListener(MyActionListener);

        PreviousButton.setBorderPainted(false);
        PreviousButton.setContentAreaFilled(false);
        PreviousButton.addActionListener(MyActionListener);

        PicFromURL.setBorderPainted(false);
        PicFromURL.setContentAreaFilled(false);
        PicFromURL.addActionListener(MyActionListener);

        MainTitle.setBorderPainted(false);
        MainTitle.setContentAreaFilled(false);
        MainTitle.setMargin(new Insets(0, 50, 20, 0));
        MainTitle.addActionListener(MyActionListener);

        BarChart.setBorderPainted(false);
        BarChart.setContentAreaFilled(false);
        BarChart.addActionListener(MyActionListener);

        roll.setSelectedIndex(WORLD);
        roll.setBackground(Color.WHITE);
        roll.setForeground(Color.BLACK);
        roll.setFont(new Font("Helvetica", Font.BOLD, 25));


        /*Panel tytułowy*/
        JPanel MainPanel = new JPanel(new GridLayout());
        MainPanel.setBackground(Color.DARK_GRAY);
        MainPanel.add(MainTitle);
        MainPanel.add(PicFromURL);
        MainPanel.add(BarChart);

        /*Panel sterujacy*/
        JPanel SwitchPanel = new JPanel(new GridLayout());
        SwitchPanel.setBackground(Color.DARK_GRAY);
        SwitchPanel.add(PreviousButton);
        SwitchPanel.add(roll);
        SwitchPanel.add(NextButton);

        /*Obramowanie SwitchPanelu*/
        SwitchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, "SwitchPanel", TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, null, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));


        /*Główny panel*/
        /*Flagi*/
        JPanel FlagPanel = new JPanel(new BorderLayout());
        FlagPanel.setBackground(Color.DARK_GRAY);
        FlagPanel.setPreferredSize(new Dimension(Height, Width));

        final JLabel FlagLabel = new JLabel(SetFlag(WORLD));
        FlagPanel.add(FlagLabel);									//Dodanie FlagLabela do FlagPanelu
        FlagPanel.addMouseListener(myMouseListener);

        /*Statystyki*/
        JPanel StatsPanel = new JPanel(new GridLayout()); //new GridLayout powoduje, że info nie wyświetla się centralnie na środku
        StatsPanel.setPreferredSize(new Dimension(Height, Width));
        StatsPanel.setBackground(Color.DARK_GRAY);
        StatsPanel.setForeground(Color.WHITE);
        StatsPanel.addMouseListener(myMouseListener);

        StatsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(null, "Statystyki", TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION, null, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 50, 10, 0)));

        detailedInfoLabel.setForeground(Color.WHITE);
        StatsPanel.add(detailedInfoLabel);					//Dodanie informacji do panelu ze statystykami

        /*Wielowątkowość*/
        /*pool istnieje --> submit --> przekazujemy obiekt, który jest instancją klasy implementującej interface Runnable*/
        pool.submit(new Runnable() {
            public void run() {
                SetStats(WORLD);
            }
        });

        ItemListener myItemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) { //przekazujemy obiekt, który może być ustawiony jako wnętrze JLabel --> OBRAZ

                /*Warunek zapobiegający podwójnemu wywoływniu tej metody po kliknięciu np. nextButton --> Interesuje nas tylko stan SELECTED (bez DESELECTED)*/
                if (e.getStateChange() == ItemEvent.SELECTED) {

                    FlagLabel.setIcon(SetFlag(roll.getSelectedIndex()));	//Ustawienie flagi odpowiadającej obecnej wartości na roll'u

                    pool.submit(new Runnable() {
                        public void run() {
                            SetStats(roll.getSelectedIndex());		//W momencie zmiany pobieramy info o statystykach
                        }
                    });
                }
            }
        };


        roll.addItemListener(myItemListener);		//Dodanie ItemListenera do Comboboxa

        /*Dodanie kart z flagami i ze statystykami*/
        flagVsStats.add(FlagPanel);
        flagVsStats.add(StatsPanel);

        /*Upakowanie wszystkiego w głównym oknie*/
        f.add(MainPanel, BorderLayout.NORTH);
        f.add(SwitchPanel, BorderLayout.CENTER);
        f.add(flagVsStats, BorderLayout.SOUTH);
        f.pack();
        f.setSize(new Dimension(1000, 620));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

    }

    /*Ustawienie flag*/
    private static Icon SetFlag(int value) {

        /* Aby każdorazowo nie były woływane metody związane ze zmianą rozmiaru to...
         * Jeżeli w tej tablicy coś już jest, to zwrazana jest ta wartość(flaga)*/
        if (Flags[value] != null) {
            return Flags[value];

            /*Jeżeli nie ma, to szukamy ją w folderze*/
        }else {
            String tmp = "FLAGS/" + countries[value].toLowerCase() + ".png";	//Ścieżka do flagi w .png

            ImageIcon tmpImage = new ImageIcon(tmp); //Stworzenie ImageIcon używając konstruktora ze ścieżką do obrazu

            /*Dzielnik modyfikujący wielkość flagi --> wyznaczamy większą z tych wartości, żeby obraz mógł się zmieścić*/
            float dzielnik = Math.max(tmpImage.getIconHeight() / (float) Height, tmpImage.getIconWidth() / (float) Width);

            /*Dostosowane rozmiaru flagi*/
            int NewHeight = (int) (0.5 + tmpImage.getIconHeight() / dzielnik);
            int NewWidth = (int) (0.5 + tmpImage.getIconWidth() / dzielnik);

            /*Tworzymy nowy obiekt, w którym będzie przeskalowana flaga
             * getScaledInstance() --> metoda pozwalająca przeskalować obraz używając określonych rozmiarów*/
            ImageIcon finalImage = new ImageIcon(tmpImage.getImage().getScaledInstance(NewWidth, NewHeight, Image.SCALE_SMOOTH));

            Flags[value] = finalImage;	//Wkładamy przeskalowany obraz do tablicy z flagami
            return finalImage;
        }
    }

    /*Ustawienie info o statystykach w danym kraju*/
    synchronized private static void SetStats(int val) {

        /*Jeżeli opis już istnieje, to zostaje ustawiony*/
        if (COVIDInfo[val] != null) {
            detailedInfoLabel.setText(COVIDInfo[val]);
            return;
        }

        /*Jeżeli nie...*/
        String tmpInfoCountry = "<html>\n";
        URL url = null;

        /*Próba połączenia*/
        try {
            if (countriesURL[val] != "world") {
                url = new URL("https://www.worldometers.info/coronavirus/country/" + countriesURL[val] + "/");
            } else {
                url = new URL("https://www.worldometers.info/coronavirus/");
            }
        } catch (MalformedURLException e) {
            logger.error("Problem with URL" + url);
            return;
        }

        boolean connected_flag = false;
        Response res = null;
        int i = 1;
        int attempts = 5;

        while(connected_flag != true) {
            logger.info(i + " proba połączenia z: " + url);

            try {
                res = Jsoup.connect(url.toString()).referrer("http://www.google.pl").execute();
                logger.info("Nawiązano połączenie z: " + url);
                connected_flag = true;
            } catch (IOException e) {
                logger.error("Nie można połączyć się z: " + url);
            }


            if (!connected_flag) {
                i++;
                if (i < attempts + 1) {

                    try {
                        Thread.sleep(1000); //Uśpienie wątku na 1 sec
                    } catch (InterruptedException exc) {
                    }
                } else {
                    logger.fatal("Nie można połączyć się z " + url + "... Zamykanie programu ");
                    return;
                }
            }
        }

        logger.info("Parsowanie...");

        Document doc = null;

        try {
            doc = res.parse();
        } catch (IOException e) {
            logger.fatal("Nie można parsować strony " + url);
            return;
        }

        Elements tmp = doc.select("div.maincounter-number");
        ArrayList<String> Dane1 = new ArrayList<>();

        for (Element e : tmp) {
            Dane1.add(e.getElementsByTag("span").text());
        }

        tmpInfoCountry += ("<P><B><font size=+4>ZAKAŻENIA: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(0) + "</font></P><BR>\n");
        tmpInfoCountry += ("<P><B><font size=+4>ZGONY: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(1) + "</font></P><BR>\n");
        tmpInfoCountry += ("<P><B><font size=+4>WYLECZONYCH: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(2) + "</font></P><BR>\n");
        tmpInfoCountry += "</html>";

        /*Wpisanie statystyk do tablicy ze statystykami*/
        COVIDInfo[val] = tmpInfoCountry;

        /*Warunek gwarantujący, że przy szybkim przełączaniu tekst nie będzie przeskakiwał --> Upewnienie się, że statystyki pochodzą z danego kraju*/
        if (roll.getSelectedIndex() == val) {
            detailedInfoLabel.setText(tmpInfoCountry);	//ustawienie tesktu w labelu ze statystykami
        } else {
            logger.fatal("Dane się zmieniły!");
        }
    }

}