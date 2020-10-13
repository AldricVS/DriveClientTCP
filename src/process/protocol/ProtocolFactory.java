package process.protocol;

import data.enums.ActionCodes;
import data.protocol.Protocol;

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
			protocol = new Protocol(ActionCodes.CONNECTION);
		}else {
			// TODO changer ici (mettre un nouveau code pour connection en tant qu'admin)
			protocol = new Protocol(ActionCodes.CONNECTION);
		}
		protocol.appendOption(userName);
		protocol.appendOption(password);
		return protocol;
	}

}
