import java.awt.EventQueue;

import java.util.Date;

import javax.swing.JFrame;

import java.util.Calendar;
import java.util.ArrayList;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainClass {

	static String title;
	static ArrayList<String> Dane1;
	static ArrayList<String> Dane2;
	
	public static void main(String[] args) throws IOException {
 		EventQueue.invokeLater(new Runnable() {
 			@Override
 			public void run() {
 				new ActionFrame();
 			}
 		});
 		
 		  String url1 = "https://www.worldometers.info/coronavirus/country/poland/";
		  String url2 = "https://www.worldometers.info/coronavirus/";
		  Document page1 = Jsoup.connect(url1).userAgent("HHHH").get();
		  Document page2 = Jsoup.connect(url2).userAgent("HHHH").get();
		 title = page1.title();
		  

		Elements krajelements = page1.select("div.maincounter-number");
		Elements ogolnie = page2.select("div.maincounter-number");
		
		Dane1 = new ArrayList<>();
		Dane2 = new ArrayList<>();
		
		 int i = 0;
		 for(Element e:krajelements) {
				  System.out.println(i + " " + e.getElementsByTag("span").first().text());
				  Dane1.add(e.getElementsByTag("span").text());
				  i++;
		 }
		 i=0;
		 for(Element e:ogolnie) {
			  System.out.println(i + " " + e.getElementsByTag("span").first().text());
			  Dane2.add(e.getElementsByTag("span").text());
			  i++;
	 }

	}
}
