package process.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import exceptions.ServerConnectionLostException;
import logger.LoggerUtility;
import process.protocol.ProtocolExtractor;
import process.protocol.ProtocolFactory;

/**
 * This class will handle all things related to the connection with the server
 * @author Aldric
 * @author Maxence Hennekein
 */
public class ServerConnectionHandler {
	private static Logger logger = LoggerUtility.getLogger(ServerConnectionHandler.class, LoggerUtility.LOG_PREFERENCE);
	private static ServerConnectionHandler instance = new ServerConnectionHandler();
	private final int TIMEOUT_DELAY = 15 * 1000;
	public static ServerConnectionHandler getInstance() {
		return instance;
	}
	
	private ServerConnectionHandler() {}
	
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
	 * Making sure that connection isn't started
	 */
	private boolean isConnected = false;
	
	/**
	 * Return the private isConnected boolean,
	 * to make sure if a connection has started or not
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	
	/**
	 * Connect to a server with specified port and ip adress
	 * @param ipAdress the Ip adress of the server
	 * @param port the port where to communicate with it
	 * @return 
	 * @throws IOException if an error raises when creating socket
	 * @throws UnknownHostException if can't connect to server
	 */
	public void initConnection(String ipAdress, int port) throws UnknownHostException, IOException{
		if (!isConnected) {
			logger.info(String.format("Attempting connection to server with IP adress %s and port %d", ipAdress, port));
			socket = new Socket(ipAdress, port);
			socket.setSoTimeout(TIMEOUT_DELAY);
			inputFlow = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputFlow = new PrintWriter(socket.getOutputStream(), true);
			logger.info("Successful server connection !");
			isConnected = true;
		}
		else {
			logger.error("A connection has already be made, close the existing one before starting another");
		}
	}
	
	/**
	 * Start a default connection
	 * <p>ip: local & port: 5000
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void initConnection() throws UnknownHostException, IOException{
		this.initConnection("127.0.0.1", 5000);
	}
	
	/**
	 * Close the connection with the server. <b>This method must be called before exiting app</b>. 
	 * @return If connection is closed properly or not
	 */
	private boolean closeConnection() {
		if (isConnected) {
			try {
				outputFlow.close();
				inputFlow.close();
				socket.close();
				isConnected = false;
				return true;
			}
			catch (IOException e) {
				logger.error("Cannot close connection properly : " + e.getMessage());
				return false;
			}
		}
		else {
			logger.error("Connection hasn't started yet");
		}
		return false;
	}
	
	/**
	 * Send a protocol to the server and wait for the answer.
	 * @param protcol the protocol containing data to process
	 * @return the string that server send back
	 * @throws IOException if cannot read what server answered
	 * @throws ServerConnectionLostException if the connection was lost with the Server
	 */
	public Protocol sendProtocolMessage(Protocol protcol) throws IOException, InvalidProtocolException, SocketException, SocketTimeoutException, ServerConnectionLostException {
		if (isConnected) {
			logger.info("Send message to server.");
			
			//create the string of the protocol
			String protocolString = protcol.toString();
			logger.info(protocolString);
			
			//send the message
			outputFlow.println(protocolString);
			
			//wait for recieving the server answer
			/*#readline throws IOException if cannot read from server*/ 
			//throw socketException when server shutdown
			try {
				String answer = inputFlow.readLine();
				logger.info(answer);
				if (answer.isEmpty()) {
					//TODO choisir une exception à jeter si le serveur envoie une réponse vide
					//throw new SocketException(msg);
					//throw new InvalidProtocolException(msg);
					//throw new ServerConnectionLostException(msg);
				}
				ProtocolExtractor extractor = new ProtocolExtractor(answer);
				return extractor.getProtocol();
			} catch (SocketTimeoutException ex) {
				throw new IOException("Délai d'attente dépassé");
			} catch (SocketException ex) {
				//throw SocketException when server connexion was closed without asking
				isConnected = false;
				throw new ServerConnectionLostException("Erreur lors de la connexion avec le Serveur, déconnexion...");
			}
		}
		else {
			logger.error("Connection hasn't started yet");
		}
		return ProtocolFactory.createErrorProtocol("Connection hasn't started yet");
	}
	
	/**
	 * Method in order to disconnect to server safely (we don't wait any answer here)
	 */
	public void disconnect() {
		if (isConnected) {
			logger.info("Send disconnect message to server.");
			Protocol protocolDisconnect = new Protocol(ActionCodes.DISCONNECT);
			//send the message
			outputFlow.println(protocolDisconnect.toString());
			
			closeConnection();
		}
		else {
			logger.error("Connection hasn't started yet");
		}
	}
	
}
