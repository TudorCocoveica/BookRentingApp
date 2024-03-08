package InchiriereCarti;

import LogIn.InterfataLogin;
import Main.LegInterfete;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InterfataInchiriere extends JFrame implements ObserverCarti {

	private JPanel contentPane;
	private JTextField txtBineAiVenit;
	private JPanel panouInchiriere;
	private JPanel panouConfSel;
	private JPanel panouCarte1;
	private JCheckBox bifaCarte1;
	private JTextArea detaliiCarte1;
	private JPanel panouCarte2;
	private JLabel pozaCarte2;
	private JCheckBox bifaCarte2;
	private JTextArea detaliiCarte2;
	private JTextArea descriereCarte2;
	private JPanel panouCarte3;
	private JLabel pozaCarte3;
	private JCheckBox bifaCarte3;
	private JTextArea detaliiCarte3;
	private JTextArea descriereCarte3;
	private JPanel panouCarte4;
	private JLabel pozaCarte4;
	private JCheckBox bifaCarte4;
	private JTextArea detaliiCarte4;
	private JTextArea descriereCarte4;
	private JPanel panouCarte5;
	private JLabel pozaCarte5;
	private JCheckBox bifaCarte5;
	private JTextArea detaliiCarte5;
	private JTextArea descriereCarte5;
	private JPanel panouCarte6;
	private JLabel pozaCarte6;
	private JCheckBox bifaCarte6;
	private JTextArea detaliiCarte6;
	private JTextArea descriereCarte6;
	private JPanel panouCarte7;
	private JLabel pozaCarte7;
	private JCheckBox bifaCarte7;
	private JTextArea detaliiCarte7;
	private JTextArea descriereCarte7;
	private JPanel panouCarte8;
	private JLabel pozaCarte8;
	private JCheckBox bifaCarte8;
	private JTextArea detaliiCarte8;
	private JTextArea descriereCarte8;
	private JPanel panouCarte9;
	private JLabel pozaCarte9;
	private JCheckBox bifaCarte9;
	private JTextArea detaliiCarte9;
	private JTextArea descriereCarte9;
	private JTextField stareC1;
	private JTextField stareC2;
	private JTextField stareC3;
	private JTextField stareC4;
	private JTextField stareC5;
	private JTextField stareC6;
	private JTextField stareC7;
	private JTextField stareC8;
	private JTextField stareC9;

	/**
	 * Variabile pentru path-ul fisierelor text
	 */

	String fisier1 = "D:\\\\facultate\\\\Java\\\\ProiectPIP_biblioteca\\\\src\\\\InchiriereCarti\\\\detaliiCarti";
	String fisier2 = "D:\\\\facultate\\\\Java\\\\ProiectPIP_biblioteca\\\\src\\\\InchiriereCarti\\\\descriereCarti";

	private boolean[] stareaInchiriere = new boolean[9];// Variabila stare Default carti

	public InterfataInchiriere[] carte = new InterfataInchiriere[9]; // Vector de obiecte pentru fiecare carte

	private int numarCartiInchiriate = 0;

	// Lista observeri pentru fiecare carte

	private List<ObserverCarti> observatori = new ArrayList<>();
	private JButton btnInchiriere;
	private JButton btnNewButton;

	/**
	 * Functii Observer
	 */

	public void adaugaObserver(ObserverCarti observer) {
		observatori.add(observer);
	}

	public void stergeObserver(ObserverCarti observer) {
		observatori.remove(observer);
	}

	public void notificaObservatori() {
		for (ObserverCarti observer : observatori) {
			observer.actualizeazaStare(this);
		}
	}

	// Actualizeaza starea curenta a cartii
	public void actualizeazaStare(InterfataInchiriere carte) {

		this.stareaInchiriere = carte.stareaInchiriere();
	}

	public void actualizeazaStareInchiriata(boolean[] stareaInchiriere) {
		this.stareaInchiriere = stareaInchiriere;
		notificaObservatori();
	}

	// Actualizeaza contorul pentru cartile inchiriate
	public void actualizeazaStareTotala(boolean[] stareaInchiriere) {

		numarCartiInchiriate = 0;
		for (boolean stare : stareaInchiriere) {
			if (!stare) {
				numarCartiInchiriate++;
			}
		}

		notificaObservatori();

		// Restrictionarea legata de numarul cartilor ce pot fi inchiriate
		if (numarCartiInchiriate > 2) {

			JOptionPane.showMessageDialog(contentPane, "Puteti inchiria maxim 2 carti in total.");
			return;
		}
	}

	/**
	 * Setarea Cartilor Disponibile pentru inceputul rularii programului
	 */

	public void seteazaToateCartileDisponibile() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("stare_inchiriere.txt"))) {
			for (int i = 0; i < stareaInchiriere.length; i++) {
				stareaInchiriere[i] = true;
				actualizeazaStareCarte("stareC" + (i + 1), true);

				writer.write("true");
				writer.newLine();
			}

			notificaObservatori();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Salvarea disponibilitatii cartilor la logOut
	 * 
	 * @param numeFisier
	 */
	public void salveazaStareaInchirierii(String numeFisier) {

		notificaObservatori();

		try (PrintWriter writer = new PrintWriter(new FileWriter(numeFisier))) {
			for (boolean stare : stareaInchiriere) {
				writer.println(stare);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Citirea din fisier a starii cartilor si actualizarea lor pe interfata
	 * 
	 * @param numeFisier
	 */
	public void incarcaStareaInchirierii(String numeFisier) {
		try (BufferedReader reader = new BufferedReader(new FileReader(numeFisier))) {
			String linie;
			int index = 0;

			while ((linie = reader.readLine()) != null && index < stareaInchiriere.length) {

				boolean stare = Boolean.parseBoolean(linie.trim());
				stareaInchiriere[index++] = stare;
			}

			try (BufferedReader reader2 = new BufferedReader(new FileReader(numeFisier))) {
				index = 1;
				while ((linie = reader2.readLine()) != null && index <= 9) {
					actualizeazaStareCarte("stareC" + index++, Boolean.parseBoolean(linie.trim()));
				}
			}

			notificaObservatori();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void actualizeazaStareCarte(String numeTextField, boolean disponibila) {

		switch (numeTextField) {
		case "stareC1":
			stareC1.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC2":
			stareC2.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC3":
			stareC3.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC4":
			stareC4.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC5":
			stareC5.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC6":
			stareC6.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC7":
			stareC7.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC8":
			stareC8.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		case "stareC9":
			stareC9.setText(disponibila ? "Disponibila" : "Inchiriata");
			break;
		default:
			break;
		}
	}

	public boolean[] stareaInchiriere() {
		return stareaInchiriere;
	}

	public int getNumarCartiInchiriate() {
		return numarCartiInchiriate;
	}

	/**
	 * Extragerea titlului si autorului a fiecarei carti din fisier
	 * 
	 * @param caleFisier
	 * @param numarCarte
	 * @return
	 */

	private String[] citesteDetaliiCarte(String caleFisier, int numarCarte) {
		String[] detalii = new String[2];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(caleFisier));
			String linie;
			for (int i = 0; i < numarCarte * 3; i++) {
				linie = reader.readLine();
				if (linie == null) {
					break;
				}
			}
			detalii[0] = reader.readLine();
			detalii[1] = reader.readLine();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detalii;
	}

	/**
	 * Afisarea starii cartilor in momentul apasarii butonului "Inchiriaza"
	 * 
	 * @param stareCarte
	 * @param stareInchiriere
	 */

	private void afiseazaStare(JTextField stareCarte, boolean stareInchiriere) {
		if (stareInchiriere)
			stareCarte.setText("Disponibila");
		else
			stareCarte.setText("Inchiriata");
	}

	/**
	 * Afisarea pe interfata a titlului si autorului despre carti
	 * 
	 * @param panouCarte
	 * @param detaliiCarte
	 * @param caleDetalii
	 * @param numarCarte
	 */

	private void afiseazaDetaliiCarte(JPanel panouCarte, JTextArea detaliiCarte, String caleDetalii, int numarCarte) {
		String[] detalii = citesteDetaliiCarte(caleDetalii, numarCarte);

		if (detalii.length >= 2) {
			String textAfisat = "Titlu: " + detalii[0] + "\nAutor: " + detalii[1];
			detaliiCarte.setText(textAfisat);
		}
	}

	/**
	 * Citirea din fisier a detaliilor cartilor si afisarea lor pe interfata
	 * 
	 * @param caleFisier
	 * @param textArea
	 * @param numarCarte
	 */

	private void citesteDescriereCarte(String caleFisier, JTextArea textArea, int numarCarte) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(caleFisier));
			String linie;
			int liniiCitite = 0;

			StringBuilder textAfisat = new StringBuilder();

			while ((linie = reader.readLine()) != null) {
				if (liniiCitite / 3 == numarCarte) {
					textAfisat.append(linie).append(System.lineSeparator());
				}
				liniiCitite++;
			}

			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);

			textArea.setText(textAfisat.toString());

			textArea.setAlignmentX(JTextArea.LEFT_ALIGNMENT);
			textArea.setAlignmentY(JTextArea.TOP_ALIGNMENT);

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfataInchiriere frame = new InterfataInchiriere();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Setarea bord-ului de Bine ati venit
	 * 
	 * @param username
	 */
	public void setWelcomeLabel(String username) {
		txtBineAiVenit.setText("Bine ai venit, " + username);
	}

	/**
	 * Create the frame.
	 */

	public InterfataInchiriere() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1142, 833);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panouWelcome = new JPanel();
		panouWelcome.setBackground(new Color(255, 128, 128));
		panouWelcome.setBounds(0, 0, 1126, 101);
		contentPane.add(panouWelcome);
		panouWelcome.setLayout(null);
		txtBineAiVenit = new JTextField();
		txtBineAiVenit.setEditable(false);
		txtBineAiVenit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtBineAiVenit.setBackground(new Color(255, 128, 128));
		txtBineAiVenit.setBounds(10, 11, 240, 79);
		panouWelcome.add(txtBineAiVenit);
		txtBineAiVenit.setColumns(10);
		txtBineAiVenit.setBorder(null);

		panouInchiriere = new JPanel();
		panouInchiriere.setBounds(0, 150, 1126, 644);
		contentPane.add(panouInchiriere);
		GridBagLayout gbl_panouInchiriere = new GridBagLayout();
		gbl_panouInchiriere.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panouInchiriere.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panouInchiriere.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panouInchiriere.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		panouInchiriere.setLayout(gbl_panouInchiriere);

		/**
		 * Panoul Cartii 1
		 */

		panouCarte1 = new JPanel();
		panouCarte1.setBorder(UIManager.getBorder("RadioButton.border"));
		panouCarte1.setLayout(null);
		GridBagConstraints gbc_panouCarte1 = new GridBagConstraints();
		gbc_panouCarte1.insets = new Insets(0, 0, 5, 5);
		gbc_panouCarte1.fill = GridBagConstraints.BOTH;
		gbc_panouCarte1.gridx = 0;
		gbc_panouCarte1.gridy = 0;
		panouInchiriere.add(panouCarte1, gbc_panouCarte1);

		JLabel pozaCarte1 = new JLabel("");
		Image pozeCarti1 = new ImageIcon(this.getClass().getResource("/hdc2.jpg")).getImage();
		pozaCarte1.setIcon(new ImageIcon(pozeCarti1));
		pozaCarte1.setBounds(10, 11, 120, 155);
		panouCarte1.add(pozaCarte1);

		bifaCarte1 = new JCheckBox("");
		bifaCarte1.setBounds(341, 178, 25, 28);
		panouCarte1.add(bifaCarte1);

		detaliiCarte1 = new JTextArea();
		detaliiCarte1.setBounds(140, 11, 224, 42);
		panouCarte1.add(detaliiCarte1);
		afiseazaDetaliiCarte(panouCarte1, detaliiCarte1, fisier1, 0);

		JTextArea descriereCarte1 = new JTextArea();
		descriereCarte1.setBounds(140, 64, 224, 102);
		panouCarte1.add(descriereCarte1);
		citesteDescriereCarte(fisier2, descriereCarte1, 0);

		stareC1 = new JTextField();
		stareC1.setBounds(10, 176, 139, 19);
		panouCarte1.add(stareC1);
		stareC1.setColumns(10);
		afiseazaStare(stareC1, stareaInchiriere[0]);

		/**
		 * Panoul Cartii 2
		 */

		panouCarte2 = new JPanel();
		panouCarte2.setLayout(null);
		panouCarte2.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte2 = new GridBagConstraints();
		gbc_panouCarte2.insets = new Insets(0, 0, 5, 5);
		gbc_panouCarte2.fill = GridBagConstraints.BOTH;
		gbc_panouCarte2.gridx = 1;
		gbc_panouCarte2.gridy = 0;
		panouInchiriere.add(panouCarte2, gbc_panouCarte2);

		pozaCarte2 = new JLabel("");
		pozaCarte2.setBounds(10, 11, 120, 155);
		panouCarte2.add(pozaCarte2);
		Image pozeCarti2 = new ImageIcon(this.getClass().getResource("/potter.jpg")).getImage();
		pozaCarte2.setIcon(new ImageIcon(pozeCarti2));

		bifaCarte2 = new JCheckBox("");
		bifaCarte2.setBounds(341, 178, 25, 28);
		panouCarte2.add(bifaCarte2);

		detaliiCarte2 = new JTextArea();
		detaliiCarte2.setBounds(140, 11, 224, 42);
		panouCarte2.add(detaliiCarte2);
		afiseazaDetaliiCarte(panouCarte2, detaliiCarte2, fisier1, 1);

		descriereCarte2 = new JTextArea();
		descriereCarte2.setWrapStyleWord(true);
		descriereCarte2.setText("");
		descriereCarte2.setLineWrap(true);
		descriereCarte2.setAlignmentY(0.0f);
		descriereCarte2.setAlignmentX(0.0f);
		descriereCarte2.setBounds(140, 64, 224, 102);
		panouCarte2.add(descriereCarte2);
		citesteDescriereCarte(fisier2, descriereCarte2, 1);

		stareC2 = new JTextField();
		stareC2.setColumns(10);
		stareC2.setBounds(10, 176, 139, 19);
		panouCarte2.add(stareC2);
		afiseazaStare(stareC2, stareaInchiriere[1]);

		/**
		 * Panoul Cartii 3
		 */

		panouCarte3 = new JPanel();
		panouCarte3.setLayout(null);
		panouCarte3.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte3 = new GridBagConstraints();
		gbc_panouCarte3.insets = new Insets(0, 0, 5, 0);
		gbc_panouCarte3.fill = GridBagConstraints.BOTH;
		gbc_panouCarte3.gridx = 2;
		gbc_panouCarte3.gridy = 0;
		panouInchiriere.add(panouCarte3, gbc_panouCarte3);

		pozaCarte3 = new JLabel("");
		pozaCarte3.setBounds(10, 11, 120, 155);
		panouCarte3.add(pozaCarte3);
		Image pozeCarti3 = new ImageIcon(this.getClass().getResource("/king.jpg")).getImage();
		pozaCarte3.setIcon(new ImageIcon(pozeCarti3));

		bifaCarte3 = new JCheckBox("");
		bifaCarte3.setBounds(341, 178, 25, 28);
		panouCarte3.add(bifaCarte3);

		detaliiCarte3 = new JTextArea();
		detaliiCarte3.setBounds(140, 11, 224, 42);
		panouCarte3.add(detaliiCarte3);
		afiseazaDetaliiCarte(panouCarte3, detaliiCarte3, fisier1, 2);

		descriereCarte3 = new JTextArea();
		descriereCarte3.setWrapStyleWord(true);
		descriereCarte3.setText("");
		descriereCarte3.setLineWrap(true);
		descriereCarte3.setAlignmentY(0.0f);
		descriereCarte3.setAlignmentX(0.0f);
		descriereCarte3.setBounds(140, 64, 224, 102);
		panouCarte3.add(descriereCarte3);
		citesteDescriereCarte(fisier2, descriereCarte3, 2);

		stareC3 = new JTextField();
		stareC3.setColumns(10);
		stareC3.setBounds(10, 176, 139, 19);
		panouCarte3.add(stareC3);
		afiseazaStare(stareC3, stareaInchiriere[2]);

		/**
		 * Panoul Cartii 4
		 */

		panouCarte4 = new JPanel();
		panouCarte4.setLayout(null);
		panouCarte4.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte4 = new GridBagConstraints();
		gbc_panouCarte4.insets = new Insets(0, 0, 5, 5);
		gbc_panouCarte4.fill = GridBagConstraints.BOTH;
		gbc_panouCarte4.gridx = 0;
		gbc_panouCarte4.gridy = 1;
		panouInchiriere.add(panouCarte4, gbc_panouCarte4);

		pozaCarte4 = new JLabel("");
		pozaCarte4.setBounds(10, 11, 120, 155);
		panouCarte4.add(pozaCarte4);
		Image pozeCarti4 = new ImageIcon(this.getClass().getResource("/poirrot.png")).getImage();
		pozaCarte4.setIcon(new ImageIcon(pozeCarti4));

		bifaCarte4 = new JCheckBox("");
		bifaCarte4.setBounds(341, 178, 25, 28);
		panouCarte4.add(bifaCarte4);

		detaliiCarte4 = new JTextArea();
		detaliiCarte4.setBounds(140, 11, 224, 42);
		panouCarte4.add(detaliiCarte4);
		afiseazaDetaliiCarte(panouCarte4, detaliiCarte4, fisier1, 3);

		descriereCarte4 = new JTextArea();
		descriereCarte4.setWrapStyleWord(true);
		descriereCarte4.setText("");
		descriereCarte4.setLineWrap(true);
		descriereCarte4.setAlignmentY(0.0f);
		descriereCarte4.setAlignmentX(0.0f);
		descriereCarte4.setBounds(140, 64, 224, 102);
		panouCarte4.add(descriereCarte4);
		citesteDescriereCarte(fisier2, descriereCarte4, 3);

		stareC4 = new JTextField();
		stareC4.setColumns(10);
		stareC4.setBounds(10, 176, 139, 19);
		panouCarte4.add(stareC4);
		afiseazaStare(stareC4, stareaInchiriere[3]);

		/**
		 * Panoul Cartii 5
		 */

		panouCarte5 = new JPanel();
		panouCarte5.setLayout(null);
		panouCarte5.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte5 = new GridBagConstraints();
		gbc_panouCarte5.insets = new Insets(0, 0, 5, 5);
		gbc_panouCarte5.fill = GridBagConstraints.BOTH;
		gbc_panouCarte5.gridx = 1;
		gbc_panouCarte5.gridy = 1;
		panouInchiriere.add(panouCarte5, gbc_panouCarte5);

		pozaCarte5 = new JLabel("");
		pozaCarte5.setBounds(10, 11, 120, 155);
		panouCarte5.add(pozaCarte5);
		Image pozeCarti5 = new ImageIcon(this.getClass().getResource("/legenda.png")).getImage();
		pozaCarte5.setIcon(new ImageIcon(pozeCarti5));

		bifaCarte5 = new JCheckBox("");
		bifaCarte5.setBounds(341, 178, 25, 28);
		panouCarte5.add(bifaCarte5);

		detaliiCarte5 = new JTextArea();
		detaliiCarte5.setBounds(140, 11, 224, 42);
		panouCarte5.add(detaliiCarte5);
		afiseazaDetaliiCarte(panouCarte5, detaliiCarte5, fisier1, 4);

		descriereCarte5 = new JTextArea();
		descriereCarte5.setWrapStyleWord(true);
		descriereCarte5.setText("");
		descriereCarte5.setLineWrap(true);
		descriereCarte5.setAlignmentY(0.0f);
		descriereCarte5.setAlignmentX(0.0f);
		descriereCarte5.setBounds(140, 64, 224, 102);
		panouCarte5.add(descriereCarte5);
		citesteDescriereCarte(fisier2, descriereCarte5, 4);

		stareC5 = new JTextField();
		stareC5.setColumns(10);
		stareC5.setBounds(10, 176, 139, 19);
		panouCarte5.add(stareC5);
		afiseazaStare(stareC5, stareaInchiriere[4]);

		/**
		 * Panoul Cartii 6
		 */

		panouCarte6 = new JPanel();
		panouCarte6.setLayout(null);
		panouCarte6.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte6 = new GridBagConstraints();
		gbc_panouCarte6.insets = new Insets(0, 0, 5, 0);
		gbc_panouCarte6.fill = GridBagConstraints.BOTH;
		gbc_panouCarte6.gridx = 2;
		gbc_panouCarte6.gridy = 1;
		panouInchiriere.add(panouCarte6, gbc_panouCarte6);

		pozaCarte6 = new JLabel("");
		pozaCarte6.setBounds(10, 11, 120, 155);
		panouCarte6.add(pozaCarte6);
		Image pozeCarti6 = new ImageIcon(this.getClass().getResource("/rings.jpg")).getImage();
		pozaCarte6.setIcon(new ImageIcon(pozeCarti6));

		bifaCarte6 = new JCheckBox("");
		bifaCarte6.setBounds(341, 178, 25, 28);
		panouCarte6.add(bifaCarte6);

		detaliiCarte6 = new JTextArea();
		detaliiCarte6.setBounds(140, 11, 224, 42);
		panouCarte6.add(detaliiCarte6);
		afiseazaDetaliiCarte(panouCarte6, detaliiCarte6, fisier1, 5);

		descriereCarte6 = new JTextArea();
		descriereCarte6.setWrapStyleWord(true);
		descriereCarte6.setText("");
		descriereCarte6.setLineWrap(true);
		descriereCarte6.setAlignmentY(0.0f);
		descriereCarte6.setAlignmentX(0.0f);
		descriereCarte6.setBounds(140, 64, 224, 102);
		panouCarte6.add(descriereCarte6);
		citesteDescriereCarte(fisier2, descriereCarte6, 5);

		stareC6 = new JTextField();
		stareC6.setColumns(10);
		stareC6.setBounds(10, 176, 139, 19);
		panouCarte6.add(stareC6);
		afiseazaStare(stareC6, stareaInchiriere[5]);

		/**
		 * Panoul Cartii 7
		 */

		panouCarte7 = new JPanel();
		panouCarte7.setLayout(null);
		panouCarte7.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte7 = new GridBagConstraints();
		gbc_panouCarte7.insets = new Insets(0, 0, 0, 5);
		gbc_panouCarte7.fill = GridBagConstraints.BOTH;
		gbc_panouCarte7.gridx = 0;
		gbc_panouCarte7.gridy = 2;
		panouInchiriere.add(panouCarte7, gbc_panouCarte7);

		pozaCarte7 = new JLabel("");
		pozaCarte7.setBounds(10, 11, 120, 155);
		panouCarte7.add(pozaCarte7);
		Image pozeCarti7 = new ImageIcon(this.getClass().getResource("/fra.jpg")).getImage();
		pozaCarte7.setIcon(new ImageIcon(pozeCarti7));

		bifaCarte7 = new JCheckBox("");
		bifaCarte7.setBounds(341, 178, 25, 28);
		panouCarte7.add(bifaCarte7);

		detaliiCarte7 = new JTextArea();
		detaliiCarte7.setBounds(140, 11, 224, 42);
		panouCarte7.add(detaliiCarte7);
		afiseazaDetaliiCarte(panouCarte7, detaliiCarte7, fisier1, 6);

		descriereCarte7 = new JTextArea();
		descriereCarte7.setWrapStyleWord(true);
		descriereCarte7.setText("");
		descriereCarte7.setLineWrap(true);
		descriereCarte7.setAlignmentY(0.0f);
		descriereCarte7.setAlignmentX(0.0f);
		descriereCarte7.setBounds(140, 64, 224, 102);
		panouCarte7.add(descriereCarte7);
		citesteDescriereCarte(fisier2, descriereCarte7, 6);

		stareC7 = new JTextField();
		stareC7.setColumns(10);
		stareC7.setBounds(10, 176, 139, 19);
		panouCarte7.add(stareC7);
		afiseazaStare(stareC7, stareaInchiriere[6]);

		/**
		 * Panoul Cartii 8
		 */

		panouCarte8 = new JPanel();
		panouCarte8.setLayout(null);
		panouCarte8.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte8 = new GridBagConstraints();
		gbc_panouCarte8.insets = new Insets(0, 0, 0, 5);
		gbc_panouCarte8.fill = GridBagConstraints.BOTH;
		gbc_panouCarte8.gridx = 1;
		gbc_panouCarte8.gridy = 2;
		panouInchiriere.add(panouCarte8, gbc_panouCarte8);

		pozaCarte8 = new JLabel("");
		pozaCarte8.setBounds(10, 11, 120, 155);
		panouCarte8.add(pozaCarte8);
		Image pozeCarti8 = new ImageIcon(this.getClass().getResource("/dune.jpg")).getImage();
		pozaCarte8.setIcon(new ImageIcon(pozeCarti8));

		bifaCarte8 = new JCheckBox("");
		bifaCarte8.setBounds(341, 178, 25, 28);
		panouCarte8.add(bifaCarte8);

		detaliiCarte8 = new JTextArea();
		detaliiCarte8.setBounds(140, 11, 224, 42);
		panouCarte8.add(detaliiCarte8);
		afiseazaDetaliiCarte(panouCarte8, detaliiCarte8, fisier1, 7);

		descriereCarte8 = new JTextArea();
		descriereCarte8.setWrapStyleWord(true);
		descriereCarte8.setText("");
		descriereCarte8.setLineWrap(true);
		descriereCarte8.setAlignmentY(0.0f);
		descriereCarte8.setAlignmentX(0.0f);
		descriereCarte8.setBounds(140, 64, 224, 102);
		panouCarte8.add(descriereCarte8);
		citesteDescriereCarte(fisier2, descriereCarte8, 7);

		stareC8 = new JTextField();
		stareC8.setColumns(10);
		stareC8.setBounds(10, 176, 139, 19);
		panouCarte8.add(stareC8);
		afiseazaStare(stareC8, stareaInchiriere[7]);

		/**
		 * Panoul Cartii 9
		 */

		panouCarte9 = new JPanel();
		panouCarte9.setLayout(null);
		panouCarte9.setBorder(UIManager.getBorder("RadioButton.border"));
		GridBagConstraints gbc_panouCarte9 = new GridBagConstraints();
		gbc_panouCarte9.fill = GridBagConstraints.BOTH;
		gbc_panouCarte9.gridx = 2;
		gbc_panouCarte9.gridy = 2;
		panouInchiriere.add(panouCarte9, gbc_panouCarte9);

		pozaCarte9 = new JLabel("");
		pozaCarte9.setBounds(10, 11, 120, 155);
		panouCarte9.add(pozaCarte9);
		Image pozeCarti9 = new ImageIcon(this.getClass().getResource("/narnia.jpg")).getImage();
		pozaCarte9.setIcon(new ImageIcon(pozeCarti9));

		bifaCarte9 = new JCheckBox("");
		bifaCarte9.setBounds(341, 178, 25, 28);
		panouCarte9.add(bifaCarte9);

		detaliiCarte9 = new JTextArea();
		detaliiCarte9.setBounds(140, 11, 224, 42);
		panouCarte9.add(detaliiCarte9);
		afiseazaDetaliiCarte(panouCarte9, detaliiCarte9, fisier1, 8);

		descriereCarte9 = new JTextArea();
		descriereCarte9.setWrapStyleWord(true);
		descriereCarte9.setText("");
		descriereCarte9.setLineWrap(true);
		descriereCarte9.setAlignmentY(0.0f);
		descriereCarte9.setAlignmentX(0.0f);
		descriereCarte9.setBounds(140, 64, 224, 102);
		panouCarte9.add(descriereCarte9);
		citesteDescriereCarte(fisier2, descriereCarte9, 8);

		stareC9 = new JTextField();
		stareC9.setColumns(10);
		stareC9.setBounds(10, 176, 139, 19);
		panouCarte9.add(stareC9);
		afiseazaStare(stareC9, stareaInchiriere[8]);

		panouConfSel = new JPanel();
		panouConfSel.setBounds(0, 100, 1126, 51);
		contentPane.add(panouConfSel);
		panouConfSel.setLayout(null);

		btnInchiriere = new JButton("Inchiriaza");
		btnInchiriere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numarCartiSelectate = 0;

				for (int i = 0; i < stareaInchiriere.length; i++) {
					JCheckBox checkbox = null;
					String numeBifa = "bifaCarte" + (i + 1);
					String numeStareTextField = "stareC" + (i + 1);

					try {
						Field field1 = InterfataInchiriere.class.getDeclaredField(numeBifa);
						checkbox = (JCheckBox) field1.get(InterfataInchiriere.this);

						Field field2 = InterfataInchiriere.class.getDeclaredField(numeStareTextField);
						field2.setAccessible(true);
						JTextField stareTextFieldCurent = (JTextField) field2.get(InterfataInchiriere.this);

						if (checkbox.isSelected()) {
							if (!stareaInchiriere[i]) {
								JOptionPane.showMessageDialog(contentPane,
										"Cartea " + (i + 1) + " nu este disponibila.");
								checkbox.setSelected(false);
								return;
							}

							numarCartiInchiriate++;

							if (numarCartiInchiriate > 2) {
								JOptionPane.showMessageDialog(contentPane, "Puteti inchiria maxim 2 carti in total.");

								return;
							}

							stareaInchiriere[i] = false;
							actualizeazaStareInchiriata(stareaInchiriere);
							System.out.println("S-a inchiriat cartea numarul " + (i + 1));
							stareTextFieldCurent.setText("Inchiriata");

						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}
		});
		btnInchiriere.setBackground(new Color(255, 128, 128));
		btnInchiriere.setFont(new Font("Arial", Font.PLAIN, 15));
		btnInchiriere.setBounds(10, 10, 180, 31);
		panouConfSel.add(btnInchiriere);

		btnNewButton = new JButton("Log OUT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				salveazaStareaInchirierii("stare_inchiriere.txt");

				setVisible(false);
				InterfataLogin interfataLogin = new InterfataLogin();

				// Deschiderea interfetei de login

				interfataLogin.setVisible(true);

			}
		});
		btnNewButton.setForeground(new Color(255, 128, 128));
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 14));
		btnNewButton.setBounds(979, 9, 110, 35);
		panouConfSel.add(btnNewButton);
	}
}
