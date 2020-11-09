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
		
		for (int i = 1; i < max; i++) {
			String Elt = listProtocol.getOptionsElement(i);
			logger.info(Elt);
			String[] product = Elt.split(";", 5);
			BigDecimal promotion = (product[4] == "null") ? new BigDecimal(product[4]) : null;
			
			listProduct.add(new Product(product[1], (Integer.parseInt(product[2])), new BigDecimal(product[3]), promotion));
		}
		logger.info("== Fin de la liste des produits ==");
		return listProduct;
	}
}
