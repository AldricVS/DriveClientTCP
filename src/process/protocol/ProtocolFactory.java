package process.protocol;

import data.Protocol;
import data.enums.ActionCodes;

/**
 * Factory class used to create specific protocols directly
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ProtocolFactory {

	/**
	 * Create protocol with connection action code
	 * @param userName the name of the user connecting
	 * @param password the password of the 
	 * @param isAdmin if the user tries to connect as admin
	 * @return the protocol corresponding to the query
	 */
	public static Protocol createConnectionProtocol(String userName, String password, boolean isAdmin) {
		Protocol protocol;
		if(isAdmin) {
			protocol = new Protocol(ActionCodes.CONNECTION_ADMIN);
		}else {
			protocol = new Protocol(ActionCodes.CONNECTION_NORMAL);
		}
		protocol.appendOption(userName);
		protocol.appendOption(password);
		return protocol;
	}

}
