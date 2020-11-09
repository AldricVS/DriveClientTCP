package data;

import java.math.BigDecimal;

/**
 * Class containing all describers of a product
 * 
 * @author Aldric
 */
public class Product {
	private String name;
	private int quantity;
	/**
	 * BigDecimals are used in order to have maximum precision
	 */
	private BigDecimal price;
	private BigDecimal promotion;

	public Product(String name, int quantity, BigDecimal price, BigDecimal promotion) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.promotion = promotion;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getPromotion() {
		return promotion;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setPromotion(BigDecimal promotion) {
		this.promotion = promotion;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}
	
	public String toProtocol() {
		return name + ";" + quantity + ";" + price + ";" + promotion;
	}
}
