/*Import Jsoup*/
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/*Pozostale*/
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainClass {

    static String title;
    static ArrayList<String> Dane1;
    static ArrayList<String> Dane2;

    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ActionFrame();				//Wywo�anie metody ActionFrame (menu g��wne)
            }
        });

        /*Adresy stron internetowych z kt�rych bed� pozyskiwane dane*/
        String url1 = "https://www.worldometers.info/coronavirus/country/poland/";
        String url2 = "https://www.worldometers.info/coronavirus/";
        
        /*Utworzenie obiekt�w typu 'Document' i metody ��cz�ce*/
        Document page1 = Jsoup.connect(url1).get(); 			//mozna dodac "userAgent()"
        Document page2 = Jsoup.connect(url2).get();
        
        /*Zmienna przechowujaca tytu� strony*/
        title = page1.title(); 

        /* CSS Selector --> Wyb�r konkretnych element�w kt�re zostan� pozyskane ze strony*/
        Elements krajelements = page1.select("div.maincounter-number");
        Elements ogolnie = page2.select("div.maincounter-number");

        /*Kontenery na parsowane dane*/ 
        Dane1 = new ArrayList<>();		//POLSKA
        Dane2 = new ArrayList<>();		//�WIAT

        /*Wyswietlenie danych w konsoli i wpisanie do waktor�w Dane1, Dane2*/
        int i = 1;
        System.out.println("POLSKA");
        for(Element e:krajelements) {
            System.out.println(i + " " + e.getElementsByTag("span").first().text());
            Dane1.add(e.getElementsByTag("span").text());
            i++;
        }
        
        i=1;
        System.out.println("�WIAT");
        for(Element e:ogolnie) {
            System.out.println(i + " " + e.getElementsByTag("span").first().text());
            Dane2.add(e.getElementsByTag("span").text());
            i++;
        }

    }
}
