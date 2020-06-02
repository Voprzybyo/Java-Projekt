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

@SuppressWarnings("serial") // Pozbycie siê warningów...

public class CardPanel extends JPanel {

	static Logger logger = Logger.getLogger(CardPanel.class);

	
	/* Rozmiar okna */
	private final static int Width = 320;
	private final static int Height = 440;

	private final static int WORLD = 0;

	private final static String[] countries = { "Œwiat", "Niemcy", "Rosja", "Polska", "S³owacja", "USA", "Brazylia",
												"Hiszpania", "UK", "Wlochy", "Francja", "Turcja", "Indie", "Iran", "Kanada", "Chiny", "Chile" };

	private final static String[] countriesURL = { "world", "germany", "russia", "poland", "slovakia", "us", "brazil", "spain",
												   "uk", "italy", "france", "turkey", "india", "iran", "canada", "china", "chile" };

	private static String[] COVIDInfo = new String[countries.length];	
	
	private static Icon[] Flags = new Icon[countries.length]; 	//Tablica z flagami
	
	/*Tworzymy Comboboxa, któremu w konstruktorze przekazujemy tablicê z nazwami Pañstw*/
	final static JComboBox<String> roll = new JComboBox<String>(countries);
	private static JLabel detailedInfoLabel = new JLabel();
	private static ExecutorService pool = Executors.newFixedThreadPool(4); //Wielow¹tkowoœæ do obs³ugi wywo³añ
	
	
	public static void CreateAndShowGUI() throws MalformedURLException {

		/*Utworzenie ramki g³ównej --> W konstruktorze napis w belce*/
		JFrame f = new JFrame("COVID-19"); 

		//final, bo wszystko wewn¹trz (takie elementy, które nie zmieni¹ powi¹zania)
		final JPanel flagVsStats = new JPanel(new CardLayout());	//Panel prze³¹czaj¹cy miêdzy flagami a statystykami
		final JButton NextButton = new JButton();					//NASTÊPNY
		final JButton PreviousButton = new JButton();				//POPRZEDNI
		final JButton PicFromURL = new JButton();					//Przycisk pobieraj¹cy grafike z URL
		final JButton MainTitle = new JButton();					//Nag³ówek
		final JButton BarChart = new JButton();
		
		/* ACTION LISTENER --> odpowiada za obs³ugê zdarzeñ*/
		ActionListener MyActionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int i = roll.getSelectedIndex();	//Aby nie przekroczyæ zakresu --> przypisujemy do 'i' ten element, który obecnie jest zaznaczony na roll'u

				/*MAIN TITLE LISTENER --> Powrót do statystyk ze œwiata*/
				if (e.getSource().equals(MainTitle)) {					//equals() porównuje 2 teksty! Mo¿liwe tylko z "final"!
					roll.setSelectedIndex(WORLD);
					return;
				}
				
				/*MAIN PICTURE LISTENER --> Wyœwietlenie grafiki z URL*/
				if (e.getSource().equals(PicFromURL)) {

					try {
						new ObrazFrame();
						return;
						
					} catch (IOException exc) {
						logger.error("Exception --> Can't display pic from URL");
					}
				}
				
				/*BAR PIC LISTENER --> Wyœwietlenie wykresu s³upkowego*/
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
					if (i >= countries.length) {i = 0;} // Aby nie przekroczyæ zakresu "zapêtlono" liste na pocz¹tek
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

			/*Je¿li u¿ytkownik naciœnie na panel to prze³¹czy na nastêpn¹ kartê*/
			public void mousePressed(MouseEvent e) {
				CardLayout cardlay = (CardLayout) flagVsStats.getLayout();		//getLayout() zwraca LayoutManager (klase nadrzêdn¹), wiêc rzutujemy na CardLayout
				cardlay.previous(flagVsStats);									//Prze³¹czenie pomiêdzy 2 kartami (flaga i statystyki)
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
		
		/*W³aœciwoœci poszczególnych przycisków --> wype³nienie t³a, usuniêcie obramowañ*/
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
		
		
		/*Panel tytu³owy*/
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
		
		
		/*G³ówny panel*/		
		/*Flagi*/
		JPanel FlagPanel = new JPanel(new BorderLayout());
		FlagPanel.setBackground(Color.DARK_GRAY);
		FlagPanel.setPreferredSize(new Dimension(Height, Width));
		
		final JLabel FlagLabel = new JLabel(SetFlag(WORLD));	
		FlagPanel.add(FlagLabel);									//Dodanie FlagLabela do FlagPanelu
		FlagPanel.addMouseListener(myMouseListener);				

		/*Statystyki*/
		JPanel StatsPanel = new JPanel(new GridLayout()); //new GridLayout powoduje, ¿e info nie wyœwietla siê centralnie na œrodku
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

		/*Wielow¹tkowoœæ*/
		 /*pool istnieje --> submit --> przekazujemy obiekt, który jest instancj¹ klasy implementuj¹cej interface Runnable*/
		pool.submit(new Runnable() {
			public void run() {
				SetStats(WORLD);
			}
		});

		ItemListener myItemListener = new ItemListener() {

			public void itemStateChanged(ItemEvent e) { //przekazujemy obiekt, który mo¿e byæ ustawiony jako wnêtrze JLabel --> OBRAZ
				
				/*Warunek zapobiegaj¹cy podwójnemu wywo³ywniu tej metody po klikniêciu np. nextButton --> Interesuje nas tylko stan SELECTED (bez DESELECTED)*/
				if (e.getStateChange() == ItemEvent.SELECTED) {
					
					 FlagLabel.setIcon(SetFlag(roll.getSelectedIndex()));	//Ustawienie flagi odpowiadaj¹cej obecnej wartoœci na roll'u

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

		/*Upakowanie wszystkiego w g³ównym oknie*/
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
		
		/* Aby ka¿dorazowo nie by³y wo³ywane metody zwi¹zane ze zmian¹ rozmiaru to...
		 * Je¿eli w tej tablicy coœ ju¿ jest, to zwrazana jest ta wartoœæ(flaga)*/
		if (Flags[value] != null) {
			return Flags[value];
		
		/*Je¿eli nie ma, to szukamy j¹ w folderze*/
		}else {
		String tmp = "FLAGS/" + countries[value].toLowerCase() + ".png";	//Œcie¿ka do flagi w .png
		
		ImageIcon tmpImage = new ImageIcon(tmp); //Stworzenie ImageIcon u¿ywaj¹c konstruktora ze œcie¿k¹ do obrazu

		/*Dzielnik modyfikuj¹cy wielkoœæ flagi --> wyznaczamy wiêksz¹ z tych wartoœci, ¿eby obraz móg³ siê zmieœciæ*/
		float dzielnik = Math.max(tmpImage.getIconHeight() / (float) Height, tmpImage.getIconWidth() / (float) Width);	

		/*Dostosowane rozmiaru flagi*/
		int NewHeight = (int) (0.5 + tmpImage.getIconHeight() / dzielnik);	
		int NewWidth = (int) (0.5 + tmpImage.getIconWidth() / dzielnik);

		/*Tworzymy nowy obiekt, w którym bêdzie przeskalowana flaga
		 * getScaledInstance() --> metoda pozwalaj¹ca przeskalowaæ obraz u¿ywaj¹c okreœlonych rozmiarów*/
		ImageIcon finalImage = new ImageIcon(tmpImage.getImage().getScaledInstance(NewWidth, NewHeight, Image.SCALE_SMOOTH));

		Flags[value] = finalImage;	//Wk³adamy przeskalowany obraz do tablicy z flagami
		return finalImage;
		}
	}

	/*Ustawienie info o statystykach w danym kraju*/
	synchronized private static void SetStats(int val) {

		/*Je¿eli opis ju¿ istnieje, to zostaje ustawiony*/
		if (COVIDInfo[val] != null) {
			 detailedInfoLabel.setText(COVIDInfo[val]);
			return;
		}

		/*Je¿eli nie...*/
		String tmpInfoCountry = "<html>\n";
		URL url = null;

		/*Próba po³¹czenia*/
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
			logger.info(i + " proba po³¹czenia z: " + url);
			
			try {
				res = Jsoup.connect(url.toString()).referrer("http://www.google.pl").execute();
				logger.info("Nawi¹zano po³¹czenie z: " + url);
				connected_flag = true;
			} catch (IOException e) {
				logger.error("Nie mo¿na po³¹czyæ siê z: " + url);
			}
			
			
			if (!connected_flag) {
				i++;
				if (i < attempts + 1) {

					try {
						Thread.sleep(1000); //Uœpienie w¹tku na 1 sec
					} catch (InterruptedException exc) {
					}
				} else {
					logger.fatal("Nie mo¿na po³¹czyæ siê z " + url + "... Zamykanie programu ");
					return;
				}
			}	
		}
		
		logger.info("Parsowanie...");

		Document doc = null;

		try {
			doc = res.parse();	
		} catch (IOException e) {
			logger.fatal("Nie mo¿na parsowaæ strony " + url);
			return;
		}

		Elements tmp = doc.select("div.maincounter-number");
		ArrayList<String> Dane1 = new ArrayList<>();

		for (Element e : tmp) {
			Dane1.add(e.getElementsByTag("span").text());
		}

		tmpInfoCountry += ("<P><B><font size=+4>ZAKA¯ENIA: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(0) + "</font></P><BR>\n");
		tmpInfoCountry += ("<P><B><font size=+4>ZGONY: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(1) + "</font></P><BR>\n");
		tmpInfoCountry += ("<P><B><font size=+4>WYLECZONYCH: </B>&nbsp;&nbsp;<font size=+4>" + Dane1.get(2) + "</font></P><BR>\n");
		tmpInfoCountry += "</html>";

		/*Wpisanie statystyk do tablicy ze statystykami*/
		COVIDInfo[val] = tmpInfoCountry;

		/*Warunek gwarantuj¹cy, ¿e przy szybkim prze³¹czaniu tekst nie bêdzie przeskakiwa³ --> Upewnienie siê, ¿e statystyki pochodz¹ z danego kraju*/
		if (roll.getSelectedIndex() == val) {
			 detailedInfoLabel.setText(tmpInfoCountry);	//ustawienie tesktu w labelu ze statystykami
		} else {
			logger.fatal("Dane siê zmieni³y!");
		}
	}

}