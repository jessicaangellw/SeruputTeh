package model;

public class User {

	private String idUser;
	private String name;
	private String email;
	private String pass;
	private String role;
	private String alamat;
	private String noHp;
	private String jenisKelamin;
	
	public User(String idUser, String name, String email, String pass, String role, String alamat, String noHp, String jenisKelamin) {
		super();
		this.idUser = idUser;
		this.name = name;
		this.email = email;
		this.pass = pass;
		this.role = role;
		this.alamat = alamat;
		this.noHp = noHp;
		this.jenisKelamin = jenisKelamin;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	public String getNoHp() {
		return noHp;
	}
	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}
	public String getJenisKelamin() {
		return jenisKelamin;
	}
	public void setJenisKelamin(String jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
	
	
	
}