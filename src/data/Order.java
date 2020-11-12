package data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * 
 * @author Maxence
 */
public class Order {
	private int idOrder;
	private String dateCommandMade;
	private String dateCommandReceive;
	private int numberProduct;
	private String userName;
	private BigDecimal totalPrice;
	
	public Order(int idOrder, String dateCommandMade, String dateCommandReceive, int numberProduct, String userName, BigDecimal totalPrice) {
		this.idOrder = idOrder;
		this.dateCommandMade = dateCommandMade;
		this.dateCommandReceive = dateCommandReceive;
		this.numberProduct = numberProduct;
		this.userName = userName;
		this.totalPrice = totalPrice.round(new MathContext(8, RoundingMode.HALF_UP));
	}

	public String getDateCommandMade() {
		return dateCommandMade;
	}

	public void setDateCommandMade(String dateCommandMade) {
		this.dateCommandMade = dateCommandMade;
	}

	public String getDateCommandReceive() {
		return dateCommandReceive;
	}

	public void setDateCommandReceive(String dateCommandReceive) {
		this.dateCommandReceive = dateCommandReceive;
	}

	public int getIdOrder() {
		return idOrder;
	}

	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getNumberProduct() {
		return numberProduct;
	}

	public void setNumberProduct(int numberProduct) {
		this.numberProduct = numberProduct;
	}

	@Override
	public String toString() {
		return "Order [idOrder=" + idOrder + ", dateCommandMade=" + dateCommandMade + ", dateCommandReceive="
				+ dateCommandReceive + ", numberProduct=" + numberProduct + ", userName=" + userName + ", totalPrice="
				+ totalPrice + "]";
	}
	
	
}
