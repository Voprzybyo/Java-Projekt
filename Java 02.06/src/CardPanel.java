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

@SuppressWarnings("serial") // Pozbycie si� warning�w...

public class CardPanel extends JPanel {

	static Logger logger = Logger.getLogger(CardPanel.class);

	
	/* Rozmiar okna */
	private final static int Width = 320;
	private final static int Height = 440;

	private final static int WORLD = 0;

	private final static String[] countries = { "�wiat", "Niemcy", "Rosja", "Polska", "S�owacja", "USA", "Brazylia",
												"Hiszpania", "UK", "Wlochy", "Francja", "Turcja", "Indie", "Iran", "Kanada", "Chiny", "Chile" };

	private final static String[] countriesURL = { "world", "germany", "russia", "poland", "slovakia", "us", "brazil", "spain",
												   "uk", "italy", "france", "turkey", "india", "iran", "canada", "china", "chile" };

	private static String[] COVIDInfo = new String[countries.length];	
	
	private static Icon[] Flags = new Icon[countries.length]; 	//Tablica z flagami
	
	/*Tworzymy Comboboxa, kt�remu w konstruktorze przekazujemy tablic� z nazwami Pa�stw*/
	final static JComboBox<String> roll = new JComboBox<String>(countries);
	private static JLabel detailedInfoLabel = new JLabel();
	private static ExecutorService pool = Executors.newFixedThreadPool(4); //Wielow�tkowo�� do obs�ugi wywo�a�
	
	
	public static void CreateAndShowGUI() throws MalformedURLException {

		/*Utworzenie ramki g��wnej --> W konstruktorze napis w belce*/
		JFrame f = new JFrame("COVID-19"); 

		//final, bo wszystko wewn�trz (takie elementy, kt�re nie zmieni� powi�zania)
		final JPanel flagVsStats = new JPanel(new CardLayout());	//Panel prze��czaj�cy mi�dzy flagami a statystykami
		final JButton NextButton = new JButton();					//NAST�PNY
		final JButton PreviousButton = new JButton();				//POPRZEDNI
		final JButton PicFromURL = new JButton();					//Przycisk pobieraj�cy grafike z URL
		final JButton MainTitle = new JButton();					//Nag��wek
		final JButton BarChart = new JButton();
		
		/* ACTION LISTENER --> odpowiada za obs�ug� zdarze�*/
		ActionListener MyActionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int i = roll.getSelectedIndex();	//Aby nie przekroczy� zakresu --> przypisujemy do 'i' ten element, kt�ry obecnie jest zaznaczony na roll'u

				/*MAIN TITLE LISTENER --> Powr�t do statystyk ze �wiata*/
				if (e.getSource().equals(MainTitle)) {					//equals() por�wnuje 2 teksty! Mo�liwe tylko z "final"!
					roll.setSelectedIndex(WORLD);
					return;
				}
				
				/*MAIN PICTURE LISTENER --> Wy�wietlenie grafiki z URL*/
				if (e.getSource().equals(PicFromURL)) {

					try {
						new ObrazFrame();
						return;
						
					} catch (IOException exc) {
						logger.error("Exception --> Can't display pic from URL");
					}
				}
				
				/*BAR PIC LISTENER --> Wy�wietlenie wykresu s�upkowego*/
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
					if (i >= countries.length) {i = 0;} // Aby nie przekroczy� zakresu "zap�tlono" liste na pocz�tek
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

			/*Je�li u�ytkownik naci�nie na panel to prze��czy na nast�pn� kart�*/
			public void mousePressed(MouseEvent e) {
				CardLayout cardlay = (CardLayout) flagVsStats.getLayout();		//getLayout() zwraca LayoutManager (klase nadrz�dn�), wi�c rzutujemy na CardLayout
				cardlay.previous(flagVsStats);									//Prze��czenie pomi�dzy 2 kartami (flaga i statystyki)
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
		
		/*Przypisanie grafik do przycisk�w*/
		NextButton.setIcon(NextIcon);
		PreviousButton.setIcon(PreviousIcon);
		PicFromURL.setIcon(PicFromUrlIcon);
		MainTitle.setIcon(TitleIcon);
		BarChart.setIcon(BarChartIcon);
		
		/*W�a�ciwo�ci poszczeg�lnych przycisk�w --> wype�nienie t�a, usuni�cie obramowa�*/
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
		
		
		/*Panel tytu�owy*/
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
		
		
		/*G��wny panel*/		
		/*Flagi*/
		JPanel FlagPanel = new JPanel(new BorderLayout());
		FlagPanel.setBackground(Color.DARK_GRAY);
		FlagPanel.setPreferredSize(new Dimension(Height, Width));
		
		final JLabel FlagLabel = new JLabel(SetFlag(WORLD));	
		FlagPanel.add(FlagLabel);									//Dodanie FlagLabela do FlagPanelu
		FlagPanel.addMouseListener(myMouseListener);				

		/*Statystyki*/
		JPanel StatsPanel = new JPanel(new GridLayout()); //new GridLayout powoduje, �e info nie wy�wietla si� centralnie na �rodku
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

		/*Wielow�tkowo��*/
		 /*pool istnieje --> submit --> przekazujemy obiekt, kt�ry jest instancj� klasy implementuj�cej interface Runnable*/
		pool.submit(new Runnable() {
			public void run() {
				SetStats(WORLD);
			}
		});

		ItemListener myItemListener = new ItemListener() {

			public void itemStateChanged(ItemEvent e) { //przekazujemy obiekt, kt�ry mo�e by� ustawiony jako wn�trze JLabel --> OBRAZ
				
				/*Warunek zapobiegaj�cy podw�jnemu wywo�ywniu tej metody po klikni�ciu np. nextButton --> Interesuje nas tylko stan SELECTED (bez DESELECTED)*/
				if (e.getStateChange() == ItemEvent.SELECTED) {
					
					 FlagLabel.setIcon(SetFlag(roll.getSelectedIndex()));	//Ustawienie flagi odpowiadaj�cej obecnej warto�ci na roll'u

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

		/*Upakowanie wszystkiego w g��wnym oknie*/
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
		
		/* Aby ka�dorazowo nie by�y wo�ywane metody zwi�zane ze zmian� rozmiaru to...
		 * Je�eli w tej tablicy co� ju� jest, to zwrazana jest ta warto��(flaga)*/
		if (Flags[value] != null) {
			return Flags[value];
		
		/*Je�eli nie ma, to szukamy j� w folderze*/
		}else {
		String tmp = "FLAGS/" + countries[value].toLowerCase() + ".png";	//�cie�ka do flagi w .png
		
		ImageIcon tmpImage = new ImageIcon(tmp); //Stworzenie ImageIcon u�ywaj�c konstruktora ze �cie�k� do obrazu

		/*Dzielnik modyfikuj�cy wielko�� flagi --> wyznaczamy wi�ksz� z tych warto�ci, �eby obraz m�g� si� zmie�ci�*/
		float dzielnik = Math.max(tmpImage.getIconHeight() / (float) Height, tmpImage.getIconWidth() / (float) Width);	

		/*Dostosowane rozmiaru flagi*/
		int NewHeight = (int) (0.5 + tmpImage.getIconHeight() / dzielnik);	
		int NewWidth = (int) (0.5 + tmpImage.getIconWidth() / dzielnik);

		/*Tworzymy nowy obiekt, w kt�rym b�dzie przeskalowana flaga
		 * getScaledInstance() --> metoda pozwalaj�ca przeskalowa� obraz u�ywaj�c okre�lonych rozmiar�w*/
		ImageIcon finalImage = new ImageIcon(tmpImage.getImage().getScaledInstance(NewWidth, NewHeight, Image.SCALE_SMOOTH));

		Flags[value] = finalImage;	//Wk�adamy przeskalowany obraz do tablicy z flagami
		return finalImage;
		}
	}

	/*Ustawienie info o statystykach w danym kraju*/
	synchronized private static void SetStats(int val) {

		/*Je�eli opis ju� istnieje, to zostaje ustawiony*/
		if (COVIDInfo[val] != null) {
			 detailedInfoLabel.setText(COVIDInfo[val]);
			return;
		}

		/*Je�eli nie...*/
		String tmpInfoCountry = "<html>\n";
		URL url = null;

		/*Pr�ba po��czenia*/
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
			logger.info(i + " proba po��czenia z: " + url);
			
			try {
				res = Jsoup.connect(url.toString()).referrer("http://www.google.pl").execute();
				logger.info("Nawi�zano po��czenie z: " + url);
				connected_flag = true;
			} catch (IOException e) {
				logger.error("Nie mo�na po��czy� si� z: " + url);
			}
			
			
			if (!connected_flag) {
				i++;
				if (i < attempts + 1) {

					try {
						Thread.sleep(1000); //U�pienie w�tku na 1 sec
					} catch (InterruptedException exc) {
					}
				} else {
					logger.fatal("Nie mo�na po��czy� si� z " + url + "... Zamykanie programu ");
					return;
				}
			}	
		}
		
		logger.info("Parsowanie...");

		Document doc = null;

		try {
			doc = res.parse();	
		} catch (IOException e) {
			logger.fatal("Nie mo�na parsowa� strony " + url);
			return;
		}

		Elements tmp = doc.select("div.maincounter-number");
		ArrayList<String> Dane1 = new ArrayList<>();

		for (Element e : tmp) {
			Dane1.add(e.getElementsByTag("span").text());
		}

		tmpInfoCountry += ("<P><B><font size=+4>ZAKA�ENIA: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(0) + "</font></P><BR>\n");
		tmpInfoCountry += ("<P><B><font size=+4>ZGONY: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(1) + "</font></P><BR>\n");
		tmpInfoCountry += ("<P><B><font size=+4>WYLECZONYCH: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(2) + "</font></P><BR>\n");
		tmpInfoCountry += "</html>";

		/*Wpisanie statystyk do tablicy ze statystykami*/
		COVIDInfo[val] = tmpInfoCountry;

		/*Warunek gwarantuj�cy, �e przy szybkim prze��czaniu tekst nie b�dzie przeskakiwa� --> Upewnienie si�, �e statystyki pochodz� z danego kraju*/
		if (roll.getSelectedIndex() == val) {
			 detailedInfoLabel.setText(tmpInfoCountry);	//ustawienie tesktu w labelu ze statystykami
		} else {
			logger.fatal("Dane si� zmieni�y!");
		}
	}

}