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

import data.Product;
import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.ModifyProductQuantityOption;
import gui.subwindows.popup_window.ModifyPromotionOption;
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
	private JButton changeQuantityButton = new JButton("Modifier Quantité");
	private JButton addPromotionButton = new JButton("Modifier la promotion");
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
		String priceText = product.getPrice().toString() + "€";
		if (product.hasPromotion()) {
			String promotionText = product.getPromotion().toString() + "€";
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
		addPromotionButton.addActionListener(new ModifyPromotionListener());
		add(addPromotionButton);

		deleteProductButton.setPreferredSize(buttonDimension);
		deleteProductButton.addActionListener(new DeleteProductListener());
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
					// a little confirmation before doing it
					int quantityToAdd = dialog.getAddQuantity();
					if (!DialogHandler.showConfirmDialog(context, "Modifier ?",
							"Voulez vous vraiment ajouter " + quantityToAdd + " " + product.getName() + " ?")) {
						// user don't want to do this, return
						return;
					}
					protocolToSend = ProtocolFactory.createAddProductQuantityProtocol(product.getId(), quantityToAdd);
				} else {
					int quantityToRemove = dialog.getRemoveQuantity();
					if (!DialogHandler.showConfirmDialog(context, "Modifier ?",
							"Voulez vous vraiment enlever " + quantityToRemove + " " + product.getName() + " ?")) {
						// user don't want to do this, return
						return;
					}
					protocolToSend = ProtocolFactory.createRemoveProductQuantityProtocol(product.getId(),
							quantityToRemove);
				}

				// send this protocol to the server
				try {
					Protocol protocolRecieved = ServerConnectionHandler.getInstance()
							.sendProtocolMessage(protocolToSend);
					if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						DialogHandler.showInformationDialog(context, "Produit modifié",
								"La quantité du produit a été modifiée");
						// refresh the page to see the modification
						context.refreshPanel();
					} else {
						DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
					}
				} catch (IOException | InvalidProtocolException ex) {
					// very unlikely to happen in real scenarios
					ProductListPanel.logger
							.error("Modify product quantity string is not readable : " + ex.getMessage());
					DialogHandler.showErrorDialog(context, "Erreur",
							"Erreur lors de la réception du message du serveur");
				}
			}
		}

	}

	class ModifyPromotionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			BigDecimal oldPromotion = product.getPromotion();
			String successString = "";

			ModifyPromotionOption dialog = new ModifyPromotionOption(context, product);
			if (dialog.showPopUp()) {
				/*create the protocol depending on the option choose by user*/
				Protocol protocolToSend = null;
				// we have 2 cases here : user want to add/modify promotion, or delete it
				if (dialog.getUserActionChoosen() == ModifyPromotionOption.ADD_PROMOTION_OPTION) {
					String confirmationString;
					if (product.hasPromotion()) {
						confirmationString = "Voulez-vous vraiment remplacer l'ancienne promotion (montant : "
								+ oldPromotion + ") ?";
						successString = "Promotion modifiée avec succès";
					} else {
						confirmationString = "Voulez-vous vraiment ajouter une nouvelle promotion à ce produit ?";
						successString = "Promotion ajoutée avec succès";
					}

					if (DialogHandler.showConfirmDialog(context, "Confirmation", confirmationString)) {
						String idString = String.valueOf(product.getId());
						BigDecimal newPromotion = dialog.getPromotionAmount();
						protocolToSend = ProtocolFactory.createAddPromotionProtocol(idString, newPromotion.toString());
					}

				} else if (dialog.getUserActionChoosen() == ModifyPromotionOption.REMOVE_PROMOTION_OPTION) {
					// if no promotion exists, nothing to do
					if (product.hasPromotion()) {
						String content = "Voulez-vous vraiment supprimer cette promotion ?";
						if (DialogHandler.showConfirmDialog(context, "Supprimer", content)) {
							String idString = String.valueOf(product.getId());
							protocolToSend = ProtocolFactory.createRemovePromotionProtocol(idString);
							successString = "Promotion supprimée avec succès";
						}
					}
				}
				
				//if we have created a protocol in the process, send it to the server
				if(protocolToSend != null) {
					try {
						Protocol protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocolToSend);
						if(protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
							//show message depending on the action before
							DialogHandler.showConfirmDialog(context, "Succès", successString);
							context.refreshPanel();
						}else {
							DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
						}
					} catch (IOException | InvalidProtocolException ex) {
						ProductListPanel.logger.error("return code string is not readable : " + ex.getMessage());
						DialogHandler.showErrorDialog(context, "Erreur", "Erreur lors de la réception du message du serveur");
					}
				}
			}
		}

	}

	class DeleteProductListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String productName = product.getName();
			boolean wantToDelete = DialogHandler.showConfirmDialog(context, "Supprimer " + productName + " ?",
					"Voulez vous vraiment supprimer le produit \"" + productName
							+ "\".\nCela le fera disparaitre définitivement.");

			if (wantToDelete) {
				String idProduct = String.valueOf(product.getId());
				Protocol protocolToSend = ProtocolFactory.createDeleteProductProtocol(idProduct);

				// send this protocol to the server
				try {
					ServerConnectionHandler instance = ServerConnectionHandler.getInstance();
					Protocol protocolRecieved = instance.sendProtocolMessage(protocolToSend);
					if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						DialogHandler.showInformationDialog(context, "Produit supprimé",
								"Le produit a été supprimé avec succès");
						// refresh the page to see the modification
						context.refreshPanel();
					} else {
						DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
					}
				} catch (IOException | InvalidProtocolException ex) {
					ProductListPanel.logger
							.error("Remove product quantity string is not readable : " + ex.getMessage());
					DialogHandler.showErrorDialog(context, "Erreur",
							"Erreur lors de la réception du message du serveur");
				}

			}
		}
	}
}
