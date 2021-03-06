package gui.components;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.JOptionPane;

import data.Protocol;
import data.enums.ActionCodes;

/**
 * Class used to create and show pop-ups to user
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class DialogHandler {
	
	/**
	 * Show a simple information message to the user.
	 * @param parentComponent in front of which component the dialog will appear
	 * @param title the title of the dialog
	 * @param content the message to send to the user
	 */
	public static void showInformationDialog(Component parentComponent, String title, String content) {
		JOptionPane.showMessageDialog(parentComponent, content, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Show a simple error message to the user.
	 * @param parentComponent in front of which component the dialog will appear
	 * @param title the title of the dialog
	 * @param content the message to send to the user
	 */
	public static void showErrorDialog(Component parentComponent, String title, String content) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(parentComponent, content, title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Display the error message of the protocol recieved from the server to the user. 
	 * If protocol is not an error protocol nor protocol has no error message, nothing will be displayed 
	 * @param parentComponent in front of which component the dialog will appear
	 * @param errorProtocol the error protocol recieved 
	 */
	public static void showErrorDialogFromProtocol(Component parentComponent, Protocol errorProtocol) {
		//we check if it is really an error protocol
		if (errorProtocol.getActionCode() == ActionCodes.ERROR && errorProtocol.getOptionsListSize() > 0) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(parentComponent, errorProtocol.getOptionsElement(0), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
		else {
			showErrorDialog(parentComponent, "Erreur", "Erreur dans la communication avec le Serveur");
		}
	}
	
	/**
	 * Show a dialog that asks for user confirmation.
	 * @param parentComponent in front of which component the dialog will appear
	 * @param title the title of the dialog
	 * @param content the message to send to the user
	 * @return {@code true} if user choose "Yes" option, {@code false} else
	 */
	public static boolean showConfirmDialog(Component parentComponent, String title, String content) {
		int answer = JOptionPane.showConfirmDialog(parentComponent, content, title, JOptionPane.YES_NO_OPTION);
		return answer == JOptionPane.YES_OPTION;
	}
	
	
}
