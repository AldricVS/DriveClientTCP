package gui.subwindows.popup_window;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class addProductPanel extends JOptionPane{
	
	//Panel
	JPanel addProductPanel = new JPanel();
	
	//TextLabel
	//private static int LABEL_DIMENSION = GuiConstants.;
	JLabel textName = new JLabel("Nom du Produit");
	JLabel textPrice = new JLabel("Prix du Produit");
	JLabel textQuantity = new JLabel("Quantité du Produit");
	
	//TextArea
	//private static int FIELD_DIMENSION = GuiConstants.;
	JTextField fieldName = new JTextField(20);
	JFormattedTextField fieldPrice;
	JFormattedTextField fieldQuantity;

	private static String[] options = {"Ajouter", "Annuler"};
	
	public addProductPanel() {
		addProductPanel.setLayout(new GridBagLayout());
		initFormattedTextFileds();
		GridBagConstraints c = new GridBagConstraints();
		//c.weightx = 2;
		//c.weighty = 3;
		c.insets = new Insets(5, 5, 5, 5);
		
		//Ajout des TextFields
		c.gridx = 0;
		c.gridy = 0;
		addProductPanel.add(textName, c);
		
		c.gridx = 0;
		c.gridy = 1;
		addProductPanel.add(textPrice, c);
		
		c.gridx = 0;
		c.gridy = 2;
		addProductPanel.add(textQuantity, c);
		
		
		//Ajout des textArea
		c.gridwidth = 2;
		
		c.gridx = 1;
		c.gridy = 0;
		addProductPanel.add(fieldName, c);
		
		c.gridx = 1;
		c.gridy = 1;
		addProductPanel.add(fieldPrice, c);
		
		c.gridx = 1;
		c.gridy = 2;
		addProductPanel.add(fieldQuantity, c);
	}
	
	private void initFormattedTextFileds() {
		NumberFormat integerNumberFormat = NumberFormat.getIntegerInstance();
		//remove comas to separate each digits
		integerNumberFormat.setGroupingUsed(false);
		NumberFormat priceNumberFormat = NumberFormat.getNumberInstance();
		
		//quantity cannot be more than 999
		integerNumberFormat.setMaximumIntegerDigits(3);
		
		//price cannot be more than 999.99€
		priceNumberFormat.setMaximumIntegerDigits(3);
		priceNumberFormat.setMaximumFractionDigits(2);
		
		fieldPrice = new JFormattedTextField(priceNumberFormat);
		fieldQuantity = new JFormattedTextField(integerNumberFormat);
	}

	/**
	 * @return true if an Element will be added, false if canceled
	 */
	public boolean getPopup() {
		int answer;
		answer = showOptionDialog(null, addProductPanel, "Ajouter un produit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if ((answer == JOptionPane.NO_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
			return false;
		}
		return true;
	}
	
	public String getNameProduct() {
		return fieldName.getText();
	}
	
	public String getPriceProduct() {
		//price is displayed with ",", we need to replace it
		return fieldPrice.getText().replace(',', '.');
	}
	
	public String getQuantityProduct() {
		return fieldPrice.getText();
	}
}
