package exceptions;

public class ServerConnectionLostException extends Exception {
	
	public ServerConnectionLostException() {
		super();
	}

	public ServerConnectionLostException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public ServerConnectionLostException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ServerConnectionLostException(String arg0) {
		super(arg0);
	}

	public ServerConnectionLostException(Throwable arg0) {
		super(arg0);
	}
}
