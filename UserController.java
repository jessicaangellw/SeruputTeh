package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import main.RegisterPage;
import model.User;

public class UserController {

	public static Alert register(String name, String email, String pass,
			String conPassword, String noHp, String alamat, String jenisKelamin, boolean tncc) {
	Alert validasi = new Alert(AlertType.INFORMATION);
	String nameV = validasiUsername(name);
	String passV = validasiPassword(pass);
	
	if (name.isEmpty() || name.length()< 5 || name.length() > 20 ) {
		validasi = new Alert (AlertType.ERROR, "Username length must be between 5-20 characters", ButtonType.OK);
	}
	else if(!email.contains("@gmail.com")) {
		validasi = new Alert (AlertType.ERROR, "Email must ends with '@gmail.com'");
	}
	else if (pass.length()<5 || pass.isEmpty()) {
		validasi = new Alert (AlertType.ERROR, "Password must be filled in and the length must be at least 5 character", ButtonType.OK);
	} 
	else if (conPassword != pass) {
		validasi = new Alert (AlertType.ERROR, "Confirm Password must equeals to password", ButtonType.OK);
	}
	else if (!noHp.startsWith("+62") || noHp.matches("+62*")) {
		validasi = new Alert (AlertType.ERROR, "Phone number must start with +62", ButtonType.OK);
	}
	else if(alamat.isEmpty()) {
		validasi = new Alert (AlertType.ERROR, "Address must be filled in", ButtonType.OK);
	}
	else if(jenisKelamin == null) {
		validasi = new Alert (AlertType.ERROR, "Gender must be choosed", ButtonType.OK);
	}
	else if (!tncc) {
		validasi = new Alert (AlertType.ERROR, "Terms and condition should be accpeted", ButtonType.OK);
	}
//	else {
//		RegisterPage rp = RegisterPage.chooseOne(name);
//		if (rp.getIdUser(-100)) {
//			validasi = new Alert(AlertType.ERROR, "User is already registered!", ButtonType.OK);	
//		} else {
//			User.refresh();
//			User.insert(new User (username, email, password, phoneNumber, address, gender, "user"));
//			validasi = new Alert(AlertType.INFORMATION, "Register success.Please login", ButtonType.OK);
//			validasi.show();
//			Scene.homes();
//		}
	return validasi;
	}

//	}

	
	public static Alert login (TextField tf_username, TextField email) {
		Alert validasi = new Alert(AlertType.INFORMATION);
//		if (tf_username.isEmpty() ) {
//			validasi = new Alert(AlertType.ERROR, "All fields must be filled", ButtonType.OK);
//		} else if (email.isEmpty() || email.equals("")) {
//			validasi = new Alert(AlertType.ERROR, "All fields must be filled", ButtonType.OK);
////		} else {
//			User user = User.chooseOne(name);
//			if (user == null) {
//				validasi = new Alert(AlertType.ERROR, "Invalid credential", ButtonType.OK);
//			} else if (user.getUserID()) {
//				validasi = new Alert(AlertType.ERROR, "Invalid credential", ButtonType.OK);
//			} else if (!user.getUserPassword().equals(pf_password)) {
//				validasi = new Alert(AlertType.ERROR, "Invalid credential", ButtonType.OK);
//			} else {
//				User.logged = user;
//				validasi = new Alert(AlertType.INFORMATION, String.format("Welcome, %s!", user.getUserUsername()), ButtonType.OK);
//				Scene.homes();
//			}
		return validasi;
		}

	
	
	
	private static String validasiPassword(String password) {
		if(password.isEmpty() || password.equals("")) return "Password must be fille in";
		if(password.length() < 5) return "Password must be at least 5 characters";

		boolean alpha = false;
		boolean numeric = false;
		for (Character chr : password.toCharArray()) {
			if (chr >= 'a' && chr <= 'z') alpha = true;
			if (chr >= 'A' && chr <= 'Z') alpha = true;
			if (chr >= '0' && chr <= '9') numeric = true;
			if (alpha && numeric) break; }
		if (!alpha || !numeric) return "Password must be alphanumeric";
		
		return "";
	}


	
	private static String validasiUsername(String username) {
		if (username.isEmpty()) return "Username must be filled in";
		if (username.length()<5 || username.length()>20) return "Username must be 5-20 character";
		return "";
	}
	
	
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
