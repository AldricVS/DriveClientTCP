package gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import gui.subwindows.employee_list.EmployeeListPanel;
import gui.subwindows.home.ConnexionPanel;
import gui.subwindows.menu.MenuPanel;
import gui.subwindows.order_list.OrderListPanel;
import gui.subwindows.product_list.ProductListPanel;
import logger.LoggerUtility;
import process.connection.ServerConnectionHandler;

/**
 * The main window that user will interact with during all process. This window
 * contains all pages used for this app.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence Hennekein
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
	ConnexionPanel connexionPanel = new ConnexionPanel(context);
	MenuPanel menuPanel = new MenuPanel(context);
	ProductListPanel productListPanel = new ProductListPanel(context);
	OrderListPanel orderListPanel = new OrderListPanel(context);
	EmployeeListPanel employeeListPanel = new EmployeeListPanel(context);

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
		contentPane.add(connexionPanel, WindowName.LOGIN.name());
		contentPane.add(menuPanel, WindowName.MENU.name());
		contentPane.add(productListPanel, WindowName.PRODUCT_LIST.name());
		contentPane.add(orderListPanel, WindowName.ORDER_LIST.name());
		contentPane.add(employeeListPanel, WindowName.EMPLOYEE_LIST.name());
		
		//focus on the home panel
		cardLayout.show(contentPane, WindowName.LOGIN.name());
	}

	class WindowCloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent windowEvent) {
			int result = JOptionPane.showConfirmDialog(null, "Voulez-vous quitter ?", "Fermeture du programme...", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				//TODO: Disconnect if auth
				//getServerConnectionHandler().closeConnection();
				System.exit(0);
			}
		}
	}
	
	/**
	 * Change the current window displayed to the one specifed
	 * @param name The {@link gui.WindowName WindowName} of the window desired
	 */
	public void changeWindow(String name) {
		cardLayout.show(contentPane, name);
	}
	
	/**
	 * Start the connection with the {@link process.connection.ServerConnectionHandler Server} using given Id & password
	 * @param id ID of Employee
	 * @param mdp password of Employee
	 * @param asAdmin if you want to try the connection as an administrator
	 */
	public void launchConnection(String id, String mdp, boolean asAdmin) {
		try {
			//start connection with the server
			ServerConnectionHandler.getInstance().initConnection();
			//join id & mdp on a list
			LinkedList<String> args = new LinkedList<String>();
				args.add(id);
				args.add(mdp);
			//send a protocol
			Protocol tryConnect;
			if (asAdmin) {
				tryConnect = new Protocol(ActionCodes.CONNECTION_ADMIN, args);
			}
			else {
				tryConnect = new Protocol(ActionCodes.CONNECTION_NORMAL, args);
			}
			logger.info(tryConnect.toString());
			Protocol answer = ServerConnectionHandler.getInstance().sendProtocolMessage(tryConnect);
			logger.info(answer);
		}
		catch (UnknownHostException e) {
			logger.error("Impossible de joindre l'adresse IP fournit");
			e.printStackTrace();
		}
		catch (InvalidProtocolException e) {
			logger.error("Erreur dans la lecture du protocole");
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
