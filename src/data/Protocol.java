package data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import data.enums.ActionCodes;

/**
 * Protocol is a data class containing all data in order to create strings that will be send to the server.<p>
 * A protocol string is composed like this : <p>
 * {@code <Action code><option 1><option 2>...<option n>}<p>
 * The number of options will vary depending of the action code 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 *
 */
public class Protocol {
	/**
	 * The action code will be the first string in every protocol
	 */
	private ActionCodes actionCode;
	
	/**
	 * options are all strings that are added after actionCode in protocol
	 */
	private List<String> options = new LinkedList<>();
	
	public Protocol(ActionCodes actionCode) {
		this.actionCode = actionCode;
	}
	
	public Protocol(ActionCodes actionCode, List<String> args) {
		this.actionCode = actionCode;
		options = args;
	}
	
	public List<String> getOptionsList(){
		return options;
	}
	
	public int getOptionsListSize() {
		return options.size();
	}
	
	/**
	 * @param index the position of the string in the list
	 * @return the string at specified position or {@code null} if index is out of bounds
	 */
	public String getOptionsElement(int index) {
		return options.get(index);
	}

	public ActionCodes getActionCode() {
		return actionCode;
	}
	
	public void setActionCode(ActionCodes actionCode) {
		this.actionCode = actionCode;
	}
	
	/**
	 * Add an option in the protocol message.<p>
	 * @param optionString the option to add
	 */
	public void appendOption(String optionString) {
		options.add(optionString);
	}
	
	//TODO une m�thode avec les ... pour pouvoir rajouter autant de param�tre qu'on veut
	//ce sera de la forme d'un DO WHILE
	
	/**
	 * Add an option with two parameters using the formatism described.
	 * @param optionString two options String to add
	 */
	public void appendTwoOptions(String optionString1, String optionString2) {
		String productOption = optionString1 + ";" + optionString2;
		options.add(productOption);
	}
	
	/**
	 * Add an option with three parameters separated using a ';'.
	 * @param optionString three options String to add
	 */
	public void appendThreeOptions(String optionString1, String optionString2, String optionString3) {
		String productOption = optionString1 + ";" + optionString2 + ";" + optionString3;
		options.add(productOption);
	}
	
	/**
	 * Create the string containing all needed data, formatted respecting the protocol.
	 * @return the string to send
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		sb.append(actionCode.getCode());
		sb.append('>');
		for (String s : options) {
			sb.append('<');
			sb.append(s);
			sb.append('>');
		}
		return sb.toString();
	}
}