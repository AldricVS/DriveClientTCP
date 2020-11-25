package gui.subwindows.employee_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import data.Protocol;
import data.User;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import exceptions.ServerConnectionLostException;
import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.components.DialogHandler;
import gui.subwindows.popup_window.addEmployeePanel;
import logger.LoggerUtility;
import process.connection.ServerConnectionHandler;
import process.protocol.ProtocolFactory;
import process.protocol.ProtocolListExtractor;

/**
 * Panel showing all Employee, should only be access by Administrator
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence
 */
public class EmployeeListPanel extends JPanel {
	public static Logger logger = LoggerUtility.getLogger(EmployeeListPanel.class, LoggerUtility.LOG_PREFERENCE);
	private MainWindow context;
	private ArrayList<User> listEmployee;
    
    /**
     * TITLE 
     */
    private final Dimension TITLE_DIMENSION = new Dimension(GuiConstants.WIDTH / 2, 3 * GuiConstants.HEIGHT / 20);
    private JPanel titlePanel = new JPanel();
    private JLabel titlelabel = new JLabel("Drivepicerie - Liste des Employés", SwingConstants.CENTER);
    
    /**
     * SlideBar
     */
    private final Dimension LIST_DIMENSION = new Dimension(4 * GuiConstants.WIDTH / 5, 10 * GuiConstants.HEIGHT / 20);
    private final Dimension EMPLOYEE_LIST_DIMENSION = new Dimension(9 * LIST_DIMENSION.width / 10, LIST_DIMENSION.height / 5);
    private JScrollPane listScrollPanel = new JScrollPane();
    private JPanel employeeListPanel;
    
    /**
     * Button
     */
    private final Dimension BUTTONS_DIMENSION = new Dimension(3 * GuiConstants.WIDTH / 4, 5 * GuiConstants.HEIGHT / 20);
	private final Dimension BUTTON_SIZE = new Dimension(BUTTONS_DIMENSION.width / 3, BUTTONS_DIMENSION.height / 5);
	private final Dimension BUTTON_FILLER = new Dimension(2 * GuiConstants.WIDTH / 9, BUTTONS_DIMENSION.height);
    private JPanel buttonsPanel = new JPanel();
    private JButton addEmployeeButton = new JButton("Ajouter un nouveau produit");
    private JButton returnButton = new JButton("Retour");
    
    public EmployeeListPanel(MainWindow context) {
        this.context = context;
        init();
    }

