package gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import exceptions.ServerConnectionLostException;
import gui.components.DialogHandler;
import gui.subwindows.employee_list.EmployeeListPanel;
import gui.subwindows.home.ConnexionPanel;
import gui.subwindows.menu.MenuPanel;
import gui.subwindows.order_list.OrderListPanel;
import gui.subwindows.product_list.ProductListPanel;
import logger.LoggerUtility;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolExtractor;
import process.protocol.ProtocolFactory;

/**
 * The main window that user will interact with during all process. This window
 * contains all pages used for this app.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence Hennekein
 */
public class MainWindow extends JFrame {
	public static Logger logger = LoggerUtility.getLogger(MainWindow.class, LoggerUtility.LOG_PREFERENCE);

	private MainWindow context = this;
	private Container contentPane;
	
	public static final Dimension MAIN_DIMENSION = new Dimension(GuiConstants.WIDTH, GuiConstants.HEIGHT);
	
	private String ipAdress;
	private int port;

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

	public MainWindow(String ipAdress, int port) {
		super("Drivepicerie");
		this.ipAdress = ipAdress;
		this.port = port;
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
				disconnect();
				System.exit(0);
			}
		}
	}
	
	/**
	 * Change the current window displayed to the one specifed
	 * @param name The {@link gui.WindowName WindowName} of the window desired
	 */
	public void changeWindow(WindowName windowName) {
		cardLayout.show(contentPane, windowName.name());
	}
	
	/**
	 * Start the connection with the {@link process.connection.ServerConnectionHandler Server} using given Id & password
	 * @param id ID of Employee
	 * @param mdp password of Employee
	 * @param asAdmin if you want to try the connection as an administrator
	 * @return true if connection has been done successfully
	 */
	public boolean launchConnection(String id, String mdp, boolean asAdmin) {
		//send a protocol
		Protocol connectProtocol;
		Protocol answer = null;
		try {
			//start connection with the server
			ServerConnectionHandler.getInstance().initConnection(ipAdress, port);
			connectProtocol = ProtocolFactory.createConnectionProtocol(id, mdp, asAdmin);
			
			logger.info("Connection attempt: " + connectProtocol.toString());
			answer = ServerConnectionHandler.getInstance().sendProtocolMessage(connectProtocol);
			//check if we got a successful actionCode 
			ProtocolExtractor extractor = new ProtocolExtractor(answer.toString());
			extractor.assertActionCodeValid(ActionCodes.SUCESS);
			
			//As administrator, we have some initialization to do
			if (asAdmin) {
				menuPanel.initAdmin();
			}
			logger.info("=== CONNECTED ===");
			return true;
		}
		catch (UnknownHostException e) {
			logger.error("Impossible de joindre l'adresse IP fournie");
		}
		catch (InvalidProtocolException e) {
			logger.error("Erreur dans la formulation d'un Protocol");
		}
		catch (IOException e) {
			logger.error("Cannot connect on port " + port + " at " + ipAdress);
		}
		catch (ServerConnectionLostException ex) {
			logger.error(ex.getMessage());
		}
		//Send a disconnect message to the server
		disconnect();
		if (answer != null) {
			DialogHandler.showErrorDialogFromProtocol(this, answer);
		}
		else {
			DialogHandler.showErrorDialog(this, "Erreur", "Impossible de lancer la connexion avec le serveur");
		}
		return false;
	}
	
	/**
	 * main method to disconnect, as it will call multiple submethod in other classes
	 */
	public void disconnect() {
		if(ServerConnectionHandler.getInstance().isConnected()) {
			menuPanel.disconnectAdmin();
			//employeeListPanel.clear(); -> Vide la liste d'employe
			ServerConnectionHandler.getInstance().disconnect();
			logger.info("=== DISCONNECTED ===");
		}
	}
	
	public void initProductList(Protocol productList) {
		productListPanel.initPanel(productList);
	}

	public void initListOrder(Protocol orderList) {
		orderListPanel.initPanel(orderList);
	}

	public void initEmployeeList(Protocol employeeList) {
		employeeListPanel.initPanel(employeeList);
	}
	
}
