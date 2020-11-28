package gui.subwindows.order_list;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Order;
import data.Product;
import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import exceptions.ServerConnectionLostException;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.showDetailsOrderPanel;
import gui.subwindows.product_list.ProductListPanel;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolFactory;
import process.protocol.ProtocolListExtractor;

/**
 * 
 * @author Maxence Hennekein
 */
public class OrderPanel extends JPanel {
	private OrderListPanel context;
	private Order order;
	
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
	 * @param context the {@link gui.subwindows.order_list.OrderListPanel OrderListPanel} where this {@link gui.subwindows.order_list.OrderPanel OrderPanel} will be
	 * @param order the Order shown in this row
	 * @param orderDimension Dimension of the Panel
	 */
	public OrderPanel(OrderListPanel context, Order order, Dimension orderDimension) {
		this.context = context;
		this.order = order;
		this.orderDimension = orderDimension;
		fieldDimension = new Dimension(7 * orderDimension.width / 50, orderDimension.height / 6);
		buttonDimension = new Dimension(5 * orderDimension.width / 50, 2 * orderDimension.height / 3);
		
		setText(order);
		init();
		initField();
		initButton();
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
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
		orderIdField.setFont(orderIdField.getFont().deriveFont(20f));
		add(orderIdField);
		
		orderUserNameField.setPreferredSize(fieldDimension);
		orderUserNameField.setEditable(false);
		add(orderUserNameField);
		
		orderTotalPriceField.setPreferredSize(fieldDimension);
		orderTotalPriceField.setEditable(false);
		add(orderTotalPriceField);
		
		orderDateMadeField.setPreferredSize(fieldDimension);
		orderDateMadeField.setEditable(false);
		add(orderDateMadeField);
		
		orderDateReceiveField.setPreferredSize(fieldDimension);
		orderDateReceiveField.setEditable(false);
		add(orderDateReceiveField);
	}
	
	private void initButton() {
		showDetailsButton.setPreferredSize(buttonDimension);
		showDetailsButton.addActionListener(new ActionShowDetails());
		add(showDetailsButton);
		
		acceptOrderButton.setPreferredSize(buttonDimension);
		acceptOrderButton.addActionListener(new ActionAcceptOrder());
		add(acceptOrderButton);
		
		cancelOrderButton.setPreferredSize(buttonDimension);
		cancelOrderButton.addActionListener(new ActionCancelOrder());
		add(cancelOrderButton);
	}
	
	class ActionShowDetails implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int orderId = order.getIdOrder();
			Protocol protocolToSend = ProtocolFactory.createGetSpecificOrderProtocol(orderId);
			Protocol protocolRecieved = null;
			try {
				protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocolToSend);
				if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
					ProtocolListExtractor extractor = new ProtocolListExtractor(protocolRecieved);
					ArrayList<Product> productList = extractor.extractOrderProductList();
					BigDecimal priceTotalRecieved = new BigDecimal(protocolRecieved.getOptionsElement(1));
					if (order.getTotalPrice().compareTo(priceTotalRecieved) == 0) {
						showDetailsOrderPanel detailsOrder = new showDetailsOrderPanel(context, order, productList);
						detailsOrder.showPopUp();
					}
				}
				else {
					throw new InvalidProtocolException("Erreur dans la réponse du serveur");
				}
			} catch (NumberFormatException ex) {
				OrderListPanel.logger.error("Error while loading totalPrice: "+protocolRecieved.getOptionsElement(2)+" ; The Number is not valid.");
			} catch (IOException | InvalidProtocolException ex) {
				OrderListPanel.logger.error("Couldn't load order with Id: "+orderId);
				DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
			} catch (ServerConnectionLostException ex) {
				ProductListPanel.logger.error(ex.getMessage());
				DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
				context.disconnect();
			}
		}
	}
	
	class ActionAcceptOrder implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int orderId = order.getIdOrder();
			boolean acceptOrder = DialogHandler.showConfirmDialog(context, "Confirmation de la commande", "Confirmez-vous la commande "+orderId+" ?");
			if (acceptOrder) {
				Protocol protocolToSend = ProtocolFactory.createAcceptOrderProtocol(orderId);
				Protocol protocolRecieved = null;
				try {
					protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocolToSend);
					if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						DialogHandler.showInformationDialog(context, "Commande confirmé", "La Commande a bien été accepté");
						//reload page
						context.refreshPanel();
					}
					else {
						throw new InvalidProtocolException();
					}
				} catch (IOException | InvalidProtocolException ex) {
					OrderListPanel.logger.error("Couldn't accept order with Id: "+orderId);
					DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
				} catch (ServerConnectionLostException ex) {
					ProductListPanel.logger.error(ex.getMessage());
					DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
					context.disconnect();
				}
			}
		}
	}
	
	class ActionCancelOrder implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int orderId = order.getIdOrder();
			boolean acceptOrder = DialogHandler.showConfirmDialog(context, "Annulation de la commande", "Annuler la commande "+orderId+" ?");
			if (acceptOrder) {
				Protocol protocolToSend = ProtocolFactory.createCancelOrderProtocol(orderId);
				Protocol protocolRecieved = null;
				try {
					protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocolToSend);
					if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						DialogHandler.showInformationDialog(context, "Commande annulé", "La Commande a bien été annulé");
						//reload page
						context.refreshPanel();
					}
					else {
						throw new InvalidProtocolException();
					}
				} catch (IOException | InvalidProtocolException ex) {
					OrderListPanel.logger.error("Couldn't accept order with Id: "+orderId);
					DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
				} catch (ServerConnectionLostException ex) {
					ProductListPanel.logger.error(ex.getMessage());
					DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
					context.disconnect();
				}
			}
		}
	}
	
}
