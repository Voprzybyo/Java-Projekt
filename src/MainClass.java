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

    /*Metoda  EventQueue.invokeLater()  powoduje, �e kod, kt�ry jest umieszczony w metodzie run zostanie wykonany na pewno przez 
     * w�tek uruchomiony przez klasy Swing - konkretniej przez w�tek s�u��cy do obs�ugi interfejsu graficznego.
	 * Powodem tego jest to i� kod biblioteki Swing nie jest wielow�tkowy.*/
    public static void main(String[] args) throws IOException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ActionFrame();				//Wywo�anie metody ActionFrame (menu g��wne) ; dynamiczne zaalokowanie pamieci "new"
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
        Elements ogolnie = page2.select("div.maincounter-number");					//div --> kontener do pozycjonowania elementow na stronie

        /*Kontenery na parsowane dane*/ 
        Dane1 = new ArrayList<>();		//POLSKA
        Dane2 = new ArrayList<>();		//�WIAT

        /*Wyswietlenie danych w konsoli i wpisanie do waktor�w Dane1, Dane2*/
        int i = 1;
        System.out.println("POLSKA");
        for(Element e:krajelements) {
            System.out.println(i + " " + e.getElementsByTag("span").text());		//span --> frazowanie tre�ci
            Dane1.add(e.getElementsByTag("span").text());							//Wy�uskanie konkretnych elementow strony
            i++;
        }
        
        i=1;
        System.out.println("�WIAT");
        for(Element e:ogolnie) {
            System.out.println(i + " " + e.getElementsByTag("span").text());		//span --> frazowanie tre�ci
            Dane2.add(e.getElementsByTag("span").text());							//Wy�uskanie konkretnych elementow strony
            i++;
        }

    }
}
