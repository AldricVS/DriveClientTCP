/**
 * 
 */
package gui.subwindows.home;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import gui.MainWindow;

/**
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outtlok.fr>
 */
public class HomePanel extends JPanel {

	private MainWindow context;
	
	/**
	 * 
	 */
	public HomePanel(MainWindow context) {
		this.context = context;
		init();
	}

	private void init() {
		setLayout(new BorderLayout());
		
		/*testing*/ 
		JPanel coloredPanel = new JPanel();
		coloredPanel.setBackground(Color.RED);
		add(coloredPanel, BorderLayout.CENTER);
	}
}
