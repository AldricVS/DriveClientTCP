/**
 * 
 */
package gui.subwindows.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.GuiConstants;
import gui.MainWindow;

/**
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class MenuPanel extends JPanel {

	private MainWindow context;
	
	/**
	 * TITLE 
	 */
	private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, GuiConstants.HEIGHT / 6);
	private JPanel titlePanel = new JPanel();
	private JLabel titlelabel = new JLabel("Drivepicerie - Accueil", SwingConstants.CENTER);
	
	/**
	 * Employe
	 */
	private final Dimension BUTTONS_DIMENSION = new Dimension(3 * GuiConstants.WIDTH / 4, 4 * GuiConstants.HEIGHT / 6);
	private JPanel buttonsPanel = new JPanel();
	private JButton productListButton = new JButton("Afficher la liste des produits");
	private JButton addProductButton = new JButton("Ajouter un nouveau produit");
	private JButton orderListButton = new JButton("Voir la liste des commandes");
	private JButton disconnectButton = new JButton("Déconnexion");
	
	/**
	 * ADMIN
	 * je les note ici, mais je pense qu'on devrait les créer uniquement si on est admin reconnu
	 */
	private JButton employeeListButton = new JButton("Ajouter un nouveau produit");
	private JButton addEmployeeButton = new JButton("Voir la liste des commandes");
	
	
	public MenuPanel(MainWindow context) {
		this.context = context;
		//Si admin, une autre méthode de création ?
		initAsEmployee();
		//initAsAdmin();
	}
	
	private void initAsEmployee() {
		initTitle();
		initActionProduit();
		initDeconnexion();
	}
	
	private void initAsAdmin() {
		initTitle();
		initActionProduit();
		initGestionMagasin();
		initDeconnexion();
	}

	private void initTitle() {
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		titlelabel.setFont(new Font("Serif", Font.BOLD, TITLE_DIMENSION.height / 2));
		titlePanel.add(titlelabel);
	}
	
	private void initActionProduit() {
		buttonsPanel.setLayout(new GridBagLayout());
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);
		
		GridBagConstraints c = new GridBagConstraints();
		
	}
	
	private void initGestionMagasin() {
		
	}
	
	private void initDeconnexion() {
		
	}
	
}
