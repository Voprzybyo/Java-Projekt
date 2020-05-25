import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import javax.swing.*;
import java.awt.*;
import java.io.File;
@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class InfoFrame extends JFrame  {

    private static String s;		//zmienna string s�u��ca do wy�wietlenia pobranych danych
    
    static Logger logger = Logger.getLogger(InfoFrame.class);	//...


    public static void RamkaInformacyjnaPolska() {

        JFrame jf = new JFrame("POLSKA STAT"); //Nowa ramka z belk� z napisem jak w konstruktorze
        jf.setLayout(new FlowLayout());		   //Taki layout manager z biblioteki JPanel
        JPanel panel = new JPanel();		   //Nowy panel
        jf.add(panel);						   //Dodanie panelu do ramki

        
        /*Wpisanie do zmiennej s danych do wyswietlania w nowootwartej ramce*/
        s = "<html>Statystyki w Polsce<br><br>";
        s += "ZAKA�ENIA: " + MainClass.Dane1.get(0) + "<br>";
        s += "ZGONY: " + MainClass.Dane1.get(1) + "<br>";
        s += "WYLECZONYCH: " + MainClass.Dane1.get(2) + "<br></html>\"";

        JLabel tekst = new JLabel(s);			//Takie jakby rzutowanie string na tekst mog�cy by� wy�wietlony
        panel.add(tekst);						//Dodatnie tekstu do panelu
        panel.setSize(200,200);					//Wymiary okna

        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));		

        jf.setLocation(600,400);				//Po�o�enie lewego g�rnego rogu okna
        jf.pack();								//Metoda pack() ustala rozmiar okna tak, aby mie�ci�y si� w nim wszystkie widoczne komponenty.
        jf.setVisible(true);
    }

    
    public static void RamkaInformacyjnaSwiat() {
        JFrame jf = new JFrame("�WIAT STAT");	//Nowa ramka z belk� z napisem jak w konstruktorze
        jf.setLayout(new FlowLayout());			//Taki layout manager z biblioteki JPanel
        JPanel panel = new JPanel();			//Nowy panel
        jf.add(panel);							//Dodanie panelu do ramki

        
        /*Wpisanie do zmiennej s danych do wyswietlania w nowootwartej ramce*/
        s = "<html>Statystyki na �wiecie<br><br>";
        s += "ZAKA�ENIA: " + MainClass.Dane2.get(0) + "<br>";
        s += "ZGONY: " + MainClass.Dane2.get(1)+ "<br>";
        s += "WYLECZONYCH: " + MainClass.Dane2.get(2) + "<br></html>\"";

        JLabel tekst = new JLabel(s);			//Takie jakby rzutowanie string na tekst mog�cy by� wy�wietlony
        panel.add(tekst);						//Dodatnie tekstu do panelu
        panel.setSize(200,200);					//Wymiary okna

        tekst.setFont(new Font("Helvetica", Font.BOLD, 35));

        jf.setLocation(600,400);				//Po�o�enie lewego g�rnego rogu okna
        jf.pack();								//Metoda pack() ustala rozmiar okna tak, aby mie�ci�y si� w nim wszystkie widoczne komponenty.
        jf.setVisible(true);
    }

}
