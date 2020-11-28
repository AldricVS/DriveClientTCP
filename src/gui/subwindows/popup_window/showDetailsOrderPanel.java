package gui.subwindows.popup_window;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

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
		this.add(orderInfoPanel);
		
	}
	
	private void initOrderInfo() {
		orderInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.weightx = 2;
		//c.weighty = 3;
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
		
		
		//Ajout des textArea
		c.gridwidth = 2;
		
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
	}
	
	private void initField() {
		fieldID = new JLabel(String.valueOf(order.getIdOrder()));
		fieldName = new JLabel(order.getUserName());
		fieldQuantity = new JLabel(String.valueOf(order.getNumberProduct()));
		fieldTotalPrice = new JLabel(order.getTotalPrice().toString());
	}
	
	private void initProductList() {
		
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
