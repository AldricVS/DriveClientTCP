
package gui.subwindows.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.subwindows.popup_window.addEmployeePanel;
import gui.subwindows.popup_window.addProductPanel;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolExtractor;
import process.protocol.ProtocolFactory;

/**
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence Hennekein
 */
public class MenuPanel extends JPanel {

	private MainWindow context;
	private boolean asAdmin;
	
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
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 4, BUTTONS_DIMENSION.height / 8);
	private JPanel buttonsPanel = new JPanel();
	private JButton productListButton = new JButton("Afficher la liste des produits");
	private JButton addProductButton = new JButton("Ajouter un nouveau produit");
	private JButton orderListButton = new JButton("Voir la liste des commandes");
	private JButton disconnectButton = new JButton("Déconnexion");
	
	/**
	 * ADMIN
	 */
	private JButton employeeListButton = new JButton("Voir la liste des Employes");
	private JButton addEmployeeButton = new JButton("Ajouter un nouvel Employe");
	
	
	public MenuPanel(MainWindow context) {
		this.context = context;
		init();
	}
	
	private void init() {
		//Les boutons seront placés toujours de la même manière
		buttonsPanel.setLayout(new GridBagLayout());
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);
		
		initTitle();
		initActionProduit();
		
		add(titlePanel);
		add(buttonsPanel);
	}

	private void initTitle() {
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		titlelabel.setFont(new Font("Serif", Font.BOLD, TITLE_DIMENSION.height / 2));
		titlePanel.add(titlelabel);
	}
	
	private void initActionProduit() {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.4;
		c.weighty = 0.2;
		
		//Product List
		c.gridx = 0;
		c.gridy = 0;
		productListButton.setPreferredSize(BUTTON_SIZE);
		productListButton.addActionListener(new ActionGoToProductList());
		buttonsPanel.add(productListButton, c);
		
		//Add Product
		c.gridx = 2;
		c.gridy = 0;
		addProductButton.setPreferredSize(BUTTON_SIZE);
		addProductButton.addActionListener(new ActionAddProduct());
		buttonsPanel.add(addProductButton, c);
		
		//Order List
		c.gridx = 1;
		c.gridy = 1;
		orderListButton.setPreferredSize(BUTTON_SIZE);
		orderListButton.addActionListener(new ActionGoToOrderList());
		buttonsPanel.add(orderListButton, c);
		

		//Disconnect
		c.gridx = 1;
		c.gridy = 3;
		disconnectButton.setPreferredSize(BUTTON_SIZE);
		disconnectButton.addActionListener(new ActionDisconnect());
		buttonsPanel.add(disconnectButton, c);
	}
	
	private void initGestionMagasin() {
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.4;
		c.weighty = 0.2;
		
		//Employee List
		c.gridx = 0;
		c.gridy = 2;
		employeeListButton.setPreferredSize(BUTTON_SIZE);
		buttonsPanel.add(employeeListButton, c);
		
		//Add Employee
		c.gridx = 2;
		c.gridy = 2;
		addEmployeeButton.setPreferredSize(BUTTON_SIZE);
		buttonsPanel.add(addEmployeeButton, c);
		
	}
	
	/**
	 * Ajoute les boutons de Gestion du Magasin
	 */
	public void initAdmin() {
		if (!asAdmin) {
			initGestionMagasin();
			asAdmin = true;
		}
	}
	
	/**
	 * Retire les boutons de Gestion du Magasin
	 */
	public void disconnectAdmin() {
		if (asAdmin) {
			buttonsPanel.remove(employeeListButton);
			buttonsPanel.remove(addEmployeeButton);
			asAdmin = false;
		}
	}
	
	class ActionDisconnect implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.disconnect();
			//revenir au login
			context.changeWindow(WindowName.LOGIN.name());
		}
	}
	
	class ActionGoToProductList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//send protocol to receive productList
			Protocol protocol = ProtocolFactory.createGetListProductProtocol();
			Protocol productList = null;
			try {
				productList = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InvalidProtocolException ex) {
				ex.printStackTrace();
			}
			context.initProductList(productList);
			context.changeWindow(WindowName.PRODUCT_LIST.name());
		}
	}
	
	
	class ActionAddProduct implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//pop-up
			addProductPanel addProductPopup = new addProductPanel();
			boolean result;
			result = addProductPopup.getPopup();
			if (result) {
				String productName = addProductPopup.getNameProduct();
				String productPrice = addProductPopup.getPriceProduct();
				String productQuantity = addProductPopup.getQuantityProduct();
				if (productName.isEmpty() || productPrice.isEmpty() || productQuantity.isEmpty()) {
					JOptionPane.showMessageDialog(context, "Un des champs est vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//start protocol to add new product
				Protocol protocol;
				protocol = ProtocolFactory.createAddProductProtocol(productName, productPrice, productQuantity);
				try {
					ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				} catch (InvalidProtocolException e1) {
					e1.printStackTrace();
					return;
				}
				//when it ends, change to product list
				context.changeWindow(WindowName.PRODUCT_LIST.name());
			}
			//the popup was closed, nothing happen
		}
	}
	
	class ActionGoToOrderList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//receive protocol to receive list of order
			Protocol protocol = ProtocolFactory.createGetListOrderProtocol();
			Protocol orderList = null;
			try {
				orderList = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InvalidProtocolException ex) {
				ex.printStackTrace();
			}
			context.initListOrder(orderList);
			context.changeWindow(WindowName.ORDER_LIST.name());
		}
	}
	class ActionGoToEmployeeList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Protocol protocol = ProtocolFactory.createGetListEmployeeProtocol();
			Protocol employeeList = null;
			try {
				employeeList = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InvalidProtocolException ex) {
				ex.printStackTrace();
			}
			context.initEmployeeList(employeeList);
			context.changeWindow(WindowName.EMPLOYEE_LIST.name());
		}
	}
	
	class ActionAddEmployee implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addEmployeePanel addEmployeePopup = new addEmployeePanel();
			boolean result;
			result = addEmployeePopup.getPopup();
			if (result) {
				String employeeName = addEmployeePopup.getNameEmployee();
				String employeePassword = addEmployeePopup.getPassword();
				String employeeConfirmPassword = addEmployeePopup.getPasswordConfirm();
				if (employeePassword.equals(employeeConfirmPassword)) {
					//start protocol to add new product
					Protocol protocol = ProtocolFactory.createAddEmployeeProtocol(employeeName, employeePassword);
					try {
						ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (InvalidProtocolException ex) {
						ex.printStackTrace();
					}
					//when it ends, change to product list
					context.changeWindow(WindowName.EMPLOYEE_LIST.name());
				}
				else {
					JOptionPane.showMessageDialog(context, "Validation du Mot de Passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
			//has the popup was closed, nothing happen
		}
	}
}