	private void init() {
		setLayout(new BorderLayout());
		
		initTitle();
		initButtons();
		
		add(titlePanel, BorderLayout.NORTH);
		add(listScrollPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}
    
	private void initTitle() {
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setPreferredSize(TITLE_DIMENSION);
		titlelabel.setFont(new Font("Serif", Font.BOLD, TITLE_DIMENSION.height / 2));
		titlePanel.add(titlelabel);
	}
	
	public void initPanel(Protocol employeeList) {
		try {
			extractFromProtocol(employeeList);
			
			employeeListPanel = new JPanel();
			employeeListPanel.setLayout(new BoxLayout(employeeListPanel, BoxLayout.PAGE_AXIS));
			employeeListPanel.setPreferredSize(LIST_DIMENSION);
			initEmployeePanel(listEmployee);
			
			listScrollPanel.setViewportView(employeeListPanel);
			listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			
		} catch (InvalidProtocolException e) {
			//show error to user and go back to menu
			DialogHandler.showErrorDialog(context, "Erreur", e.getMessage());
			context.changeWindow(WindowName.MENU);
		}
	}
	
	private void extractFromProtocol(Protocol protocol) throws InvalidProtocolException {
		ProtocolListExtractor extractor = new ProtocolListExtractor(protocol);
		listEmployee = extractor.extractEmployeeList();
	}

	/**
	 * Transform the Employee list received into a Panel
	 * @param employeeList The list of all employee
	 * @param size the amount of Employee
	 */
	private void initEmployeePanel(List<User> employeeList) {
		//clear product panel in order to be sure that we start on a fresh start
		employeeListPanel.removeAll();
		for (Iterator<User> i = employeeList.iterator(); i.hasNext(); ) {
			User u = i.next();
			employeeListPanel.add(new EmployeePanel(this, u, EMPLOYEE_LIST_DIMENSION));
		}
	}
	
	private void initButtons() {
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
		buttonsPanel.setPreferredSize(BUTTONS_DIMENSION);
		
		addEmployeeButton.setAlignmentX(CENTER_ALIGNMENT);
		addEmployeeButton.setMaximumSize(BUTTON_SIZE);
		
		returnButton.setAlignmentX(CENTER_ALIGNMENT);
		returnButton.setMaximumSize(BUTTON_SIZE);
		
		addEmployeeButton.addActionListener(new ActionAddEmployee());
		returnButton.addActionListener(new ActionRetour());
		
		//add button
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(addEmployeeButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
		buttonsPanel.add(returnButton);
		buttonsPanel.add(Box.createRigidArea(BUTTON_FILLER));
	}
	
	class ActionAddEmployee implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			addEmployeePanel addEmployeePopup = new addEmployeePanel();
			boolean result;
			result = addEmployeePopup.getPopup();
			if (result) {
				String employeeName = addEmployeePopup.getNameEmployee();
				String employeePassword = addEmployeePopup.getPassword();
				String employeeConfirmPassword = addEmployeePopup.getPasswordConfirm();
				if (employeePassword.equals(employeeConfirmPassword)) {
					//start protocol to add new product
					Protocol protocol = ProtocolFactory.createAddEmployeeProtocol(employeeName, employeePassword);
					try {
						Protocol protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(protocol);
						if (protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
							DialogHandler.showInformationDialog(context, "Succès", "Bienvenue à "+employeeName+" qui rejoint l'équipe !");
							// refresh the page : ask a new selection to server
							refreshPanel();
						} else {
							DialogHandler.showErrorDialogFromProtocol(context, protocolRecieved);
						}
					} catch (IOException | InvalidProtocolException ex) {
						logger.error("Error while adding new Employee: " + ex.getMessage());
						DialogHandler.showErrorDialog(context, "Erreur", ex.getMessage());
					} catch (ServerConnectionLostException ex) {
						logger.error(ex.getMessage());
						DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
						context.disconnect();
					}
				}
				else {
					DialogHandler.showErrorDialog(context, "Erreur", "Validation du Mot de Passe incorrect");
				}
			}
			//has the popup was closed, nothing happen
		}
	}
	
    class ActionRetour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.changeWindow(WindowName.MENU);
		}
	}
    /**
     * Refresh the panel asking to the server the product list
     */
	public void refreshPanel() {
		boolean isRefreshValid = false;
		try {
			Protocol protocolRecieved = ServerConnectionHandler.getInstance().sendProtocolMessage(ProtocolFactory.createGetListEmployeeProtocol());
			if(protocolRecieved.getActionCode() == ActionCodes.SUCESS) {
				extractFromProtocol(protocolRecieved);
				initEmployeePanel(listEmployee);
				listScrollPanel.setViewportView(employeeListPanel);
				repaint();
				employeeListPanel.repaint();
				isRefreshValid = true;
			}
		} catch (IOException | InvalidProtocolException e) {
			//we can't do anymore here, go back to menu
			logger.error("Can't retrieve information from string : " + e.getMessage());
			DialogHandler.showErrorDialog(context, "Rafraichissement impossible", "Impossible de récupérer la liste des employés, retour au menu.");
		} catch (ServerConnectionLostException ex) {
			logger.error(ex.getMessage());
			DialogHandler.showErrorDialog(context, "Fin de la Connection", ex.getMessage());
			context.disconnect();
		}
		
		//if something bad happens, go to menu
		if(!isRefreshValid) {
			context.changeWindow(WindowName.MENU);
		}
	}
	
	protected void disconnect() {
		context.disconnect();
	}
}