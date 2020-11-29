package gui.subwindows.popup_window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import data.Order;
import data.Product;

/**
 * 
 * @author Maxence
 */
public class showDetailsOrderPanel extends JOptionPane {
	private Component context;
	
	//Panel for Order Information
	private JPanel orderInfoPanel = new JPanel();
	//TextLabel
	private JLabel textID = new JLabel("ID de la Commande");
	private JLabel textName = new JLabel("Nom du Client");
	private JLabel textQuantity = new JLabel("Nombre de Produit");
	private JLabel textTotalPrice = new JLabel("Prix total");
	
	//TextArea
	private JLabel fieldID;
	private JLabel fieldName;
	private JLabel fieldQuantity;
	private JLabel fieldTotalPrice;
	
	//productList / quantity
	private JPanel listProductPanel = new JPanel();
    private JScrollPane listScrollPanel = new JScrollPane();
	private JLabel textProductName = new JLabel("Nom du Produit");
	private JLabel textProductQuantity = new JLabel("Quantité du produit");
	
	private static String[] options = {"Fermer"};
	
	private Order order;
	private ArrayList<Product> productList;

	public showDetailsOrderPanel(Component context, Order order, ArrayList<Product> productList) {
		this.context = context;
		this.order = order;
		this.productList = productList;
		initField();
		initOrderInfo();
		initProductList();
		initLayout();
	}
	
	private void initLayout() {
		this.setLayout(new BorderLayout());
		this.add(orderInfoPanel, BorderLayout.NORTH);
		this.add(listScrollPanel, BorderLayout.SOUTH);
	}
	
	private void initOrderInfo() {
		orderInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);

		//Ajout des TextFields
		c.gridx = 0;
		c.gridy = 0;
		orderInfoPanel.add(textID, c);
		
		c.gridx = 0;
		c.gridy = 1;
		orderInfoPanel.add(textName, c);
		
		c.gridx = 0;
		c.gridy = 2;
		orderInfoPanel.add(textQuantity, c);
		
		c.gridx = 0;
		c.gridy = 3;
		orderInfoPanel.add(textTotalPrice, c);
		
		
		//Ajout des données de la commande
		c.gridx = 1;
		c.gridy = 0;
		orderInfoPanel.add(fieldID, c);
		
		c.gridx = 1;
		c.gridy = 1;
		orderInfoPanel.add(fieldName, c);
		
		c.gridx = 1;
		c.gridy = 2;
		orderInfoPanel.add(fieldQuantity, c);
		
		c.gridx = 1;
		c.gridy = 3;
		orderInfoPanel.add(fieldTotalPrice, c);
		
		//Ajout des informations pour le produit
		c.gridx = 0;
		c.gridy = 4;
		orderInfoPanel.add(textProductName, c);
		
		c.gridx = 1;
		c.gridy = 4;
		orderInfoPanel.add(textProductQuantity, c);
	}
	
	private void initField() {
		fieldID = new JLabel(String.valueOf(order.getIdOrder()));
		fieldName = new JLabel(order.getUserName());
		fieldQuantity = new JLabel(String.valueOf(order.getNumberProduct()));
		fieldTotalPrice = new JLabel(order.getTotalPrice().toString());
	}
	
	private void initProductList() {
		listProductPanel.removeAll();
		listProductPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(2, 5, 2, 5);
		//c.gridwidth = 2;
		
		int y = 0;
		for (Iterator<Product> i = productList.iterator(); i.hasNext(); y++) {
			Product p = i.next();
			c.gridx = 0;
			c.gridy = y;
			listProductPanel.add(new JLabel(p.getName()), c);
			c.gridx = 3;
			c.gridy = y;
			listProductPanel.add(new JLabel(String.valueOf(p.getQuantity())), c);
		}

		listScrollPanel.setViewportView(listProductPanel);
		listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
		
	public boolean showPopUp() {
		int answer = showOptionDialog(context,
				this,
				"Détails de la commande "+order.getIdOrder(),
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]);
	
		return answer == JOptionPane.OK_OPTION;
	}
}
