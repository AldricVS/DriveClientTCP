package gui.subwindows.product_list;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.Product;
import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.ModifyProductQuantityOption;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolFactory;

/**
 * Panel shwoing a single product
 * 
 * @author Maxence Hennekein
 */
public class ProductPanel extends JPanel {
	private ProductListPanel context;

	private Product product;

	private Dimension productDimension;
	private Dimension buttonDimension;
	private Dimension fieldDimension;
	private JTextField productNameField = new JTextField();
	private JTextField productPriceField = new JTextField();
	private JTextField productQuantityField = new JTextField();
	private JButton changeQuantityButton = new JButton("Modifier Quantit�");
	private JButton addPromotionButton = new JButton("Ajouter une promotion");
	private JButton deleteProductButton = new JButton("Supprimer le produit");

	/**
	 * 
	 * @param product          the product shown in this row
	 * @param productDimension Dimension of the Panel
	 */
	public ProductPanel(ProductListPanel context, Product product, Dimension productDimension) {
		this.context = context;
		// we have to keep trace of the product for action listeners
		this.product = product;
		this.productDimension = productDimension;
		fieldDimension = new Dimension(productDimension.width / 5, productDimension.height / 6);
		buttonDimension = new Dimension(2 * productDimension.width / 15, 2 * productDimension.height / 3);

		setText(product);
		init();
		initField();
		initButton();
	}

	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(productDimension);
	}

	private void setText(Product product) {
		productNameField.setText(product.getName());
		productQuantityField.setText(((Integer) product.getQuantity()).toString());

		// we will check if we have a promotion, and add it here if it is the case
		String priceText = product.getPrice().toString() + "�";
		if (product.hasPromotion()) {
			String promotionText = product.getPromotion().toString() + "�";
			productPriceField.setText(promotionText + " (prix normal : " + priceText + ")");
		} else {
			productPriceField.setText(priceText);
		}

	}

	private void initField() {
		productNameField.setPreferredSize(fieldDimension);
		productNameField.setEditable(false);
		productNameField.setFont(productNameField.getFont().deriveFont(20f));
		add(productNameField);

		productPriceField.setPreferredSize(fieldDimension);
		productPriceField.setEditable(false);
		// a promotion will be bold instead
		if (product.hasPromotion()) {
			productPriceField.setFont(productNameField.getFont().deriveFont(Font.BOLD, 20f));
		} else {
			productPriceField.setFont(productNameField.getFont().deriveFont(20f));
		}

		add(productPriceField);

		productQuantityField.setPreferredSize(fieldDimension);
		productQuantityField.setEditable(false);
		productQuantityField.setFont(productNameField.getFont().deriveFont(20f));
		add(productQuantityField);
	}

	private void initButton() {
		changeQuantityButton.setPreferredSize(buttonDimension);
		changeQuantityButton.addActionListener(new ModifyProductQuantityListener());
		add(changeQuantityButton);

		addPromotionButton.setPreferredSize(buttonDimension);
		add(addPromotionButton);

		deleteProductButton.setPreferredSize(buttonDimension);
		add(deleteProductButton);
	}

	class ModifyProductQuantityListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ModifyProductQuantityOption dialog = new ModifyProductQuantityOption(context, product);
			if (dialog.showPopUp()) {
				Protocol protocolToSend;
				// check which action user choose
				if (dialog.getUserActionChoosen() == ModifyProductQuantityOption.ADD_QUANTITY_OPTION) {
					protocolToSend = ProtocolFactory.createAddProductQuantityProtocol(product.getIdProduct(),
							dialog.getAddQuantity());
				} else {
					protocolToSend = ProtocolFactory.createRemoveProductQuantityProtocol(product.getIdProduct(),
							dialog.getRemoveQuantity());
				}

				// send this protocol to the server
				try {
					Protocol protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocolToSend);
					if(protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						DialogHandler.showInformationDialog(context, "Produit modifi�", "La quantit� du produit a �t� modifi�e");
						//refresh the page to see the modification
						context.refreshPanel();
					}else {
						DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
					}
				} catch (IOException | InvalidProtocolException ex) {
					ex.printStackTrace();
					ProductListPanel.logger.error("Modify product quantity string isnot readable : " + ex.getMessage());
					DialogHandler.showErrorDialog(context, "Erreur", "Erreur lors de la r�ception du message du serveur");
				}
			}
		}

	}
}
