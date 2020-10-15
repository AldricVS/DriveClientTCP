/**
 * 
 */
package process.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class will handle all things related to the connection with the server
 * @author Aldric
 */
public class ServerConnectionHandler {

	/**
	 * The socket where client and server will send data to each other
	 */
	private Socket socket;
	
	/**
	 * All the messages that client want to send to server will be sended with this.
	 */
	private PrintWriter outputFlow;
	
	/**
	 * All data comming from server will be comming from this buffered reader
	 */
	private BufferedReader inputFlow;
	
	/**
	 * Connect to a server with specified port and ip adress
	 * @param ipAdress the Ip adress of the server
	 * @param port the port where to communicate with it
	 * @throws IOException if an error raises when creating socket
	 * @throws UnknownHostException if can't connect to server
	 */
	public ServerConnectionHandler(String ipAdress, int port) throws UnknownHostException, IOException{
		socket = new Socket(ipAdress, port);
		inputFlow = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outputFlow = new PrintWriter(socket.getOutputStream(), true);
	}

}
