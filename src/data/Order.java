package data;

import java.util.ArrayList;

public class Order {
	private int idOrder;
	private String userName;
	private int totalPrice;
	private String dateCommand;
	private ArrayList<Product> productList;
	
	public Order(int idOrder, String userName, int totalPrice) {
		this.idOrder = idOrder;
		this.userName = userName;
		this.totalPrice = totalPrice;
	}
}
