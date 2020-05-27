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

	private final static String[] countries = { "Swiat", "Niemcy", "Rosja", "Polska", "Slowacja", "USA", "Brazylia",
												"Hiszpania", "UK", "Wlochy", "Francja", "Turcja", "Indie", "Iran", "Kanada", "Chiny", "Chile" };

	private final static String[] countriesURL = { "world", "germany", "russia", "poland", "slovakia", "us", "brazil", "spain",
												   "uk", "italy", "france", "turkey", "india", "iran", "canada", "china", "chile" };

	private static String[] COVIDInfo = new String[countries.length];
	private static Icon[] Flags = new Icon[countries.length];
	final static JComboBox<String> roll = new JComboBox<String>(countries);
	private static JLabel detailedInfo = new JLabel();
	private static ExecutorService pool = Executors.newFixedThreadPool(5); //Wielow¹tkowoœæ
	
	
	public static void CreateAndShowGUI() throws MalformedURLException {

		/*Utworzenie ramki g³ównej --> W konstruktorze napis w belce*/
		JFrame f = new JFrame("COVID-19");

		
		final JPanel flagVsStats = new JPanel(new CardLayout());	//Panel prze³¹czaj¹cy miêdzy flagami a statystykami
		final JButton NextButton = new JButton();					//NASTÊPNY
		final JButton PreviousButton = new JButton();				//POPRZEDNI
		final JButton PicFromURL = new JButton();					//Przycisk pobieraj¹cy grafike z URL
		final JButton MainTitle = new JButton();					//Nag³ówek

		
		/* ACTION LISTENER */
		ActionListener MyActionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int i = roll.getSelectedIndex();

				
				/*MAIN TITLE LISTENER --> Powrót do statystyk ze œwiata*/
				if (e.getSource().equals(MainTitle)) {
					roll.setSelectedIndex(WORLD);
					return;
				}
				
				/*MAIN PICTURE LISTENER --> Wyœwietlenie grafiki z URL*/
				if (e.getSource().equals(PicFromURL)) {

					try {
						new ObrazFrame();
						
					} catch (IOException e1) {
						logger.error("Exception --> Can't display pic from URL");
					}
				}
				
				/*NEXT BUTTON LISTENER*/
				if (e.getSource().equals(NextButton)) {
					i++;
					if (i >= countries.length) {i = 0;}
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

			public void mousePressed(MouseEvent e) {
				CardLayout card = (CardLayout) flagVsStats.getLayout();
				card.previous(flagVsStats);									//Prze³¹czenie pomiêdzy 2 kartami (flaga i statystyki)
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

		/*Przypisanie grafik do przycisków*/
		NextButton.setIcon(NextIcon);
		PreviousButton.setIcon(PreviousIcon);
		PicFromURL.setIcon(PicFromUrlIcon);
		MainTitle.setIcon(TitleIcon);

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

		roll.setSelectedIndex(WORLD);
		roll.setBackground(Color.WHITE);
		roll.setForeground(Color.BLACK);
		roll.setFont(new Font("Helvetica", Font.BOLD, 25));
		
		
		/*Panel tytulowy*/
		JPanel MainPanel = new JPanel(new GridLayout());
		MainPanel.setBackground(Color.DARK_GRAY);
		MainPanel.add(MainTitle);
		MainPanel.add(PicFromURL);

		/*Panel sterujacy*/
		JPanel SwitchPanel = new JPanel(new GridLayout());
		SwitchPanel.setBackground(Color.DARK_GRAY);

		/*Do³¹czenie do panelu przycisków i Comboboxa*/
		SwitchPanel.add(PreviousButton);
		SwitchPanel.add(roll);
		SwitchPanel.add(NextButton);
		
		/*Obramowanie SwitchPanelu*/
		SwitchPanel.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder(null, "SwitchPanel", TitledBorder.DEFAULT_JUSTIFICATION,
								TitledBorder.DEFAULT_POSITION, null, Color.LIGHT_GRAY),
								BorderFactory.createEmptyBorder(10, 0, 10, 0)));
		
		
		/*G³ówny panel*/
		JPanel FlagPanel = new JPanel(new BorderLayout());
		FlagPanel.setBackground(Color.DARK_GRAY);
		FlagPanel.setPreferredSize(new Dimension(Height, Width));
		
		final JLabel FlagLabel = new JLabel(SetFlag(WORLD));	//Na pocz¹tku by³ BigBang
		FlagPanel.add(FlagLabel);									//Dodanie FlagLabela do FlagPanelu
		FlagPanel.addMouseListener(myMouseListener);

		JPanel StatsPanel = new JPanel(new GridLayout());
		StatsPanel.setPreferredSize(new Dimension(Height, Width));
		StatsPanel.setBackground(Color.DARK_GRAY);
		StatsPanel.setForeground(Color.WHITE);
		StatsPanel.addMouseListener(myMouseListener);

		StatsPanel.setBorder(BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder(null, "Statystyki", TitledBorder.DEFAULT_JUSTIFICATION,
								TitledBorder.DEFAULT_POSITION, null, Color.LIGHT_GRAY),
								BorderFactory.createEmptyBorder(10, 50, 10, 0)));

		 detailedInfo.setForeground(Color.WHITE);
		 StatsPanel.add(detailedInfo);					//Dodanie informacji do panelu ze statystykami

		/*Wielow¹tkowoœæ*/
		pool.submit(new Runnable() {
			public void run() {
				SetStats(WORLD);
			}
		});

		ItemListener myItemListener = new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					 FlagLabel.setIcon(SetFlag(roll.getSelectedIndex()));

					pool.submit(new Runnable() {
						public void run() {
							SetStats(roll.getSelectedIndex());
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
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);

	}

	/*Ustawienie flag*/
	private static Icon SetFlag(int value) {
		
		/*Je¿eli jest flaga to j¹ ustawiamy*/
		if (Flags[value] != null) {
			return Flags[value];
		
		/*Je¿eli nie ma, to szukamy j¹ w folderze*/
		}else {
		String tmp = "FLAGS/" + countries[value].toLowerCase() + ".png";	//Œcie¿ka do flagi w .png
		
		ImageIcon tmpImage = new ImageIcon(tmp);

		float dzielnik = Math.max(tmpImage.getIconHeight() / (float) Height, tmpImage.getIconWidth() / (float) Width);	//Dostosowanie rozmiaru do okna

		/*Dostosowane rozmiary flagi*/
		int NewHeight = (int) (0.5 + tmpImage.getIconHeight() / dzielnik);
		int NewWidth = (int) (0.5 + tmpImage.getIconWidth() / dzielnik);

		ImageIcon image = new ImageIcon(tmpImage.getImage().getScaledInstance(NewWidth, NewHeight, Image.SCALE_SMOOTH));

		Flags[value] = image;
		return image;
		}
	}

	/*Ustawienie info o statystykach w danym kraju*/
	synchronized private static void SetStats(int val) {

		if (COVIDInfo[val] != null) {
			 detailedInfo.setText(COVIDInfo[val]);
			return;
		}

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
			logger.error("Problem with URL" + url, e);
			return;
		}

		boolean connected = false;
		Response res = null;
		int i = 0;
		int attempts = 5;
		
		while(connected != true) {
			logger.info("Proba po³¹czenia z: " + url);
			
			try {
				res = Jsoup.connect(url.toString()).execute();
				logger.info("Nawi¹zano po³¹czenie z: " + url);
				connected = true;
			} catch (IOException e) {
				logger.error("Nie mo¿na po³¹czyæ siê z: " + url);
			}
			
			
			if (!connected) {
				if (i < attempts) {

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
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
			logger.fatal("Nie mo¿na parsowaæ strony " + url, e);
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

		COVIDInfo[val] = tmpInfoCountry;

		if (roll.getSelectedIndex() == val) {
			 detailedInfo.setText(tmpInfoCountry);
		} else {
			logger.fatal("Dane siê zmieni³y!");
		}
	}

}