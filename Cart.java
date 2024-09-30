package model;

public class Cart {
	private String idProduct;
	private String idUser;
	private int qty;
	
	public Cart(String idProduct, String idUser, int qty) {
		super();
		this.idProduct = idProduct;
		this.idUser = idUser;
		this.qty = qty;
	}
	public String getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
	
	
	
	
}
