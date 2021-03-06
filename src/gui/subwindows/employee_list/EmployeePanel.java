package gui.subwindows.employee_list;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.Employee;
import data.Protocol;
import data.User;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import exceptions.ServerConnectionLostException;
import gui.WindowName;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.addEmployeePanel;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolFactory;

/**
 * 
 * @author Maxence
 */
public class EmployeePanel extends JPanel {
	private Dimension productDimension;
	private Dimension buttonDimension;
	private Dimension fieldDimension;
	private JTextField employeeNameField = new JTextField();
	//private JButton changePasswordButton = new JButton("Reinitialiser le mot de passe");
	private JButton deleteEmployeeButton = new JButton("Renvoyer l'employ�");
	
	private EmployeeListPanel context;
	private Employee employee;
	/**
	 * 
	 * @param product the product shown in this row
	 * @param employeeDimension Dimension of the Panel
	 */
	public EmployeePanel(EmployeeListPanel context, Employee employee, Dimension employeeDimension) {
		this.context = context;
		this.employee = employee;
		this.productDimension = employeeDimension;
		fieldDimension = new Dimension(employeeDimension.width / 4, employeeDimension.height / 6);
		buttonDimension = new Dimension(employeeDimension.width / 5, 2 * employeeDimension.height / 3);
		
		setText();
		init();
		initField();
		initButton();
	}
	
	private void init() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setMinimumSize(productDimension);
	}
	
	private void setText() {
		String descriptionString = employee.getName();
		String lastConnectionDate = employee.getLastConnectionDate();
		if(lastConnectionDate != null) {
			descriptionString += ". Derni�re connexion le " + lastConnectionDate + ".";
		}else {
			descriptionString += ". Jamais connect� pour l'instant.";
		}
		employeeNameField.setText(descriptionString);
	}

	private void initField() {
		employeeNameField.setPreferredSize(fieldDimension);
		employeeNameField.setEditable(false);
		add(employeeNameField);
	}
	
	private void initButton() {
		/*
		changePasswordButton.setPreferredSize(buttonDimension);
		changePasswordButton.addActionListener(new ActionChangePassword());
		add(changePasswordButton);
		*/
		deleteEmployeeButton.setMaximumSize(buttonDimension);
		deleteEmployeeButton.addActionListener(new ActionDeleteEmployee());
		add(deleteEmployeeButton);
	}
	
	
	/*
	class ActionChangePassword implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addEmployeePanel addEmployeePopup = new addEmployeePanel();
			if (addEmployeePopup.getChangePasswordPopup(employee.getName())) {
				String name = addEmployeePopup.getNameEmployee();
				String password = addEmployeePopup.getPassword();
				String passwordConfirm = addEmployeePopup.getPasswordConfirm();
				Protocol protocolToSend = ProtocolFactory.createAddEmployeeProtocol(name, password);
			}
		}
	}
	*/
	
	class ActionDeleteEmployee implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			boolean result;
			result = DialogHandler.showConfirmDialog(context, "Suppression d'un employ�", "Voulez-vous supprimer l'employ� " + employee.getName() + " ?");
			if (result) {
				try {
				Protocol protocolToSend = ProtocolFactory.createRemoveEmployeeProtocol(employee.getName());
				Protocol protocolRecieved;
					protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocolToSend);
					if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
						DialogHandler.showInformationDialog(context, "Employ� vir�", "L'employ� est d�sormais au chomage !");
						// refresh the page to see the modification
						context.refreshPanel();
					} else {
						DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
					}
				} catch (IOException |InvalidProtocolException ex) {
					EmployeeListPanel.logger.error("Couldn't remove Employee: " + ex.getMessage());
					DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
				} catch (ServerConnectionLostException ex) {
					EmployeeListPanel.logger.error(ex.getMessage());
					DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
					context.disconnect();
				}
			}
		}
	}
	
}
