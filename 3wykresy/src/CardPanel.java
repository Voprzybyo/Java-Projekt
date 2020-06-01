//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardPanel extends JPanel {
    static Logger logger = Logger.getLogger(CardPanel.class);
    private static final int Width = 320;
    private static final int Height = 440;
    private static final int WORLD = 0;
    private static final String[] countries = new String[]{"Swiat", "Niemcy", "Rosja", "Polska", "Slowacja", "USA", "Brazylia", "Hiszpania", "Wlochy", "Francja", "Turcja", "Indie", "Iran", "Kanada", "Chiny", "Chile"};
    private static final String[] countriesURL = new String[]{"world", "germany", "russia", "poland", "slovakia", "us", "brazil", "spain", "italy", "france", "turkey", "india", "iran", "canada", "china", "chile"};
    private static String[] COVIDInfo;
    private static Icon[] Flags;
    static final JComboBox<String> roll;
    private static JLabel detailedInfo;
    private static ExecutorService pool;

    //public CardPanel() { }

    // tworzenie GUI
    public static void CreateAndShowGUI() throws MalformedURLException {
        JFrame f = new JFrame("COVID-19"); //nazwa białej ramki
        final JPanel flagVsStats = new JPanel(new CardLayout()); // przycisk flaga/staty
        final JButton NextButton = new JButton(); // szczalka w prawo
        final JButton PreviousButton = new JButton(); //szczalka w lewo
        final JButton PicFromURL = new JButton(); // przycisk pokazujacy zdjecie covid
        final JButton MainTitle = new JButton(); // przycisk napis covid
        final JButton BarChart = new JButton();

        // przewijanie za pomocą przycisków next i previous
        ActionListener MyActionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = CardPanel.roll.getSelectedIndex();
                if (e.getSource().equals(MainTitle)) {
                    CardPanel.roll.setSelectedIndex(0);
                } else {
                    if (e.getSource().equals(PicFromURL)) {
                        try {
                            new ObrazFrame();
                        } catch (IOException var4) {
                            CardPanel.logger.error("Exception --> Can't display pic from URL");
                        }
                    }

//////////////////////////////////////////////////////////////////////////////////
                    if(e.getSource().equals(BarChart)){
                        try {
                            new WykresFrame(countries, countriesURL);
                        } catch (IOException var5) {
                            CardPanel.logger.error("Exception --> Can't display bar chart");
                        }
                    }


//////////////////////////////////////////////////////////////////////////////////

                    if (e.getSource().equals(NextButton)) {
                        ++i;
                        if (i >= CardPanel.countries.length) {
                            i = 0;
                        }

                        CardPanel.roll.setSelectedIndex(i);
                        CardPanel.logger.trace("Pressed Next --> " + i);
                    } else if (e.getSource().equals(PreviousButton)) {
                        --i;
                        if (i < 0) {
                            i = CardPanel.countries.length - 1;
                        }

                        CardPanel.roll.setSelectedIndex(i);
                        CardPanel.logger.trace("Pressed Previous --> " + i);
                    } else {
                        CardPanel.logger.error("There is no action listener for that button!!!");
                    }
                }
            }
        };

        // polowanie na eventy ?
        MouseListener myMouseListener = new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                CardLayout card = (CardLayout)flagVsStats.getLayout();
                card.previous(flagVsStats);
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        };

        // stworzenie ikonek dla przycisków
        ImageIcon PicFromUrlIcon = new ImageIcon("GRAPHICS/cor3.png");
        ImageIcon TitleIcon = new ImageIcon("GRAPHICS/title2.png");
        ImageIcon PreviousIcon = new ImageIcon("GRAPHICS/left4.png");
        ImageIcon NextIcon = new ImageIcon("GRAPHICS/right4.png");
        ImageIcon BarChartIcon = new ImageIcon("GRAPHICS/wykr3.png");

        // ustawianie ikony przy przewijaniu
        NextButton.setIcon(NextIcon);
        PreviousButton.setIcon(PreviousIcon);
        PicFromURL.setIcon(PicFromUrlIcon);
        MainTitle.setIcon(TitleIcon);
        BarChart.setIcon(BarChartIcon);

        //ustawienia przycisku next
        NextButton.setBorderPainted(false);
        NextButton.setContentAreaFilled(false);
        NextButton.addActionListener(MyActionListener);

        // ustawienia przycisku previous
        PreviousButton.setBorderPainted(false);
        PreviousButton.setContentAreaFilled(false);
        PreviousButton.addActionListener(MyActionListener);

        // ustawienia przycisku pokazujacego zdj covida
        PicFromURL.setBorderPainted(false);
        PicFromURL.setContentAreaFilled(false);
        PicFromURL.addActionListener(MyActionListener);

        // ustawienia przycisku z napisem "covid"
        MainTitle.setBorderPainted(false);
        MainTitle.setContentAreaFilled(false);
        MainTitle.setMargin(new Insets(0, 50, 20, 0)); // odstęp od białej ramki napisu covid
        MainTitle.addActionListener(MyActionListener);

        // ustawienie przycisku wykresu
        BarChart.setBorderPainted(false);
        BarChart.setContentAreaFilled(false);
        BarChart.addActionListener(MyActionListener);


        // ustawienia comboboxa
        roll.setSelectedIndex(0);
        roll.setBackground(Color.WHITE);
        roll.setForeground(Color.BLACK);
        roll.setFont(new Font("Helvetica", 1, 25)); // zmiana wygladu i rozmiaru czcionki w comboBox

        // stworzenie panelu gornego
        JPanel MainPanel = new JPanel(new GridLayout());
        MainPanel.setBackground(Color.DARK_GRAY); // kolor
        MainPanel.add(MainTitle); // 1 przycisk - napis covid
        MainPanel.add(PicFromURL); // 2 przycisk - zdj covida
        MainPanel.add(BarChart);

        // stworzenie srodkowego panelu - tam gdzie sa przyciski do przewijania next i previous
        JPanel SwitchPanel = new JPanel(new GridLayout());
        SwitchPanel.setBackground(Color.DARK_GRAY); // kolor
        SwitchPanel.add(PreviousButton); // dodanie przycisku next
        SwitchPanel.add(roll); // dodanie comboboxa
        SwitchPanel.add(NextButton); // dodanie przycisku next
        // ustawienie ramki panelu środkowego
        SwitchPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder((Border)null, "SwitchPanel", 0, 0, (Font)null, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 0, 10, 0)));

        // stworzenie panelu dolnego - z flagami
        JPanel FlagPanel = new JPanel(new BorderLayout());
        FlagPanel.setBackground(Color.DARK_GRAY);
        FlagPanel.setPreferredSize(new Dimension(440, 320));
        final JLabel FlagLabel = new JLabel(SetFlag(0));
        FlagPanel.add(FlagLabel);
        FlagPanel.addMouseListener(myMouseListener);

        // stworzenie dolnego panelu - ze statami
        JPanel StatsPanel = new JPanel(new GridLayout());
        StatsPanel.setPreferredSize(new Dimension(440, 320));
        StatsPanel.setBackground(Color.DARK_GRAY);
        StatsPanel.setForeground(Color.WHITE);
        StatsPanel.addMouseListener(myMouseListener);
        StatsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder((Border)null, "Statystyki", 0, 0, (Font)null, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 50, 10, 0)));

        // napisy parsowne ze strony - staty
        detailedInfo.setForeground(Color.WHITE); // ustawienie ich koloru
        StatsPanel.add(detailedInfo); // dodanie statów

        // coś do przelaczania miedzy kolejnymi krajami
        pool.submit(new Runnable() {
            public void run() {
                CardPanel.SetStats(0);
            }
        });
        ItemListener myItemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    FlagLabel.setIcon(CardPanel.SetFlag(CardPanel.roll.getSelectedIndex()));
                    CardPanel.pool.submit(new Runnable() {
                        public void run() {
                            CardPanel.SetStats(CardPanel.roll.getSelectedIndex());
                        }
                    });
                }

            }
        };

        roll.addItemListener(myItemListener);

        //dodanie do panelu dolnego dwóch panelów - z flagami i statami
        flagVsStats.add(FlagPanel);
        flagVsStats.add(StatsPanel);

        // f to główne białe obramowanie całej aplikacji - dodawanie kolejnych panelów i ustawienia zachowania
        f.add(MainPanel, "North");
        f.add(SwitchPanel, "Center");
        f.add(flagVsStats, "South");
        f.pack();
        f.setLocationRelativeTo((Component)null);
        f.setDefaultCloseOperation(3);
        f.setVisible(true);
    }

    // dodanie zdjec flag
    private static Icon SetFlag(int value) {
        if (Flags[value] != null) {
            return Flags[value];
        } else {
            String var10000 = countries[value];
            String tmp = "FLAGS/" + var10000.toLowerCase() + ".png";
            ImageIcon tmpImage = new ImageIcon(tmp);
            float dzielnik = Math.max((float)tmpImage.getIconHeight() / 440.0F, (float)tmpImage.getIconWidth() / 320.0F);
            int NewHeight = (int)(0.5D + (double)((float)tmpImage.getIconHeight() / dzielnik));
            int NewWidth = (int)(0.5D + (double)((float)tmpImage.getIconWidth() / dzielnik));
            ImageIcon image = new ImageIcon(tmpImage.getImage().getScaledInstance(NewWidth, NewHeight, 4));
            Flags[value] = image;
            return image;
        }
    }

    // parsowanie statystyk
    private static synchronized void SetStats(int val) {
        if (COVIDInfo[val] != null) {
            detailedInfo.setText(COVIDInfo[val]);
        } else {
            String tmpInfoCountry = "<html>\n";
            URL url = null;

            try {
                if (countriesURL[val] != "world") {
                    String var10002 = countriesURL[val];
                    url = new URL("https://www.worldometers.info/coronavirus/country/" + var10002 + "/");
                } else {
                    url = new URL("https://www.worldometers.info/coronavirus/");
                }
            } catch (MalformedURLException var15) {
                logger.error("Problem with URL" + url, var15);
                return;
            }

            boolean connected = false;
            Response res = null;
            int i = 0;
            byte attempts = 5;

            while(!connected) {
                logger.info("Proba po��czenia z: " + url);

                try {
                    res = Jsoup.connect(url.toString()).execute();
                    logger.info("Nawi�zano po��czenie z: " + url);
                    connected = true;
                } catch (IOException var14) {
                    logger.error("Nie mo�na po��czy� si� z: " + url);
                }

                if (!connected) {
                    if (i >= attempts) {
                        logger.fatal("Nie mo�na po��czy� si� z " + url + "... Zamykanie programu ");
                        return;
                    }

                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException var13) {
                    }
                }
            }

            logger.info("Parsowanie...");
            Document doc = null;

            try {
                doc = res.parse();
            } catch (IOException var12) {
                logger.fatal("Nie mo�na parsowa� strony " + url, var12);
                return;
            }

            Elements tmp = doc.select("div.maincounter-number");
            ArrayList<String> Dane1 = new ArrayList();
            Iterator var10 = tmp.iterator();

            while(var10.hasNext()) {
                Element e = (Element)var10.next();
                Dane1.add(e.getElementsByTag("span").text());
            }

            tmpInfoCountry = tmpInfoCountry + "<P><B><font size=+4>Zakażenia: </B>&nbsp;&nbsp;<font size=+4>" + (String)Dane1.get(0) + "</font></P><BR>\n";
            tmpInfoCountry = tmpInfoCountry + "<P><B><font size=+4>Zgony: </B>&nbsp;&nbsp;<font size=+4>" + (String)Dane1.get(1) + "</font></P><BR>\n";
            tmpInfoCountry = tmpInfoCountry + "<P><B><font size=+4>Wyleczonych: </B>&nbsp;&nbsp;<font size=+4>" + (String)Dane1.get(2) + "</font></P><BR>\n";
            tmpInfoCountry = tmpInfoCountry + "</html>";
            COVIDInfo[val] = tmpInfoCountry;
            if (roll.getSelectedIndex() == val) {
                detailedInfo.setText(tmpInfoCountry);
            } else {
                logger.fatal("Dane si� zmieni�y!");
            }

        }
    }

    static {
        COVIDInfo = new String[countries.length];
        Flags = new Icon[countries.length];
        roll = new JComboBox(countries);
        detailedInfo = new JLabel();
        pool = Executors.newFixedThreadPool(5);
    }
}
