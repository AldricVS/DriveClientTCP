package gui.subwindows.popup_window;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.Product;

/**
 * Dialog that asks to user a change in the quantity of a specific product
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ModifyProductQuantityOption extends JOptionPane {
	
	public static final int ADD_QUANTITY_OPTION = 1;
	public static final int REMOVE_QUANTITY_OPTION = 2;
	
	private JPanel context;
	private Product product;

	private ButtonGroup addRemButtonGroup = new ButtonGroup();
	
	private JRadioButton addRadioButton = new JRadioButton("Ajouter"); 
	private JRadioButton removeRadioButton = new JRadioButton("Enlever"); 
	
	private JSpinner addQuantitySpinner = new JSpinner();
	private JSpinner removeQuantitySpinner = new JSpinner();
	
	//those 2 attributes are here to help user to keep trace of old and new quantity
	private JLabel currentQuantityLabel;
	private JLabel modifiedQuantityLabel;
	
	private final String options[] = {"Valider", "Annuler"};
	private final String BEFORE_MODIF_STRING = "Avant modification : ";
	private final String AFTER_MODIF_STRING = "Après modification : ";
	
	public ModifyProductQuantityOption(JPanel context, Product product) {
		this.context = context;
		this.product = product;
		this.removeAll();
		//create a grid with 3 rows and 2 columns, with a 10 pixels gap between each cell
		this.setLayout(new GridLayout(3, 2, 10, 10));
		
		/*we must define 2 spinners models*/
		//the first one is for addQuantitySpiner, we can't add less than 1 thing and don't really have upper limit,
		//but we will not accept more than 1000 additions. 
		SpinnerModel spinnerAddQuantityModel = new SpinnerNumberModel(1, 1, 1000, 1);
		addQuantitySpinner.setModel(spinnerAddQuantityModel);
		addQuantitySpinner.addChangeListener(new SpinnerChangeListener());
		
		//the second one is for removeQuantitySpinner, we can't remove less than zero thing and have 
		//the limit set to the current product quantity 
		SpinnerModel spinnerRemoveQuantityModel = new SpinnerNumberModel(1, 0, product.getQuantity(), 1);
		removeQuantitySpinner.setModel(spinnerRemoveQuantityModel);
		removeQuantitySpinner.addChangeListener(new SpinnerChangeListener());
		
		/*Define JLabels*/
		currentQuantityLabel = new JLabel(BEFORE_MODIF_STRING + product.getQuantity());
		modifiedQuantityLabel = new JLabel(AFTER_MODIF_STRING + (product.getQuantity() + 1));
		
		//default one is the addSpinner
		addRadioButton.setSelected(true);
		removeRadioButton.setSelected(false);
		addQuantitySpinner.setEnabled(true);
		removeQuantitySpinner.setEnabled(false);
		
		/*put elements in the panel in the right order*/
		this.add(addRadioButton);
		this.add(removeRadioButton);
		this.add(addQuantitySpinner);
		this.add(removeQuantitySpinner);
		this.add(currentQuantityLabel);
		this.add(modifiedQuantityLabel);
		
		/*and add needed listeners*/
		//we need to have radioButtons linked 
		addRemButtonGroup.add(addRadioButton);
		addRemButtonGroup.add(removeRadioButton);
		removeRadioButton.addActionListener(new RadioChoiceListener());
		addRadioButton.addActionListener(new RadioChoiceListener());
		
	}
	
	public boolean showPopUp() {
		int answer = showOptionDialog(context,
				this,
				"Modifier la quantité de " + product.getName(),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]);
	
		return answer == JOptionPane.OK_OPTION;
	}
	
	/**
	 * Get what user have choosen between adding or removing quantity.
	 * @return {@link ModifyProductQuantityOption#ADD_QUANTITY_OPTION} or {@link ModifyProductQuantityOption#REMOVE_QUANTITY_OPTION}
	 */
	public int getUserActionChoosen() {
		if(addRadioButton.isSelected()) {
			return ADD_QUANTITY_OPTION;
		}else {
			return REMOVE_QUANTITY_OPTION;
		}
	}
	
	/**
	 * You must ask which choice user have made with {@link ModifyProductQuantityOption#getUserActionChoosen}
	 * @return the value stored in the remove spinner
	 */
	public int getRemoveQuantity() {
		return (int)removeQuantitySpinner.getValue();
	}
	
	/**
	 * You must ask which choice user have made with {@link ModifyProductQuantityOption#getUserActionChoosen}
	 * @return the value stored in the add spinner
	 */
	public int getAddQuantity() {
		return (int)addQuantitySpinner.getValue();
	}
	
	class RadioChoiceListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//check which button is cicked, and disable the spinner corresponding to the other one
			//also, update modifiedQuantityLabel to show to user 
			int modifiedQuantity;
			if(e.getSource() == addRadioButton) {
				addQuantitySpinner.setEnabled(true);
				removeQuantitySpinner.setEnabled(false);
				modifiedQuantity = getAddQuantity() + product.getQuantity();
			}else {
				removeQuantitySpinner.setEnabled(true);
				addQuantitySpinner.setEnabled(false);
				modifiedQuantity = product.getQuantity() - getRemoveQuantity();
			}
			modifiedQuantityLabel.setText(AFTER_MODIF_STRING + modifiedQuantity);
		}
		
	}
	
	class SpinnerChangeListener implements ChangeListener{
		
		@Override
		public void stateChanged(ChangeEvent e) {
			//modifiy the modifiedQuantitylabel depending on the value changed
			int modifiedQuantity;
			if(e.getSource() == addQuantitySpinner) {
				modifiedQuantity = getAddQuantity() + product.getQuantity();
			}else {
				modifiedQuantity = product.getQuantity() - getRemoveQuantity();
			}
			modifiedQuantityLabel.setText(AFTER_MODIF_STRING + modifiedQuantity);
		}
		
	}
	
}
