package process.protocol;

import data.Protocol;
import data.enums.ActionCodes;

public class ProtocolBuilder {
	private Protocol protocol;

	public ProtocolBuilder(ActionCodes actionCode) {
		protocol = new Protocol(actionCode);
	}
	
	
}
