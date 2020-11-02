
package gui.subwindows.product_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import data.Product;
import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.subwindows.popup_window.addProductPanel;

/**
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence
 */
public class ProductListPanel extends JPanel {

private MainWindow context;
    
    /**
     * TITLE 
     */
    private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, 3 * GuiConstants.HEIGHT / 18);
    private JPanel titlePanel = new JPanel();
    private JLabel titlelabel = new JLabel("Drivepicerie - Accueil", SwingConstants.CENTER);
    
    /**
     * SlideBar
     */
    private final Dimension LIST_DIMENSION = new Dimension(4 * GuiConstants.WIDTH / 5, 10 * GuiConstants.HEIGHT / 18);
    private final Dimension PRODUCT_LIST_DIMENSION = new Dimension(7 * LIST_DIMENSION.width / 8, LIST_DIMENSION.height / 6);
    private JScrollPane listScrollPanel = new JScrollPane();
    private JPanel productListPanel = new JPanel();
    
    //test seulement, créer quelques produits pour essayer l'affichage
    private Product product1 = new Product("Caillou", 5, new BigDecimal(5));
    private Product product2 = new Product("Rocher", 2, new BigDecimal(19.5));
    private Product product3 = new Product("Pierre", 50, new BigDecimal(0.25));
    private Product product4 = new Product("Arbre", 50, new BigDecimal(0.25));
    private Product product5 = new Product("Branche", 50, new BigDecimal(0.25));
    private Product product6 = new Product("Feuille", 50, new BigDecimal(0.25));
    private Product product7 = new Product("Eau", 50, new BigDecimal(0.25));
    //list de product
    private Product[] productListTest;
    private int size = 7;
    
    /**
     * Button
     */
    private final Dimension BUTTONS_DIMENSION = new Dimension(3 * GuiConstants.WIDTH / 4, 5 * GuiConstants.HEIGHT / 18);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 4, BUTTONS_DIMENSION.height / 8);
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
		//init list doit être fait après récupération de la liste de produit par le serveur
		initList();
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
	
	/**
	 * Liste de produit, uniquement pour des tests (TODO à supprimer)
	 */
	private void initTest() {
		productListTest = new Product[size];
		productListTest[0] = product1;
		productListTest[1] = product2;
		productListTest[2] = product3;
		productListTest[3] = product4;
		productListTest[4] = product5;
		productListTest[5] = product6;
		productListTest[6] = product7;
			//je rempli manuelement, mais il faut le faire autrement
			// -> changer JList pour contenir les productPanel, qui vont gérer l'affichage
	}
	
	private void initList() {
		/*
		 * Normalement dès lors qu'on accède à la page,
		 * mais pour le moment on utilise une liste préfaite
		 */
		initTest();
			//On transforme notre liste de produit en affichage
		productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.PAGE_AXIS));
		productListPanel.setPreferredSize(LIST_DIMENSION);
		initProductPanel(productListTest, size);
		
		listScrollPanel.setViewportView(productListPanel);
		listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	/**
	 * Transform the Product's liste receive to a Panel
	 * @param productList The Liste of All product
	 * @param size the amount of Product
	 */
	private void initProductPanel(Product[] productList, int size) {
		for (int i = 0; i < size; i++) {
			Product p = productList[i];
			productListPanel.add(new ProductPanel(p, PRODUCT_LIST_DIMENSION));
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
		
		//add button
		buttonsPanel.add(addProductButton);
		buttonsPanel.add(returnButton);
	}
	
	class ActionAddProduct implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//pop-up
			addProductPanel addProductPopup = new addProductPanel();
			addProductPopup.getPopup();
			String productName = addProductPopup.getNameProduct();
			String productPrice = addProductPopup.getPriceProduct();
			String productQuantity = addProductPopup.getQuantityProduct();
			//start protocol to add new product
				//when it ends, change to product list
				context.changeWindow(WindowName.PRODUCT_LIST.name());
			//redraw ?
		}
	}
	
    class ActionRetour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.changeWindow(WindowName.MENU.name());
		}
	}
}