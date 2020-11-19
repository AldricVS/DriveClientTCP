package process.protocol;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.Order;
import data.Product;
import data.Protocol;
import exceptions.InvalidProtocolException;
import gui.components.DialogHandler;
import logger.LoggerUtility;

/**
 * Helper class used to get lists of items from a protocol.
 * Those protocols are writen like :
 * {@code <ActionCode><NumberOfItems><ItemElt1;ItemElt2;...;ItemEltN><ItemElt1;ItemElt2;...;ItemEltN>...}
 * @author Maxence
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 */
public class ProtocolListExtractor{
	private static Logger logger = LoggerUtility.getLogger(ProtocolListExtractor.class, LoggerUtility.LOG_PREFERENCE);
	
	/*=======CONTSTANTES=======*/
	
	private static final String ERROR_PRODUCT_LIST = "La liste des produits n'est pas lisible";
	private static final String ERROR_ORDER_LIST = "La liste des commandes n'est pas lisible";
	
	private static final int NUMBER_ITEMS_PER_PRODUCT = 5;
	private static final int NUMBER_ITEMS_PER_ORDER = 6;
	
	Protocol listProtocol;

	public ProtocolListExtractor(Protocol protocol) throws InvalidProtocolException {
		this.listProtocol = protocol;
	}
	
	/**
	 * Extract all products from the protocol.
	 * @return a list containg all products from the protocol
	 * @throws InvalidProtocolException if product list is not well-formed (it can miss a sub item for instance, or the number)
	 */
	public ArrayList<Product> extractProductList() throws InvalidProtocolException {
		ArrayList<Product> listProduct = new ArrayList<Product>();
		int max = listProtocol.getOptionsListSize();
		int numberOfProducts = Integer.parseInt(listProtocol.getOptionsElement(0));
		
		//Note, on a max-1 car il y a aussi le nombre de produit transmis
		if (max-1 != numberOfProducts) {
			throw new InvalidProtocolException("Une erreur dans le compte des produits à été trouvée.");
		}
		
		logger.info("== Listage des "+numberOfProducts+" produits recus ==");
		String productString = "";
		try {
			for (int i = 1; i < max; i++) {
				productString = listProtocol.getOptionsElement(i);
				logger.info(productString);
				String[] productsArray = productString.split(";");
				//array size must be 5
				if(productsArray.length != NUMBER_ITEMS_PER_PRODUCT) {
					logger.error("Product list recieved is not valid : the string \"" + productString +"\" is not well-formed.");
					throw new InvalidProtocolException(ERROR_PRODUCT_LIST);
				}
				BigDecimal promotion = (productsArray[4].equals("null")) ? null : new BigDecimal(productsArray[4]);
				
				listProduct.add(new Product(Integer.parseInt(productsArray[0]), productsArray[1], new BigDecimal(productsArray[2]), Integer.parseInt(productsArray[3]), promotion));
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			//if we are here, it means that product list is not well formed, protocol is invalid
			logger.error("Product list recieved is not valid : the string \"" + productString +"\" is not well-formed.");
			throw new InvalidProtocolException(ERROR_PRODUCT_LIST);
		}catch (NumberFormatException e) {
			logger.error("A number cannot be readed in the line \"" + productString + "\"");
			throw new InvalidProtocolException(ERROR_PRODUCT_LIST);
		}
		logger.info("== Fin de la liste des produits ==");
		return listProduct;
	}
	
	/**
	 * Extract all orders recieved from the protocol.
	 * @return a list containg all orders from the protocol
	 * @throws InvalidProtocolException if product list is not well-formed (it can miss a sub item for instance, or the number)
	 */
	public ArrayList<Order> extractOrderList() throws InvalidProtocolException {
		ArrayList<Order> listOrder = new ArrayList<Order>();
		int max = listProtocol.getOptionsListSize();
		int numberOfOrders = Integer.parseInt(listProtocol.getOptionsElement(0));
		
		if (max-1 != numberOfOrders) {
			throw new InvalidProtocolException("Une erreur dans le compte des commandes à été trouvée.");
		}
		
		logger.info("== Listage des "+numberOfOrders+" commandes recus ==");
		String orderString = "";
		try {
			for (int i = 1; i < max; i++) {
				orderString = listProtocol.getOptionsElement(i);
				logger.info(orderString);
				String[] order = orderString.split(";");
				if(order.length != NUMBER_ITEMS_PER_ORDER) {
					logger.error("Order list recieved is not valid : the string \"" + orderString +"\" is not well-formed.");
					throw new InvalidProtocolException(ERROR_ORDER_LIST);
				}
				listOrder.add(new Order(Integer.parseInt(order[0]), order[1], order[2], Integer.parseInt(order[3]), order[4],new BigDecimal(order[5])));
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			logger.error("Order list recieved is not valid : the string \"" + orderString +"\" is not well-formed.");
			throw new InvalidProtocolException(ERROR_ORDER_LIST);
		}catch (NumberFormatException e) {
			logger.error("A number cannot be readed in the line \"" + orderString + "\"");
			throw new InvalidProtocolException(ERROR_PRODUCT_LIST);
		}
		logger.info("== Fin de la liste des commandes ==");
		return listOrder;
	}
}
