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
import javax.swing.text.AbstractDocument;
import javax.swing.text.NumberFormatter;

import gui.components.document_filters.IntegerDocumentFilter;
import gui.components.document_filters.PriceDocumentFilter;

/**
 * Pop up panel that user have to fill in order to create a new product
 * @author Maxence Hennkein
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class addProductPanel extends JOptionPane{
	private static final int NUMBER_COLUMNS = 20; 
	
	
	//Panel
	JPanel addProductPanel = new JPanel();
	
	//TextLabel
	//private static int LABEL_DIMENSION = GuiConstants.;
	JLabel textName = new JLabel("Nom du Produit");
	JLabel textPrice = new JLabel("Prix du Produit");
	JLabel textQuantity = new JLabel("Quantité du Produit");
	
	//TextArea
	//private static int FIELD_DIMENSION = GuiConstants.;
	JTextField fieldName = new JTextField(NUMBER_COLUMNS);
	JTextField fieldPrice;
	JTextField fieldQuantity;

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
		
		fieldPrice = new JTextField();
		AbstractDocument abstractDocumentPrice = (AbstractDocument)fieldPrice.getDocument();
		abstractDocumentPrice.setDocumentFilter(new PriceDocumentFilter());
		fieldPrice.setColumns(NUMBER_COLUMNS);


		fieldQuantity = new JTextField(NUMBER_COLUMNS);
		AbstractDocument abstractDocumentQuantity = (AbstractDocument)fieldQuantity.getDocument();
		abstractDocumentQuantity.setDocumentFilter(new IntegerDocumentFilter());
	}

	/**
	 * @return true if a valid element can be added, false if canceled or the fields's content are not valid
	 */
	public boolean getPopup() {
		int answer;
		answer = showOptionDialog(null, addProductPanel, "Ajouter un produit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if ((answer == JOptionPane.NO_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
			return false;
		}
		//we will check if all fields are filled correctly
		String nameProduct = getNameProduct();
		if(nameProduct.contains(";") || nameProduct.contains("<") || nameProduct.contains(">")) {
			return false;
		}
		try {
			//if values are not valid, they will throw a NumberFormatException
			Integer.parseInt(getQuantityProduct());
			new BigDecimal(getPriceProduct());
		}catch (NumberFormatException e) {
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
		return fieldQuantity.getText();
	}
}
