package process.protocol;

import org.apache.log4j.Logger;

import data.Protocol;
import data.enums.ActionCodes;
import logger.LoggerUtility;

/**
 * Factory class used to create specific protocols directly
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @author Maxence Hennekein
 */
public class ProtocolFactory {
	private static Logger logger = LoggerUtility.getLogger(ProtocolFactory.class, LoggerUtility.LOG_PREFERENCE);

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
	
	public static Protocol createAddProductProtocol(String name, String price, String quantity) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.ADD_NEW_PRODUCT);
		protocol.appendOptions(name, price, quantity);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createAddProductQuantityProtocol(String id, String quantity) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.ADD_PRODUCT_QUANTITY);
		protocol.appendOptions(id, quantity);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createRemoveProductQuantityProtocol(String id, String quantity) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.REMOVE_PRODUCT_QUANTITY);
		protocol.appendOptions(id, quantity);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createDeleteProductProtocol(String id) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.REMOVE_PRODUCT_DEFINITELY);
		protocol.appendOption(id);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createAcceptOrderProtocol(String id) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.VALIDATE_ORDER);
		protocol.appendOption(id);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createCancelOrderProtocol(String id) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.DELETE_ORDER);
		protocol.appendOption(id);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createGetListProductProtocol() {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.GET_PRODUCT_LIST);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createGetListOrderProtocol() {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.GET_ORDER_LIST);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createGetListEmployeeProtocol() {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.GET_EMPLOYEE_LIST);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createAddEmployeeProtocol(String employeeName, String password) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.ADD_EMPLOYEE);
		protocol.appendOption(employeeName);
		protocol.appendOption(password);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createRemoveEmployeeProtocol(String employeeName) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.REMOVE_EMPLOYEE);
		protocol.appendOption(employeeName);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createAddPromotionProtocol(String id, String price) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.APPLY_PROMOTION);
		protocol.appendOptions(id, price);
		logger.info(protocol.toString());
		return protocol;
	}
	
	public static Protocol createRemovePromotionProtocol(String id) {
		Protocol protocol;
		protocol = new Protocol(ActionCodes.REMOVE_PROMOTION);
		protocol.appendOption(id);
		logger.info(protocol.toString());
		return protocol;
	}
	
	
}
