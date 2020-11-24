/**
 * 
 */
package gui.subwindows.home;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.components.DialogHandler;

/**
 * First panel of the application. Ask for user to connect with login and password.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence Hennekein
 */
public class ConnexionPanel extends JPanel {

	private MainWindow context;
	
	/**
	 * TITLE 
	 */
	private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, GuiConstants.HEIGHT / 5);
	private JPanel titlePanel = new JPanel();
	private JLabel titlelabel = new JLabel("Drivepicerie - Connexion", SwingConstants.CENTER);
	
	/**
	 * TEXT FIELDS
	 */
	private final Dimension FIELDS_DIMENSION = new Dimension(GuiConstants.WIDTH, GuiConstants.HEIGHT / 3);
	private final Dimension TEXTFIELD_DIMENSION = new Dimension(FIELDS_DIMENSION.width / 5, FIELDS_DIMENSION.height / 10);
	private JPanel fieldsPanel = new JPanel();
	private JLabel loginLabel = new JLabel("Identifiant", SwingConstants.CENTER);
	private JLabel passwordLabel = new JLabel("Mot de passe", SwingConstants.CENTER);
	private JTextField loginTextArea = new JTextField();
	private JCheckBox rememberCheckBox = new JCheckBox("Se souvenir de Moi", true);
	private final String PATH_LOGIN = "./data/loginLog.txt";
	private String rememberedId = null;
	private JTextField passwordTextArea = new JPasswordField();
	private final Dimension FIELDS_FILLER = new Dimension(GuiConstants.WIDTH, GuiConstants.HEIGHT / 8);
	
	/**
	 * BUTTONS
	 */
	private final Dimension BUTTONS_DIMENSION = new Dimension(GuiConstants.WIDTH, GuiConstants.HEIGHT / 4);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 6, BUTTONS_DIMENSION.height / 4);
	private JPanel buttonsPanel = new JPanel();
	private JButton connectionButton = new JButton("Connexion");
	private JButton connectionAdminButton = new JButton("Connexion en tant qu'Administrateur");
	private final Dimension BUTTON_FILLER = new Dimension(2 * GuiConstants.WIDTH / 9, BUTTONS_DIMENSION.height);

	
	public ConnexionPanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		
		initTitle();
		initFields();
		initButtons();
		
		add(titlePanel, BorderLayout.NORTH);
		add(fieldsPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}

	private void initTitle() {
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		titlelabel.setFont(new Font("Serif", Font.BOLD, TITLE_DIMENSION.height / 2));
		titlePanel.add(titlelabel);
	}

	private void initFields() {
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		fieldsPanel.setPreferredSize(FIELDS_DIMENSION);
		
		//alignement center
		loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		loginTextArea.setMaximumSize(TEXTFIELD_DIMENSION);
		getSavedLogin();
		loginTextArea.setText(rememberedId);
		rememberCheckBox.setAlignmentX(CENTER_ALIGNMENT);
		passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordTextArea.setMaximumSize(TEXTFIELD_DIMENSION);
		
		//add them to the fields panel
		fieldsPanel.add(Box.createRigidArea(FIELDS_FILLER));
		fieldsPanel.add(loginLabel);
		fieldsPanel.add(loginTextArea);
		fieldsPanel.add(rememberCheckBox);
		fieldsPanel.add(Box.createRigidArea(FIELDS_FILLER));
		fieldsPanel.add(passwordLabel);
		fieldsPanel.add(passwordTextArea);
		fieldsPanel.add(Box.createRigidArea(FIELDS_FILLER));
	}

	private void initButtons() {
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);

		//alignement
		connectionButton.setAlignmentX(CENTER_ALIGNMENT);
		connectionButton.setAlignmentY(CENTER_ALIGNMENT);
		connectionButton.setMaximumSize(BUTTON_SIZE);
		
		connectionAdminButton.setAlignmentX(CENTER_ALIGNMENT);
		connectionAdminButton.setAlignmentY(CENTER_ALIGNMENT);
		connectionAdminButton.setMaximumSize(BUTTON_SIZE);
		
		//handle connection
		connectionButton.addActionListener(new ActionConnection());
		connectionAdminButton.addActionListener(new ActionAdminConnection());
		
		//add button
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(connectionButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(connectionAdminButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
	}
	
	class ActionConnection implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Lancer la connexion
				//Récupérer combo Id / mdp
			String Id = loginTextArea.getText();
			String Mdp = passwordTextArea.getText();
			passwordTextArea.setText(null);
			boolean answer;
				//ServerConnectionHandler
			answer = context.launchConnection(Id, Mdp, false);
			if (answer) {
				rememberedId = rememberCheckBox.isSelected()? Id: null;
				setSavedLogin(rememberedId);
				loginTextArea.setText(rememberedId);
				context.changeWindow(WindowName.MENU);
			}
		}
	}
	
	class ActionAdminConnection implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//Lancer la connexion
				//Récupérer combo Id / mdp
			String Id = loginTextArea.getText();
			String Mdp = passwordTextArea.getText();
			passwordTextArea.setText(null);
			boolean answer;
				//ServerConnectionHandler
			answer = context.launchConnection(Id, Mdp, true);
			if (answer) {
				rememberedId = rememberCheckBox.isSelected()? Id: null;
				setSavedLogin(rememberedId);
				loginTextArea.setText(rememberedId);
				context.changeWindow(WindowName.MENU);
			}
		}
	}
	
	private void getSavedLogin() {
		BufferedReader reader;
		File file = new File(PATH_LOGIN);
		try {
			if (file.exists()) {
				reader = new BufferedReader(new FileReader(file));
				rememberedId = reader.readLine();
				MainWindow.logger.info("Utilisateur enregistré trouvé: "+rememberedId);
				reader.close();
			}
			else {
				//File was not created, try to create a new one
				setSavedLogin("");
			}
		} catch (FileNotFoundException e1) {
			MainWindow.logger.error("Fichier où est sauvegardé le login enregistré non trouvé, création...");
			setSavedLogin("");
		} catch (IOException e2) {
			MainWindow.logger.error("Impossible d'ouvrir le fichier " + file.getAbsolutePath());
			DialogHandler.showErrorDialog(context, "Erreur", "Impossible d'ouvrir le fichier où est sauvegardé votre login: " + file.getAbsolutePath());
		}
		
	}
	
	private void setSavedLogin(String rememberId) {
		FileWriter writer;
		File file = new File(PATH_LOGIN);
		try {
			if (file.exists()) {
				writer = new FileWriter(file);
				if (! (rememberId == null || rememberId.equals(""))) {
					writer.write(rememberId);
					MainWindow.logger.info("Sauvegarde du nom de l'utilisateur: "+rememberId);
				}
				writer.close();
			}
			else {
				//try to create all dirs to file
				String[] path = PATH_LOGIN.split("/");
				String completePath = path[0];
				for (int i = 1; (i < path.length - 1) && (i < 5); i++) {
					completePath += "/" + path[i];
				}
				File dir = new File(completePath);
				dir.mkdirs();
				
				//lets try again
				if (file.exists()) {
					writer = new FileWriter(file);
					if (! (rememberId == null || rememberId.equals(""))) {
						writer.write(rememberId);
						MainWindow.logger.info("Sauvegarde du nom de l'utilisateur: "+rememberId);
					}
					writer.close();
				}
			}
		} catch (IOException e) {
			MainWindow.logger.error("Erreur dans la création du fichier " + file.getAbsolutePath());
			DialogHandler.showErrorDialog(context, "Erreur", "Impossible d'ouvrir le fichier où est sauvegardé votre login: " + file.getAbsolutePath());
		}
	}
	
}
