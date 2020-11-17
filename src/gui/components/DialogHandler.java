package gui.components;

import java.awt.Component;

import javax.swing.JOptionPane;

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
		JOptionPane.showMessageDialog(parentComponent, content, title, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Show a dialog that asks for user confirmation.
	 * @param parentComponent in front of which component the dialog will appear
	 * @param title the title of the dialog
	 * @param content the message to send to the user
	 * @return {@code true} if user choose "Yes" option, {@code false} else
	 */
	public static boolean showChoiceDialog(Component parentComponent, String title, String content) {
		int answer = JOptionPane.showConfirmDialog(parentComponent, content, title, JOptionPane.YES_NO_OPTION);
		return answer == JOptionPane.YES_OPTION;
	}
}
