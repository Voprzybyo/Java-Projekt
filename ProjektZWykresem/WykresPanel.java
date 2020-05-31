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

public class WykresPanel extends JPanel
{
    static Logger logger = Logger.getLogger(CardPanel.class);
    private int histogramHeight = 200;
    private int barWidth = 50;
    private int barGap = 10;

    private JPanel barPanel; // słupki
    private JPanel labelPanel; // dolne podpisy ABCDEF

    private List<Bar> bars = new ArrayList<Bar>();

    public WykresPanel(){
        setBorder(new EmptyBorder(10, 10, 10, 10)); // ramka czarna
        setLayout(new BorderLayout());

        barPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        Border outer = new MatteBorder(1, 1, 1, 1, Color.BLACK); // grubosc ramki i kolor
        Border inner = new EmptyBorder(10, 10, 0, 10); // odległosc słupków od ramki
        Border compound = new CompoundBorder(outer, inner);
        barPanel.setBorder(compound);

        labelPanel = new JPanel(new GridLayout(1, 0, barGap, 0));
        labelPanel.setBorder(new EmptyBorder(5, 10, 0, 10));

        add(barPanel, BorderLayout.CENTER);
        add(labelPanel, BorderLayout.PAGE_END);
    }

        public void addHistogramColumn (String label,int value, Color color)
        {
            Bar bar = new Bar(label, value, color);
            bars.add(bar);
        }

        public void layoutHistogram ()
        {
            barPanel.removeAll();
            labelPanel.removeAll();

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



        public void createAndShowGUI (String[] countries, String[] countriesURL) throws MalformedURLException {

            //this.addHistogramColumn("A", 350, Color.RED);
            //this.addHistogramColumn("B", 690, Color.YELLOW);
            //this.addHistogramColumn("C", 510, Color.BLUE);
            //this.addHistogramColumn("D", 570, Color.ORANGE);
            //this.addHistogramColumn("E", 180, Color.MAGENTA);
            //this.addHistogramColumn("F", 504, Color.CYAN);

            URL url = null;
            for(int i=1; i<countries.length-1; i++)
            {
                try {
                    String var10002 = countriesURL[i];
                    url = new URL("https://www.worldometers.info/coronavirus/country/" + var10002 + "/");
                } catch (MalformedURLException var15) {
                    logger.error("Problem with URL" + url, var15);
                    return;
                }

                boolean connected = false;
                Connection.Response res = null;
                int j = 0;
                byte attempts = 5;

                while(!connected) {
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
                Element e = (Element)var10.next();
                Dane1.add(e.getElementsByTag("span").text());
                String pomocnicza = Dane1.get(0);
                pomocnicza = pomocnicza.replace(",", "");
                //System.out.println(pomocnicza);
                int value = Integer.parseInt(pomocnicza);
                this.addHistogramColumn(countries[i], value, Color.RED);
                this.layoutHistogram();
            }
        }

    private class ColorIcon implements Icon {
        private int shadow = 3;

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
}