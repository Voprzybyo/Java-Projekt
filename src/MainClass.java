/*Import log4j*/
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;
/*Import Jsoup*/
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*Pozostale*/
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainClass {

    static String title;
    static ArrayList<String> Dane1;
    static ArrayList<String> Dane2;
    static Logger logger = Logger.getLogger(MainClass.class);

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ActionFrame();
            }
        });

        /*Adresy stron internetowych z których bed¹ pozyskiwane dane*/
        String url1 = "https://www.worldometers.info/coronavirus/country/poland/";
        String url2 = "https://www.worldometers.info/coronavirus/";
        
        /*Utworzenie obiektów typu 'Document' i Metody ³¹cz¹ce*/
        Document page1 = Jsoup.connect(url1).get(); 			//mozna dodac "userAgent()"
        Document page2 = Jsoup.connect(url2).get();
        
        /*Zmienna przechowujaca tytu³ strony*/
        title = page1.title(); 

        /* CSS Selector --> Wybór konkretnych elementów które zostan¹ pozyskane ze strony*/
        Elements krajelements = page1.select("div.maincounter-number");
        Elements ogolnie = page2.select("div.maincounter-number");

        /*Kontenery na parsowane dane*/ 
        Dane1 = new ArrayList<>();		//POLSKA
        Dane2 = new ArrayList<>();		//ŒWIAT

        /*Wyswietlenie danych w konsoli i wpisanie do waktorów Dane1, Dane2*/
        int i = 1;
        System.out.println("POLSKA");
        for(Element e:krajelements) {
            System.out.println(i + " " + e.getElementsByTag("span").first().text());
            Dane1.add(e.getElementsByTag("span").text());
            i++;
        }
        
        i=1;
        System.out.println("ŒWIAT");
        for(Element e:ogolnie) {
            System.out.println(i + " " + e.getElementsByTag("span").first().text());
            Dane2.add(e.getElementsByTag("span").text());
            i++;
        }

        
        
        
        String log4jConfigFile = System.getProperty("user.dir")
                + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
        logger.debug("this is a debug log message");
        logger.info("this is a information log message");
        logger.warn("this is a warning log message");
    }
}
