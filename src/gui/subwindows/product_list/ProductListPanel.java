
package gui.subwindows.product_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import data.Product;
import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.addProductPanel;
import logger.LoggerUtility;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolFactory;
import process.protocol.ProtocolListExtractor;

/**
 * Main panel that shows all products recievec from server
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence Hennekein
 */
public class ProductListPanel extends JPanel {
	public static Logger logger = LoggerUtility.getLogger(ProductListPanel.class, LoggerUtility.LOG_PREFERENCE);
	private MainWindow context;
	private ArrayList<Product> listProduct;

	/**
	 * TITLE
	 */
	private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, 3 * GuiConstants.HEIGHT / 20);
	private JPanel titlePanel = new JPanel();
	private JLabel titlelabel = new JLabel("Drivepicerie - Liste des produits", SwingConstants.CENTER);

	/**
	 * SlideBar
	 */
	private final Dimension LIST_DIMENSION = new Dimension(4 * GuiConstants.WIDTH / 5, 10 * GuiConstants.HEIGHT / 20);
	private final Dimension PRODUCT_LIST_DIMENSION = new Dimension(9 * LIST_DIMENSION.width / 10,
			LIST_DIMENSION.height / 5);
	private JScrollPane listScrollPanel = new JScrollPane();
	private JPanel productListPanel;

	/**
	 * Button
	 */
	private final Dimension BUTTONS_DIMENSION = new Dimension(3 * GuiConstants.WIDTH / 4, 5 * GuiConstants.HEIGHT / 20);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 3, BUTTONS_DIMENSION.height / 5);
	private final Dimension BUTTON_FILLER = new Dimension(2 * GuiConstants.WIDTH / 9, BUTTONS_DIMENSION.height);
	private JPanel buttonsPanel = new JPanel();
	private JButton addProductButton = new JButton("Ajouter un nouveau produit");
	private JButton returnButton = new JButton("Retour");

	public ProductListPanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());

		initTitle();
		initButtons();

		add(titlePanel, BorderLayout.NORTH);
		add(listScrollPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}

	private void initTitle() {
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		titlelabel.setFont(new Font("Serif", Font.BOLD, TITLE_DIMENSION.height / 2));
		titlePanel.add(titlelabel);
	}

	public void initPanel(Protocol protocol) {
		try {
			extractFromProtocol(protocol);

			// On transforme notre liste de produit en affichage
			productListPanel = new JPanel();
			productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.PAGE_AXIS));
			productListPanel.setMinimumSize(LIST_DIMENSION);
			initProductPanel(listProduct);

			listScrollPanel.setViewportView(productListPanel);
			listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			// if we are here, no errors occured, we can switch window
			context.changeWindow(WindowName.PRODUCT_LIST);

		} catch (InvalidProtocolException e) {
			// show error to user and go back to menu
			DialogHandler.showErrorDialog(context, "Erreur", e.getMessage());
			context.changeWindow(WindowName.MENU);
		}
	}

	private void extractFromProtocol(Protocol protocol) throws InvalidProtocolException {
		ProtocolListExtractor extractor = new ProtocolListExtractor(protocol);
		listProduct = extractor.extractProductList();
	}

	/**
	 * Transform the Product's liste receive to a Panel
	 * 
	 * @param productList The Liste of All product
	 * @param size        the amount of Product
	 */
	private void initProductPanel(List<Product> productList) {
		// clear product panel in order to be sure that we start on a fresh start
		productListPanel.removeAll();
		for (Iterator<Product> i = productList.iterator(); i.hasNext();) {
			Product p = i.next();
			productListPanel.add(new ProductPanel(this, p, PRODUCT_LIST_DIMENSION));
		}
	}

	private void initButtons() {
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);

		addProductButton.setAlignmentX(CENTER_ALIGNMENT);
		addProductButton.setMaximumSize(BUTTON_SIZE);

		returnButton.setAlignmentX(CENTER_ALIGNMENT);
		returnButton.setMaximumSize(BUTTON_SIZE);

		addProductButton.addActionListener(new ActionAddProduct());
		returnButton.addActionListener(new ActionRetour());

		// add button
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(addProductButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(returnButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
	}

	class ActionAddProduct implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// pop-up
			addProductPanel addProductPopup = new addProductPanel();
			if (addProductPopup.getPopup()) {
				String productName = addProductPopup.getNameProduct();
				String productPrice = addProductPopup.getPriceProduct();
				String productQuantity = addProductPopup.getQuantityProduct();

				// start protocol to add new product
				Protocol protcol = ProtocolFactory.createAddProductProtocol(productName, productPrice, productQuantity);
				try {
					Protocol protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protcol);
					// If server send success message, add the new product in the list
					if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						// refresh the page : ask a new selection to server
						refreshPanel();
					} else {
						DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
					}
				} catch (IOException | InvalidProtocolException e1) {
					e1.printStackTrace();
					DialogHandler.showErrorDialog(context, "Mauvaise réponse",
							"La réponse du serveur n'a pas pu être déchifrée.");
				}
			}
		}
	}

	class ActionRetour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.changeWindow(WindowName.MENU);
		}
	}

	/**
	 * Refresh the panel asking to the server the product list
	 */
	public void refreshPanel() {
		boolean isRefreshValid = false;
		try {
			Protocol protocolRecieved = ServerConnectionHandler.getInstance()
					.sendProtocolMessage(ProtocolFactory.createGetListProductProtocol());
			
			if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
				extractFromProtocol(protocolRecieved);
				initProductPanel(listProduct);
				listScrollPanel.setViewportView(productListPanel);
				repaint();
				productListPanel.repaint();
				isRefreshValid = true;
			}
		} catch (IOException | InvalidProtocolException e) {
			// we can't do anymore here, go back to menu
			DialogHandler.showErrorDialog(context, "Rafraichissement impossible",
					"Impossible de récupérer la liste des produits, retour au menu.");
			e.printStackTrace();
			logger.error("Can't retrieve information from string : " + e.getMessage());
		}

		// if something bad happens, go to menu
		if (!isRefreshValid) {
			context.changeWindow(WindowName.MENU);
		}
	}

}