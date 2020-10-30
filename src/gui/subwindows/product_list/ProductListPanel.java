
package gui.subwindows.product_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JScrollPane listPanel = new JScrollPane();
    private JList<Product> productList;
    
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
		//initList();
		initButtons();
		
		add(titlePanel, BorderLayout.NORTH);
		//add(listPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}
    
	private void initTitle() {
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		titlelabel.setFont(new Font("Serif", Font.BOLD, TITLE_DIMENSION.height / 2));
		titlePanel.add(titlelabel);
	}
	
	private void initList() {
		//j'attends les methodes pour recup la la liste depuis le serveur
		
		//productList.setLayoutOrientation(JList.VERTICAL);
		//productList.setVisibleRowCount(-1);
		//listPanel.add(productList);
	}
	
	
	private void initButtons() {
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);
		
		addProductButton.setAlignmentX(CENTER_ALIGNMENT);
		addProductButton.setMaximumSize(BUTTON_SIZE);
		
		returnButton.setAlignmentX(CENTER_ALIGNMENT);
		returnButton.setMaximumSize(BUTTON_SIZE);
		
		//addProductButton.addActionListener(new ActionAddProduct());
		returnButton.addActionListener(new ActionRetour());
		
		//add button
		buttonsPanel.add(addProductButton);
		buttonsPanel.add(returnButton);
	}
	
    class ActionRetour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.changeWindow(WindowName.MENU.name());
		}
	}
}