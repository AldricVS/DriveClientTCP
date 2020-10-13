package data;

import java.math.BigDecimal;

/**
 * Class containing all describers of a product
 * @author Aldric
 */
public class Product {
	private String name;
	private int quantity;
	private BigDecimal price;
	
	public Product(String name, int quantity, BigDecimal price) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", quantity=" + quantity + ", price=" + price + "]";
	}
}
