package gui.subwindows.product_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Product;
import gui.GuiConstants;

/**
 * 
 * @author Maxence
 */
public class ProductPanel extends JPanel {
	private JTextField productNameField = new JTextField();
	private JTextField productPriceField = new JTextField();
	private JTextField productQuantityField = new JTextField();
	private JButton addQuantityButton = new JButton("Modifier Quantité");
	private JButton addPromotionButton = new JButton("Ajouter une promotion");
	private JButton deleteProductButton = new JButton("Supprimer le produit");
	
	
	public ProductPanel(Product product, Dimension productDimension) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setPreferredSize(productDimension);
		setText(product);
		init();
	}
	
	private void setText(Product product) {
		productNameField.setText(product.getName());
		productPriceField.setText( ((BigDecimal)product.getPrice()).toString());
		productQuantityField.setText( ((Integer)product.getQuantity()).toString());
	}

	private void init() {
		add(productNameField);
		add(productPriceField);
		add(productQuantityField);
		
		add(addQuantityButton);
		add(addPromotionButton);
		add(deleteProductButton);
	}
}
