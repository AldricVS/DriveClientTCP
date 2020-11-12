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
	private JTextField orderTotalPriceField = new JTextField();
	private JTextField orderDateMadeField = new JTextField();
	private JTextField orderDateReceiveField = new JTextField();
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
		fieldDimension = new Dimension(7 * orderDimension.width / 50, orderDimension.height / 6);
		buttonDimension = new Dimension(orderDimension.width / 10, 2 * orderDimension.height / 3);
		
		setText(order);
		init();
		initField();
		initButton();
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		//TODO: refaire les dimensions (correctement) pour avoir un affichage maximum convenable
		setMaximumSize(orderDimension);
	}
	
	private void setText(Order order) {
		orderIdField.setText( ((Integer)order.getIdOrder()).toString());
		orderUserNameField.setText(order.getUserName());
		orderTotalPriceField.setText( ((BigDecimal)order.getTotalPrice()).toString());
		orderDateMadeField.setText(order.getDateCommandMade());
		orderDateReceiveField.setText(order.getDateCommandReceive());
	}

	private void initField() {
		orderIdField.setPreferredSize(fieldDimension);
		orderIdField.setEditable(false);
		add(orderIdField);
		
		orderUserNameField.setPreferredSize(fieldDimension);
		orderUserNameField.setEditable(false);
		add(orderUserNameField);
		
		orderDateMadeField.setPreferredSize(fieldDimension);
		orderDateMadeField.setEditable(false);
		add(orderDateMadeField);
		
		orderDateReceiveField.setPreferredSize(fieldDimension);
		orderDateReceiveField.setEditable(false);
		add(orderDateReceiveField);
	}
	
	private void initButton() {
		showDetailsButton.setPreferredSize(buttonDimension);
		add(showDetailsButton);
		
		acceptOrderButton.setPreferredSize(buttonDimension);
		add(acceptOrderButton);
		
		cancelOrderButton.setPreferredSize(buttonDimension);
		add(cancelOrderButton);
	}
}
