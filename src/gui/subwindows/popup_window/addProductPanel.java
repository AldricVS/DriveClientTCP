package gui.subwindows.popup_window;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.GuiConstants;

public class addProductPanel {
	
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
	JTextField fieldPrice = new JTextField(20);
	JTextField fieldQuantity = new JTextField(20);

	private static String[] options = {"Ajouter", "Annuler"};
	
	public addProductPanel() {
		addProductPanel.setLayout(new GridBagLayout());
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
	
	public void getPopup() {
		JOptionPane.showOptionDialog(null, addProductPanel, "Ajouter un produit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
	}
	
	public String getNameProduct() {
		return fieldName.getText();
	}
	
	public String getPriceProduct() {
		return fieldPrice.getText();
	}
	
	public String getQuantityProduct() {
		return fieldQuantity.getText();
	}
}
