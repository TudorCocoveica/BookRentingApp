package LogIn;

import InchiriereCarti.InterfataInchiriere;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.awt.event.ActionEvent;

public class InterfataLogin extends JFrame {

	// Path-ul datelor de logare
	File f = new File("d:\\facultate\\Java\\ProiectPIP_biblioteca\\DateLogin");

	int ln;
	boolean dateCorecte = false;
	String Username, Password, Email;
	//
	private JPanel contentPane;
	private JTextField tfusr;
	private JTextField tfpswd;
	private JTextField tfmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfataLogin frame = new InterfataLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Functionalitatile acestui JFrame - InterfataLogin
	 */

	protected void createFolder() {

		if (!f.exists()) {
			f.mkdirs();
		}

	}

	/**
	 * Crearea fisierului cu datele de LogIn in calculatorul clientului
	 */

	protected void readFile() {
		try {
			FileReader fr = new FileReader(f + "\\LogIns.txt");
			System.out.println("file exists!");
		} catch (FileNotFoundException e) {
			try {
				FileWriter fw = new FileWriter(f + "\\LogIns.txt");
				System.out.println("File Created");
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
	}

	/**
	 * Adaugarea datelor de logare in fisier
	 * 
	 * @param usr  - userName
	 * @param pswd - parola
	 * @param mail
	 * @return
	 */
	protected int addData(String usr, String pswd, String mail) {
		try {
			RandomAccessFile raf = new RandomAccessFile(f + "\\LogIns.txt", "rw");

			// Verificarea ca datele de inregistrare introduse sa fie unice
			for (int i = 0; i < ln; i++) {
				String line = raf.readLine();
				if (line == null) {
					break;
				}

				String existingUsername = "";
				String existingEmail = "";

				// Verificarea ca linia curenta sa nu fie goala

				if (line.length() > 9 && line.startsWith("Username:")) {
					existingUsername = line.substring(9).trim();
				} else {
					continue;
				}

				raf.readLine(); // Salt peste linia parolei

				String emailLine = raf.readLine();

				// Verificarea ca linia curenta sa nu fie goala
				if (emailLine != null && emailLine.length() > 6) {
					existingEmail = emailLine.substring(6).trim();
				}

				if (usr.equalsIgnoreCase(existingUsername)
						&& (mail.equalsIgnoreCase(existingEmail) && !mail.isEmpty())) {
					new CustomMessageBox("Username AND Email already exists");
					return 0;
				} else if (usr.equalsIgnoreCase(existingUsername)) {
					new CustomMessageBox("Username already exists");
					return 0;
				} else if (mail.equalsIgnoreCase(existingEmail) && !mail.isEmpty()) {
					new CustomMessageBox("Email already exists");
					return 0;
				}

			}

			if (mail.contains("@gmail.com") & !usr.isEmpty() & !pswd.isEmpty()) {
				raf.writeBytes("\r\n");
				raf.writeBytes("\r\n");
				raf.writeBytes("Username:" + usr + "\r\n");
				raf.writeBytes("Password:" + pswd + "\r\n");
				raf.writeBytes("Email:" + mail);
				return 1;

			} else {
				new CustomMessageBox("DATE INCORECTE");
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return 0;

	}

	// Getter username

	public String getUsername() {
		return tfusr.getText();
	}

	// Verificare date corecte

	public boolean ok() {
		return dateCorecte;
	}

	protected void logic(String usr, String pswd) {
		try {
			RandomAccessFile raf = new RandomAccessFile(f + "\\LogIns.txt", "rw");
			for (int i = 0; i < ln; i++) {
				String line = raf.readLine();
				if (line == null) {
					break;
				}

				// Extragerea datelor
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String forUser = parts[1].trim();

					String passwordLine = raf.readLine();
					String[] passwordParts = passwordLine.split(":");
					if (passwordParts.length == 2) {
						String forPswd = passwordParts[1].trim();
						raf.readLine();

						// Verificam daca datele coincid
						if (usr.equals(forUser) && pswd.equals(forPswd)) {
							getUsername();
							dateCorecte = true;
							setVisible(false);

							/**
							 * Deschiderea interfetei de inchiriat carti + borderul de Bine ai venit
							 */
							InterfataInchiriere paginaInchiriere = new InterfataInchiriere();
							paginaInchiriere.incarcaStareaInchirierii("stare_inchiriere.txt");
							paginaInchiriere.setVisible(true);
							paginaInchiriere.setWelcomeLabel(getUsername());

							return;
						}
					}
				}
			}

			new CustomMessageBox("Incorrect user/password");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void countLines() {
		try {
			ln = 1;
			RandomAccessFile raf = new RandomAccessFile(f + "\\LogIns.txt", "rw");
			for (int i = 0; raf.readLine() != null; i++) {
				ln++;
			}
			System.out.println("number of lines:" + ln);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	//
	public InterfataLogin() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 598, 485);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 128, 128));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Bine ai venit !");
		lblNewLabel.setBounds(10, 53, 574, 36);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBackground(new Color(255, 182, 193));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_2 = new JLabel("User Name");
		lblNewLabel_2.setFont(new Font("Arial", Font.ITALIC, 20));
		lblNewLabel_2.setBounds(113, 139, 111, 36);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Arial", Font.ITALIC, 20));
		lblNewLabel_1.setBounds(113, 198, 125, 36);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_3 = new JLabel("Email");
		lblNewLabel_3.setFont(new Font("Arial", Font.ITALIC, 20));
		lblNewLabel_3.setBounds(113, 247, 98, 49);
		contentPane.add(lblNewLabel_3);
		lblNewLabel_3.setVisible(false);

		tfusr = new JTextField();
		tfusr.setBounds(253, 139, 199, 37);
		contentPane.add(tfusr);
		tfusr.setColumns(10);

		tfpswd = new JTextField();
		tfpswd.setColumns(10);
		tfpswd.setBounds(253, 198, 199, 37);
		contentPane.add(tfpswd);

		tfmail = new JTextField();
		tfmail.setColumns(10);
		tfmail.setBounds(253, 256, 199, 37);
		contentPane.add(tfmail);
		tfmail.setVisible(false);

		/**
		 * BUTON LOGIN
		 */

		JButton btnLogin = new JButton("LogIn");
		btnLogin.setVisible(true);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createFolder();
				readFile();
				countLines();
				logic(tfusr.getText(), tfpswd.getText()); // testeaza datele pentru logIn
				//

			}
		});
		btnLogin.setForeground(new Color(255, 128, 128));
		btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
		btnLogin.setBounds(238, 360, 125, 36);
		contentPane.add(btnLogin);

		/**
		 * BUTON REGISTER
		 */

		JButton btnRegister = new JButton("Register");
		btnRegister.setVisible(false);
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				createFolder();
				readFile();
				countLines();
				int ok = addData(tfusr.getText(), tfpswd.getText(), tfmail.getText()); // adauga datele din text field
																						// in fisierul meu txt de date

				if (ok == 1) {
					btnRegister.setVisible(false);
					btnLogin.setVisible(true);
					tfmail.setVisible(false);
					lblNewLabel_3.setVisible(false);

				}

			}
		});
		btnRegister.setForeground(new Color(255, 128, 128));
		btnRegister.setFont(new Font("Arial", Font.BOLD, 18));
		btnRegister.setBounds(238, 360, 125, 36);
		contentPane.add(btnRegister);

		/**
		 * BUTON DONT HAVE AN ACCOUNT
		 */

		JButton btnNewButton = new JButton("Don't Have an Account");
		btnNewButton.setVisible(true);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tfmail.setVisible(true);
				lblNewLabel_3.setVisible(true);

				btnLogin.setVisible(false);
				btnRegister.setVisible(true);
				btnNewButton.setVisible(false);

			}
		});
		btnNewButton.setFont(new Font("Arial", Font.BOLD, 12));
		btnNewButton.setForeground(new Color(255, 128, 128));
		btnNewButton.setBounds(21, 360, 170, 36);
		contentPane.add(btnNewButton);

		/**
		 * BUTON CLEAR ALL
		 */

		JButton btnNewButton_1_1 = new JButton("Clear All");
		btnNewButton_1_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				tfusr.setText("");
				tfpswd.setText("");
				tfmail.setText("");

			}
		});
		btnNewButton_1_1.setForeground(new Color(255, 128, 128));
		btnNewButton_1_1.setFont(new Font("Arial", Font.BOLD, 18));
		btnNewButton_1_1.setBounds(426, 360, 125, 36);
		contentPane.add(btnNewButton_1_1);
	}

}
