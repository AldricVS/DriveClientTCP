package gui.subwindows.employee_list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import gui.GuiConstants;
import gui.MainWindow;
import gui.WindowName;
import gui.subwindows.popup_window.addEmployeePanel;

/**
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence
 */
public class EmployeeListPanel extends JPanel {

private MainWindow context;
    
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
		//InitList se déclenche plutot quand on charge la page
		//initList();
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
	
	private void initList() {
		
			//On transforme notre liste de produit en affichage
		employeeListPanel = new JPanel();
		employeeListPanel.setLayout(new BoxLayout(employeeListPanel, BoxLayout.PAGE_AXIS));
		employeeListPanel.setPreferredSize(LIST_DIMENSION);
		
		listScrollPanel.setViewportView(employeeListPanel);
		listScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		listScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
			addEmployeePopup.getPopup();
			String employeeName = addEmployeePopup.getNameEmployee();
			String employeePassword = addEmployeePopup.getPassword();
			String employeeConfirmPassword = addEmployeePopup.getPasswordConfirm();
			//start protocol to add new employee
				context.changeWindow(WindowName.EMPLOYEE_LIST.name());
		}
	}
	
    class ActionRetour implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			context.changeWindow(WindowName.MENU.name());
		}
	}
}