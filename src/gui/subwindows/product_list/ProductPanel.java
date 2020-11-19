package gui.subwindows.product_list;

import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.Product;

/**
 * Panel shwoing a single product
 * @author Maxence Hennekein
 */
public class ProductPanel extends JPanel {
	
	private Product product;
	
	private Dimension productDimension;
	private Dimension buttonDimension;
	private Dimension fieldDimension;
	private JTextField productNameField = new JTextField();
	private JTextField productPriceField = new JTextField();
	private JTextField productQuantityField = new JTextField();
	private JButton addQuantityButton = new JButton("Modifier Quantité");
	private JButton addPromotionButton = new JButton("Ajouter une promotion");
	private JButton deleteProductButton = new JButton("Supprimer le produit");
	
	/**
	 * 
	 * @param product the product shown in this row
	 * @param productDimension Dimension of the Panel
	 */
	public ProductPanel(Product product, Dimension productDimension) {
		//we have to keep trace of the product for action listeners
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
		productQuantityField.setText( ((Integer)product.getQuantity()).toString());
		
		//we will check if we have a promotion, and add it here if it is the case
		String priceText = ((BigDecimal)product.getPrice()).toString() + "€";
		if(product.hasPromotion()) {
			String promotionText = product.getPromotion().toString() + "€";
			productPriceField.setText(promotionText + "(prix normal : " + priceText + ")");
		}else {
			productPriceField.setText(priceText);
		}
		
		
	}

	private void initField() {
		productNameField.setPreferredSize(fieldDimension);
		productNameField.setEditable(false);
		productNameField.setFont(productNameField.getFont().deriveFont(20f));
		add(productNameField);
		
		//TODO appliquer la promotion (avec un style etout)
		productPriceField.setPreferredSize(fieldDimension);
		productPriceField.setEditable(false);
		//a promotion will be bold instead
		if(product.hasPromotion()) {
			productPriceField.setFont(productNameField.getFont().deriveFont(20f));
		}else {
			productPriceField.setFont(productNameField.getFont().deriveFont(Font.BOLD, 20f));
		}
		
		
		add(productPriceField);
		
		productQuantityField.setPreferredSize(fieldDimension);
		productQuantityField.setEditable(false);
		productQuantityField.setFont(productNameField.getFont().deriveFont(20f));
		add(productQuantityField);
	}
	
	private void initButton() {
		addQuantityButton.setPreferredSize(buttonDimension);
		//addQuantityButton.addActionListener();
		add(addQuantityButton);
		
		addPromotionButton.setPreferredSize(buttonDimension);
		add(addPromotionButton);
		
		deleteProductButton.setPreferredSize(buttonDimension);
		add(deleteProductButton);
	}
}
