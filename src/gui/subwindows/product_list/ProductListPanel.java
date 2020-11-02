
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
 */
public class ProductListPanel extends JPanel {

private MainWindow context;
    
    /**
     * TITLE 
     */
    private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, GuiConstants.HEIGHT / 6);
    private JPanel titlePanel = new JPanel();
    private JLabel titlelabel = new JLabel("Drivepicerie - Accueil", SwingConstants.CENTER);
    
    /**
     * SlideBar
     */
    private final Dimension LIST_DIMENSION = new Dimension(4 * GuiConstants.WIDTH / 5, 5 * GuiConstants.HEIGHT / 9);
    private JScrollPane listScrollPanel;
    private JList<Product> productJList;
    
    //test seulement, créer quelques produits pour essayer l'affichage
    private BigDecimal nbr1 = new BigDecimal(5);
    private Product product1 = new Product("Caillou", 5, nbr1);
    private BigDecimal nbr2 = new BigDecimal(19.5);
    private Product product2 = new Product("Rocher", 2, nbr2);
    private BigDecimal nbr3 = new BigDecimal(0.25);
    private Product product3 = new Product("Pierre", 50, nbr3);
    //list de product
    private Product[] productListTest;
    
    /**
     * Button
     */
    private final Dimension BUTTONS_DIMENSION = new Dimension(3 * GuiConstants.WIDTH / 4, 4 * GuiConstants.HEIGHT / 6);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 4, BUTTONS_DIMENSION.height / 8);
    private JPanel buttonsPanel = new JPanel();
    private JButton returnButton = new JButton("Retour");
    private JButton addProductButton = new JButton("Ajouter un nouveau produit");
    
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
	
	private void initList() {
    	int size = 3;
		productListTest = new Product[size];
		productListTest[0] = product1;
		productListTest[1] = product2;
		productListTest[2] = product3;
			//je rempli manuel, mais il faut le faire autrement
			// -> changer JList pour contenir les productPanel, qui vont gérer l'affichage
		
    	productJList = new JList<Product>(productListTest);
		productJList.setLayoutOrientation(JList.VERTICAL);
		
		listScrollPanel = new JScrollPane(productJList);
		listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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