package process.protocol;

import data.enums.ActionCodes;
import data.protocol.Protocol;

public class ProtocolBuilder {
	private Protocol protocol;

	public ProtocolBuilder(ActionCodes actionCode) {
		protocol = new Protocol(actionCode);
	}
	
	
}
