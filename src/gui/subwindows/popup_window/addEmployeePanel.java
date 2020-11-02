package gui.subwindows.popup_window;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class addEmployeePanel extends JOptionPane{
	
	//Panel
	JPanel addEmployeePanel = new JPanel();
	
	//TextLabel
	//private static int LABEL_DIMENSION = GuiConstants.;
	JLabel textName = new JLabel("Nom de l'Employe");
	JLabel textPassword = new JLabel("Mot de Passe");
	JLabel textConfirmPassword = new JLabel("Confirmer Mot de Passe");
	
	//TextArea
	//private static int FIELD_DIMENSION = GuiConstants.;
	JTextField fieldName = new JTextField(20);
	JTextField fieldPassword = new JTextField(30);
	JTextField fieldConfirmPassword = new JTextField(30);

	private static String[] options = {"Ajouter", "Annuler"};
	
	public addEmployeePanel() {
		addEmployeePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//c.weightx = 2;
		//c.weighty = 3;
		c.insets = new Insets(5, 5, 5, 5);
		
		//Ajout des TextFields
		c.gridx = 0;
		c.gridy = 0;
		addEmployeePanel.add(textName, c);
		
		c.gridx = 0;
		c.gridy = 1;
		addEmployeePanel.add(textPassword, c);
		
		c.gridx = 0;
		c.gridy = 2;
		addEmployeePanel.add(textConfirmPassword, c);
		
		
		//Ajout des textArea
		c.gridwidth = 2;
		
		c.gridx = 1;
		c.gridy = 0;
		addEmployeePanel.add(fieldName, c);
		
		c.gridx = 1;
		c.gridy = 1;
		addEmployeePanel.add(fieldPassword, c);
		
		c.gridx = 1;
		c.gridy = 2;
		addEmployeePanel.add(fieldConfirmPassword, c);
	}
	
	/**
	 * 
	 * @return true if an Element will be added, false if canceled
	 */
	public boolean getPopup() {
		int answer;
		answer = showOptionDialog(null, addEmployeePanel, "Ajouter un Employe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if ((answer == JOptionPane.NO_OPTION) || (answer == JOptionPane.CLOSED_OPTION)) {
			return false;
		}
		return true;
	}
	
	public String getNameEmployee() {
		return fieldName.getText();
	}
	
	public String getPassword() {
		return fieldPassword.getText();
	}
	
	public String getPasswordConfirm() {
		return fieldConfirmPassword.getText();
	}
}
