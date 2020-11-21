package gui.subwindows.employee_list;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.User;

/**
 * 
 * @author Maxence
 */
public class EmployeePanel extends JPanel {
	private Dimension productDimension;
	private Dimension buttonDimension;
	private Dimension fieldDimension;
	private JTextField employeeNameField = new JTextField();
	private JButton changePasswordButton = new JButton("Reinitialiser le mot de passe");
	
	/**
	 * 
	 * @param product the product shown in this row
	 * @param productDimension Dimension of the Panel
	 */
	public EmployeePanel(EmployeeListPanel context, User employee, Dimension productDimension) {
		this.productDimension = productDimension;
		fieldDimension = new Dimension(productDimension.width / 3, productDimension.height / 6);
		buttonDimension = new Dimension(productDimension.width / 4, 2 * productDimension.height / 3);
		
		setText(employee);
		init();
		initField();
		initButton();
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(productDimension);
	}
	
	private void setText(User employee) {
		employeeNameField.setText(employee.getName());
	}

	private void initField() {
		employeeNameField.setPreferredSize(fieldDimension);
		employeeNameField.setEditable(false);
		add(employeeNameField);
	}
	
	private void initButton() {
		changePasswordButton.setPreferredSize(buttonDimension);
		//addQuantityButton.addActionListener();
		add(changePasswordButton);
	}
}
