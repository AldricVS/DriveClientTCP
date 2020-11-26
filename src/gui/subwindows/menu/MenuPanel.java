
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
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import exceptions.ServerConnectionLostException;
import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.addEmployeePanel;
import gui.subwindows.popup_window.addProductPanel;
import process.connection.ServerConnectionHandler;
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
		employeeListButton.addActionListener(new ActionGoToEmployeeList());
		buttonsPanel.add(employeeListButton, c);
		
		//Add Employee
		c.gridx = 2;
		c.gridy = 2;
		addEmployeeButton.setPreferredSize(BUTTON_SIZE);
		addEmployeeButton.addActionListener(new ActionAddEmployee());
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
			context.changeWindow(WindowName.LOGIN);
		}
	}
	
	class ActionGoToProductList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//send protocol to receive productList
			Protocol protocol = ProtocolFactory.createGetListProductProtocol();
			Protocol recievedProtocol = null;
			try {
				recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
				if (recievedProtocol.getActionCode() == ActionCodes.ERROR) {
					DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
					return;
				}
				if (recievedProtocol.getOptionsListSize() < 2) {
					MainWindow.logger.error("protocol recieved have no products.");
					throw new InvalidProtocolException("Aucun produit n'a été récupéré.");
				}
				
				//if no error occurs, we can move safely to the next page
				context.initProductList(recievedProtocol);
				context.changeWindow(WindowName.PRODUCT_LIST);
				
			} catch (IOException | InvalidProtocolException ex) {
				MainWindow.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
			} catch (ServerConnectionLostException ex) {
				MainWindow.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
				context.disconnect();
				context.changeWindow(WindowName.LOGIN);
			}
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
					DialogHandler.showErrorDialog(context, "Erreur", "Un des champs est vide.");
					return;
				}
				
				Protocol protocol;
				Protocol recievedProtocol = null;
				try {
					//start protocol to add new product
					protocol = ProtocolFactory.createAddProductProtocol(productName, productPrice, productQuantity);
					recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
					if(recievedProtocol.getActionCode() == ActionCodes.ERROR) {
						DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
						return;
					}
					DialogHandler.showInformationDialog(context, "Succès", "Le produit "+productName+" a bien été ajouté");
					
					//when it ends, change to product list
					protocol = ProtocolFactory.createGetListProductProtocol();
					recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
					if(recievedProtocol.getActionCode() == ActionCodes.ERROR) {
						DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
						return;
					}
					if(recievedProtocol.getOptionsListSize() < 2) {
						MainWindow.logger.error("protocol recieved have no products.");
						throw new InvalidProtocolException("Aucun produit n'a pu être récupéré.");
					}
					
					//if no error occurs, we can move safely to the next page
					context.initProductList(recievedProtocol);
					
				} catch (IOException | InvalidProtocolException ex) {
					MainWindow.logger.error(ex.getMessage());
					DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
				} catch (ServerConnectionLostException ex) {
					MainWindow.logger.error(ex.getMessage());
					DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
					context.disconnect();
					context.changeWindow(WindowName.LOGIN);
				}
			}
		}
	}
	
	class ActionGoToOrderList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//receive protocol to receive list of order
			Protocol protocol = ProtocolFactory.createGetListOrderProtocol();
			Protocol recievedProtocol = null;
			try {
				recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
				if (recievedProtocol.getActionCode() == ActionCodes.ERROR) {
					DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
					return;
				}
				//charge order list
				context.initListOrder(recievedProtocol);
				//go to order page
				context.changeWindow(WindowName.ORDER_LIST);
			} catch (IOException | InvalidProtocolException ex) {
				MainWindow.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
			} catch (ServerConnectionLostException ex) {
				MainWindow.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
				context.disconnect();
				context.changeWindow(WindowName.LOGIN);
			}
		}
	}
	
	class ActionGoToEmployeeList implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Protocol protocol = ProtocolFactory.createGetListEmployeeProtocol();
			Protocol recievedProtocol = null;
			try {
				recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
				if(recievedProtocol.getActionCode() == ActionCodes.ERROR) {
					DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
					return;
				}
				if(recievedProtocol.getOptionsListSize() < 2) {
					MainWindow.logger.error("protocol recieved have no employe");
					throw new InvalidProtocolException("Aucun Employe n'a pu être trouvé.");
				}
				context.initEmployeeList(recievedProtocol);
				context.changeWindow(WindowName.EMPLOYEE_LIST);
			} catch (IOException | InvalidProtocolException ex) {
				MainWindow.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
			} catch (ServerConnectionLostException ex) {
				MainWindow.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
				context.disconnect();
				context.changeWindow(WindowName.LOGIN);
			}
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
					try {
						Protocol protocol = ProtocolFactory.createAddEmployeeProtocol(employeeName, employeePassword);
						Protocol recievedProtocol;
						
						recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
						if(recievedProtocol.getActionCode() == ActionCodes.ERROR) {
							DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
							return;
						}
						protocol = ProtocolFactory.createGetListEmployeeProtocol();
						recievedProtocol = null;
						
						recievedProtocol = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
						if(recievedProtocol.getActionCode() == ActionCodes.ERROR) {
							DialogHandler.showErrorDialogFromProtocol(context, recievedProtocol);
							return;
						}
						if(recievedProtocol.getOptionsListSize() < 2) {
							MainWindow.logger.error("protocol recieved have no employe");
							throw new InvalidProtocolException("Aucun Employe n'a pu être trouvé.");
						}
						
						context.initEmployeeList(recievedProtocol);
						
						//when it ends, change to employee list
						context.changeWindow(WindowName.EMPLOYEE_LIST);
					} catch (IOException | InvalidProtocolException ex) {
						ex.printStackTrace();
						DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
					} catch (ServerConnectionLostException ex) {
						MainWindow.logger.error(ex.getMessage());
						DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
						context.disconnect();
						context.changeWindow(WindowName.LOGIN);
					}
				}
				else {
					DialogHandler.showErrorDialog(context, "Erreur", "Validation du Mot de Passe incorrect");
				}
			}
			//has the popup was closed, nothing happen
		}
	}
}
