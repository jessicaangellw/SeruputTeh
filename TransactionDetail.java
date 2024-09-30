package model;

public class TransactionDetail {
	
	private String idTransaction;
	private int total;
	private int qty;
	
	public TransactionDetail(String idTransaction, int total, int qty) {
		super();
		this.idTransaction = idTransaction;
		this.total = total;
		this.qty = qty;
	}
	public String getIdTransaction() {
		return idTransaction;
	}
	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	
	
	
	
	
}