package data;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Class containing all describers of a product
 * 
 * @author Aldric
 * @author Maxence Hennekein
 */
public class Product {
	private int idProduct;
	private String name;
	private int quantity;
	/**
	 * BigDecimals are used in order to have maximum precision
	 */
	private BigDecimal price;
	private BigDecimal promotion;

	public Product(int idProduct, String name, int quantity, BigDecimal price, BigDecimal promotion) {
		this.idProduct = idProduct;
		this.name = name;
		this.quantity = quantity;
		this.price = price.round(new MathContext(1, RoundingMode.HALF_UP));
		if (promotion != null) {
			promotion = promotion.round(new MathContext(1, RoundingMode.HALF_UP));
		}
		this.promotion = promotion;
	}

	public Product(String name, BigDecimal price, int quantity) {
		this.name = name;
		this.quantity = quantity;
		this.price = price.round(new MathContext(1, RoundingMode.HALF_UP));
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
	
	public int getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
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
