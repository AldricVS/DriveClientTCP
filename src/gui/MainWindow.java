package gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import gui.subwindows.employee_list.EmployeListPanel;
import gui.subwindows.home.HomePanel;
import gui.subwindows.menu.MenuPanel;
import gui.subwindows.order_list.OrderListPanel;
import gui.subwindows.product_list.ProductListPanel;
import logger.LoggerUtility;

/**
 * The main window that user will interact with during all process. This window
 * contains all pages used for this app.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outtlok.fr>
 */
public class MainWindow extends JFrame {
	private static Logger logger = LoggerUtility.getLogger(MainWindow.class, LoggerUtility.LOG_PREFERENCE);

	private MainWindow context = this;
	private Container contentPane;
	
	public static final Dimension MAIN_DIMENSION = new Dimension(GuiConstants.WIDTH, GuiConstants.HEIGHT);

	/**
	 * Card layout is the main panel used in order to navigate through all
	 * subwindows.
	 */
	CardLayout cardLayout = new CardLayout();
	
	/**
	 * All subwindows of the app
	 */
	HomePanel homePanel = new HomePanel(context);
	MenuPanel menuPanel = new MenuPanel(context);
	ProductListPanel productListPanel = new ProductListPanel(context);
	OrderListPanel orderListPanel = new OrderListPanel(context);
	EmployeListPanel employeListPanel = new EmployeListPanel(context);

	public MainWindow() {
		super("Drivepicerie");
		contentPane = getContentPane();
		init();
		addSubWindows();
	}

	private void init() {
		// set the global UI look like the others windows on the current OS
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			logger.warn("Cannot change UI look and feel : " + e.getMessage());
		}

		setPreferredSize(MAIN_DIMENSION);
		setLayout(cardLayout);
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowCloseListener());
		setResizable(false);
	}

	private void addSubWindows() {
		contentPane.add(homePanel, WindowName.HOME.name());
		contentPane.add(menuPanel, WindowName.MENU.name());
		contentPane.add(productListPanel, WindowName.PRODUCT_LIST.name());
		contentPane.add(orderListPanel, WindowName.ORDER_LIST.name());
		contentPane.add(employeListPanel, WindowName.EMPLOYEE_LIST.name());
		
		//focus on the home panel
		cardLayout.show(contentPane, WindowName.HOME.name());
	}

	class WindowCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			/*
			 * TODO : add a confirm pop-up and if click yes AND client authenticated, send a
			 * disconnect message.
			 */

		}
	}
}
