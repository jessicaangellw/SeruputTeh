package model;

public class TransactionHeader {
	private String idTransaction;
	private String idUser;
	public TransactionHeader(String idTransaction, String idUser) {
		super();
		this.idTransaction = idTransaction;
		this.idUser = idUser;
	}
	public String getIdTransaction() {
		return idTransaction;
	}
	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	
}