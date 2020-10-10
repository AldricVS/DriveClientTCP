package process.helpers;

/**
 * Class used to create Strings needed to communicate with server.<p>
 * We use 3 differents characters for formatting data : 
 * {@code "<", ">" and ";"}. This mean that we cannot use those 3 chararacters in any of messages.
 * 
 * @author Aldric Vitali Silvestre
 */
public class ProtocolCreatorHelper {
	
	/**
	 * Create a String to ask server for a connexion
	 * @param login the login of the user
	 * @param password the password associated with the login
	 * @return a formatted String that can be send to the server
	 */
	public static String createConnectionString(String login, String password) {
		return "";
	}
	
}
