
package gui.subwindows.order_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import data.Order;
import data.Product;
import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.components.DialogHandler;
import logger.LoggerUtility;
import process.protocol.ProtocolListExtractor;

/**
 * Main Panel showing all Order recieved from the server
 * @author Maxence Hennekein
 */
public class OrderListPanel extends JPanel {
	public static Logger logger = LoggerUtility.getLogger(OrderListPanel.class, LoggerUtility.LOG_PREFERENCE);
	private MainWindow context;
	private ArrayList<Order> listOrder;
	
    /**
     * TITLE 
     */
    private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, 3 * GuiConstants.HEIGHT / 20);
    private JPanel titlePanel = new JPanel();
    private JLabel titlelabel = new JLabel("Drivepicerie - Liste des commandes", SwingConstants.CENTER);
    
    /**
     * SlideBar
     */
    private final Dimension LIST_DIMENSION = new Dimension(4 * GuiConstants.WIDTH / 5, 10 * GuiConstants.HEIGHT / 20);
    private final Dimension ORDER_LIST_DIMENSION = new Dimension(9 * LIST_DIMENSION.width / 10, LIST_DIMENSION.height / 5);
    private JScrollPane listScrollPanel = new JScrollPane();
    private JPanel orderListPanel;
    
    /**
     * Button
     */
    private final Dimension BUTTONS_DIMENSION = new Dimension(3 * GuiConstants.WIDTH / 4, 5 * GuiConstants.HEIGHT / 20);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 3, BUTTONS_DIMENSION.height / 5);
	private final Dimension BUTTON_FILLER = new Dimension(2 * GuiConstants.WIDTH / 5, BUTTONS_DIMENSION.height);
    private JPanel buttonsPanel = new JPanel();
    private JButton returnButton = new JButton("Retour");
    
    public OrderListPanel(MainWindow context) {
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
			
			//On transforme notre liste de produit en affichage
			orderListPanel = new JPanel();
			orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.PAGE_AXIS));
			orderListPanel.setMinimumSize(LIST_DIMENSION);
			initProductPanel(listOrder);
			
			listScrollPanel.setViewportView(orderListPanel);
			listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		} catch (InvalidProtocolException e) {
			logger.error("Erreur dans l'extraction du Protocol: "+e.getMessage());
			DialogHandler.showErrorDialog(context, "Erreur", e.getMessage());
			//return to Menu
			context.changeWindow(WindowName.MENU);
		}
	}
	
	private void extractFromProtocol(Protocol protocol) throws InvalidProtocolException {
		ProtocolListExtractor extractor = new ProtocolListExtractor(protocol);
		listOrder = extractor.extractOrderList();
	}
	
	/**
	 * change the productList received into OrderPanel
	 * @param productList
	 */
	private void initProductPanel(List<Order> productList) {
		for (Iterator<Order> i = productList.iterator(); i.hasNext(); ) {
			Order order = i.next();
			orderListPanel.add(new OrderPanel(this, order, ORDER_LIST_DIMENSION));
		}
	}

	private void initButtons() {
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);
		
		returnButton.setAlignmentX(CENTER_ALIGNMENT);
		returnButton.setMaximumSize(BUTTON_SIZE);
		returnButton.addActionListener(new ActionRetour());
		
		//add button
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(returnButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
	}
	
    class ActionRetour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.changeWindow(WindowName.MENU);
		}
	}
}