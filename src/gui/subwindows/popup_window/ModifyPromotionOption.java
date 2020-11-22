/**
 * 
 */
package gui.subwindows.popup_window;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import data.Product;

/**
 * Dialog that ask to user a change in the promotion of a specific product. He
 * can either add one new, modify one existing, or delete it.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ModifyPromotionOption extends JOptionPane {

	private Component context;

	private static final String[] options = { "Modifier", "Annuler" };
	private static final double MIN_AMOUNT = 0.01;
	private static final double STEP_SIZE = 0.01;
	
	public static final int ADD_PROMOTION_OPTION = 1;
	public static final int REMOVE_PROMOTION_OPTION = 2;

	private JLabel initialPriceLabel;
	private JLabel promotionAmountLabel;

	private JCheckBox promotionCheckBox = new JCheckBox("Produit en promotion");
	private JSpinner amountSpinner;

	private Product product;

	public ModifyPromotionOption(Component context, Product product) {
		this.product = product;
		this.context = context;
		initComponents();
		initLayout();

	}

	private void initComponents() {
		// set spinner min and max value depending on the price of the product
		BigDecimal price = product.getPrice();
		BigDecimal promotion = product.getPromotion();
		double priceDoubleValue = price.doubleValue();
		SpinnerModel priceSpinnerModel = new SpinnerNumberModel(priceDoubleValue, MIN_AMOUNT, priceDoubleValue, STEP_SIZE);
		amountSpinner = new JSpinner(priceSpinnerModel);

		// define labels depending on the actual promotion state of the product
		initialPriceLabel = new JLabel("Prix initial : " + price + " €");

		boolean hasPromotion = product.hasPromotion();
		if (hasPromotion) {
			promotionAmountLabel = new JLabel("Prix promotionnel : " + promotion + " €", SwingConstants.CENTER);
		} else {
			promotionAmountLabel = new JLabel("Prix promotionnel : " + price + " €", SwingConstants.CENTER);
		}
		promotionCheckBox.setSelected(hasPromotion);
		amountSpinner.setEnabled(hasPromotion);
		promotionAmountLabel.setEnabled(hasPromotion);

		// init listeners
		promotionCheckBox.addActionListener(new CheckBoxListener());
		amountSpinner.addChangeListener(new ChangeAmountListener());
	}

	private void initLayout() {
		this.removeAll();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// vertical glue is used to make sure that all component take all the space of
		// the panel
		this.add(initialPriceLabel);
		initialPriceLabel.setAlignmentX(CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());

		this.add(promotionCheckBox);
		promotionCheckBox.setAlignmentX(CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());

		this.add(amountSpinner);
		amountSpinner.setAlignmentX(CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());

		this.add(promotionAmountLabel);
		promotionAmountLabel.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	public BigDecimal getPromotionAmount() {
		double value = (double)amountSpinner.getValue();
		return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
	}
	
	public boolean showPopUp() {
		int answer = showOptionDialog(context,
				this,
				"Promotion de " + product.getName(),
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				null,
				options,
				options[0]);
	
		return answer == JOptionPane.OK_OPTION;
	}
	
	/**
	 * Get what user have choosen between adding/modifying or removing promotion.
	 * @return {@link ModifyProductQuantityOption#ADD_QUANTITY_OPTION} or {@link ModifyProductQuantityOption#REMOVE_QUANTITY_OPTION}
	 */
	public int getUserActionChoosen() {
		if(promotionCheckBox.isSelected()) {
			return ADD_PROMOTION_OPTION;
		}else {
			return REMOVE_PROMOTION_OPTION;
		}
	}

	class CheckBoxListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// we just want to enable or disable some fields depending on if the checkbox is
			// selected or not
			boolean isSelected = promotionCheckBox.isSelected();
			promotionCheckBox.setSelected(isSelected);
			amountSpinner.setEnabled(isSelected);
			promotionAmountLabel.setEnabled(isSelected);
		}
	}

	class ChangeAmountListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			// Change the promotion label depending on the spinner's value
			BigDecimal newPromotion = getPromotionAmount();
			promotionAmountLabel.setText("Prix promotionnel : " + newPromotion + " €");
		}
	}
}
