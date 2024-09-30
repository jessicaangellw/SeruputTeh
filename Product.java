
package model;

public class Product {

	private String idProduct;
	private String namaProduct;
	private int hargaProduct;
	private String descProduct;
	
	public Product(String idProduct, String namaProduct, int hargaProduct, String descProduct) {
		super();
		this.idProduct = idProduct;
		this.namaProduct = namaProduct;
		this.hargaProduct = hargaProduct;
		this.descProduct = descProduct;
	}

	public String getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(String idProduct) {
		this.idProduct = idProduct;
	}

	public String getNamaProduct() {
		return namaProduct;
	}

	public void setNamaProduct(String namaProduct) {
		this.namaProduct = namaProduct;
	}

	public int getHargaProduct() {
		return hargaProduct;
	}

	public void setHargaProduct(int hargaProduct) {
		this.hargaProduct = hargaProduct;
	}

	public String getDescProduct() {
		return descProduct;
	}

	public void setDescProduct(String descProduct) {
		this.descProduct = descProduct;
	}
	
	
}