package gui.subwindows.order_list;

import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Order;

/**
 * 
 * @author Maxence Hennekein
 */
public class OrderPanel extends JPanel {
	private Dimension orderDimension;
	private Dimension buttonDimension;
	private Dimension fieldDimension;
	private JTextField orderIdField = new JTextField();
	private JTextField orderUserNameField = new JTextField();
	private JTextField orderDateField = new JTextField();
	private JButton showDetailsButton = new JButton("Afficher les details");
	private JButton acceptOrderButton = new JButton("Accepter la commande");
	private JButton cancelOrderButton = new JButton("Supprimer la commande");
	
	/**
	 * 
	 * @param order the Order shown in this row
	 * @param orderDimension Dimension of the Panel
	 */
	public OrderPanel(Order order, Dimension orderDimension) {
		this.orderDimension = orderDimension;
		fieldDimension = new Dimension(orderDimension.width / 5, orderDimension.height / 6);
		buttonDimension = new Dimension(2 * orderDimension.width / 15, 2 * orderDimension.height / 3);
		
		setText(order);
		init();
		initField();
		initButton();
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(orderDimension);
	}
	
	private void setText(Order order) {
		
	}

	private void initField() {
		orderIdField.setPreferredSize(fieldDimension);
		orderIdField.setEditable(false);
		add(orderIdField);
		
		orderUserNameField.setPreferredSize(fieldDimension);
		orderUserNameField.setEditable(false);
		add(orderUserNameField);
		
		orderDateField.setPreferredSize(fieldDimension);
		orderDateField.setEditable(false);
		add(orderDateField);
	}
	
	private void initButton() {
		showDetailsButton.setPreferredSize(buttonDimension);
		//addQuantityButton.addActionListener();
		add(showDetailsButton);
		
		acceptOrderButton.setPreferredSize(buttonDimension);
		add(acceptOrderButton);
		
		cancelOrderButton.setPreferredSize(buttonDimension);
		add(cancelOrderButton);
	}
}
