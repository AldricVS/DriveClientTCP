package process.protocol;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import data.Product;
import data.Protocol;
import exceptions.InvalidProtocolException;
import logger.LoggerUtility;

/**
 * Used the same way as {@link process.protocol.ProtocolExtractor ProtocolExtractor} but the some method to extract as a list
 * @author Maxence
 */
public class ProtocolListExtractor extends ProtocolExtractor {
	private static Logger logger = LoggerUtility.getLogger(ProtocolListExtractor.class, LoggerUtility.LOG_PREFERENCE);

	public ProtocolListExtractor(String protocolString) throws InvalidProtocolException {
		super(protocolString);
	}
	
	public ArrayList<Product> extractProductList() throws InvalidProtocolException {
		Protocol listProtocol = getProtocol();
		
		ArrayList<Product> listProduct = new ArrayList<Product>();
		int max = listProtocol.getOptionsListSize();
		int taille = Integer.parseInt(listProtocol.getOptionsElement(0));
		
		//Note, on a max-1 car il y a aussi le nombre de produit transmit
		if (max-1 != taille) {
			throw new InvalidProtocolException("Le nombre de Produit recu ne correspond pas a celui transmit");
		}
		
		
		logger.info("== Listage des "+taille+" produits recus ==");
		try {
			for (int i = 1; i < max; i++) {
				String Elt = listProtocol.getOptionsElement(i);
				logger.info(Elt);
				String[] product = Elt.split(";", 5);
				BigDecimal promotion = (product[4].equals("null")) ? null : new BigDecimal(product[4]);
				
				listProduct.add(new Product(Integer.parseInt(product[0]), product[1], new BigDecimal(product[2]), Integer.parseInt(product[3]), promotion));
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		logger.info("== Fin de la liste des produits ==");
		return listProduct;
	}
}
