/**
 * 
 */
package gui.subwindows.home;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import gui.GuiConstants;
import gui.MainWindow;

/**
 * First panel of the application. Ask for user to connect with login and password.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
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
	private final Dimension FIELDS_DIMENSION = new Dimension(GuiConstants.WIDTH / 3, GuiConstants.HEIGHT / 3);
	private final Dimension TEXTFIELD_DIMENSION = new Dimension(FIELDS_DIMENSION.width / 3, FIELDS_DIMENSION.height / 8);
	private JPanel fieldsPanel = new JPanel();
	private JLabel loginLabel = new JLabel("Identifiant", SwingConstants.CENTER);
	private JLabel passwordLabel = new JLabel("Mot de passe", SwingConstants.CENTER);
	private JTextField loginTextArea = new JTextField();
	private JTextField passwordTextArea = new JTextField();
	
	/**
	 * BUTTONS
	 */
	private final Dimension BUTTONS_DIMENSION = new Dimension(GuiConstants.WIDTH / 5, GuiConstants.HEIGHT / 4);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 3, BUTTONS_DIMENSION.height / 3);
	private JPanel buttonsPanel = new JPanel();
	private JButton connectionNormalButton = new JButton("Connexion");
	//private JButton connectionAdminButton = new JButton("Connexion en tant qu'admninistrateur");

	private final Dimension FILLER = new Dimension(GuiConstants.WIDTH, GuiConstants.HEIGHT / 8);
	
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
		passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
		passwordTextArea.setMaximumSize(TEXTFIELD_DIMENSION);
		
		//add them to the fields panel
		fieldsPanel.add(Box.createRigidArea(FILLER));
		fieldsPanel.add(loginLabel);
		fieldsPanel.add(loginTextArea);
		fieldsPanel.add(Box.createRigidArea(FILLER));
		fieldsPanel.add(passwordLabel);
		fieldsPanel.add(passwordTextArea);
	}

	private void initButtons() {
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);

		//alignement
		connectionNormalButton.setAlignmentX(CENTER_ALIGNMENT);
		connectionNormalButton.setPreferredSize(BUTTON_SIZE);
		
		//add button
		buttonsPanel.add(Box.createRigidArea(FILLER));
		buttonsPanel.add(connectionNormalButton);
		
	}
}