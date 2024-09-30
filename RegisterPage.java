package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;
import util.DatabaseConnect;

public class RegisterPage implements EventHandler {
	private Stage s;
	Scene registerScene;
	GridPane gp;
	VBox mainVB;
	BorderPane mainBP;
	Label registerLabel, usernameLabel, emailLabel, passwordLabel, confirmPassLabel, phoneNumbLabel, addressLabel,
			genderLabel, descriptionLabel;
	TextField usernameTF, emailTF, phoneNumbTF, addressTF;
	PasswordField passwordPF, confirmPassPF;
	RadioButton maleRB, femaleRB;
	TextArea addressTA;
	CheckBox agree;
	Button registerButton;
	Hyperlink loginHyperlink;
	HBox questionHB, genderRB;
	ArrayList<User> arr_user = new ArrayList<>();

	private final DatabaseConnect db = DatabaseConnect.getInstance();

	private int selectedID = -1;

	private void initialize() {
		registerLabel = new Label("Register");
		registerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		usernameLabel = new Label("Username");
		emailLabel = new Label("Email");
		passwordLabel = new Label("Password");
		confirmPassLabel = new Label("Confirm password: ");
		phoneNumbLabel = new Label("Phone number");
		addressLabel = new Label("Address");
		genderLabel = new Label("Gender");
		descriptionLabel = new Label("Have an account?");
		loginHyperlink = new Hyperlink("login here");
		usernameTF = new TextField();
		usernameTF.setPromptText("Input username");
		emailTF = new TextField();
		emailTF.setPromptText("Input email");
		addressTF = new TextField();
		addressTF.setPromptText("Input address");
		phoneNumbTF = new TextField();
		phoneNumbTF.setPromptText("Input phone number");
		passwordPF = new PasswordField();
		passwordPF.setPromptText("Input password");
		confirmPassPF = new PasswordField();
		confirmPassPF.setPromptText("Input confirm password");
		maleRB = new RadioButton("Male");
		femaleRB = new RadioButton("Female");

		HBox genderRB = new HBox();
		genderRB.getChildren().addAll(maleRB, femaleRB);
		genderRB.setSpacing(4);

		ToggleGroup tgGender = new ToggleGroup();
		tgGender.getToggles().addAll(maleRB, femaleRB);

		addressTA = new TextArea();
		addressTA.setPromptText("Input address");
		agree = new CheckBox("i agree to all terms and condition");
		registerButton = new Button("Register");

		HBox questionHB = new HBox();
		questionHB.getChildren().addAll(descriptionLabel, loginHyperlink);

		gp = new GridPane();
		gp.add(registerLabel, 1, 0);
		gp.add(usernameLabel, 0, 1);
		gp.add(usernameTF, 1, 1);
		gp.add(emailLabel, 0, 2);
		gp.add(emailTF, 1, 2);
		gp.add(passwordLabel, 0, 3);
		gp.add(passwordPF, 1, 3);
		gp.add(confirmPassLabel, 0, 4);
		gp.add(confirmPassPF, 1, 4);
		gp.add(phoneNumbLabel, 0, 5);
		gp.add(phoneNumbTF, 1, 5);
		gp.add(addressLabel, 0, 6);
		gp.add(addressTA, 1, 6);
		gp.add(genderLabel, 0, 7);
		gp.add(genderRB, 1, 7);
		gp.add(agree, 1, 8);
		gp.add(questionHB, 1, 9);
		gp.add(registerButton, 1, 10);
		gp.setVgap(5);
		gp.setHgap(10);
		gp.setAlignment(Pos.CENTER);

		VBox mainVB = new VBox();
		mainVB.getChildren().add(gp);
		mainVB.setSpacing(4);
		mainVB.setAlignment(Pos.CENTER);

//		mainBP.setAlignment(mainBP, Pos.CENTER);
		mainBP = new BorderPane();
		mainBP.setCenter(mainVB);

		registerScene = new Scene(mainBP, 800, 500);
		
		getDataCustomerIndex();
	}

	public RegisterPage(Stage primaryStage) {
		this.setS(primaryStage);
		initialize();
		setMouseAction();
//		eventHandler();

		primaryStage.setScene(registerScene);
		primaryStage.setTitle("Register Page");
		primaryStage.show();
	}

	private int customerIndex;
	private void getDataCustomerIndex() {
		String query = "SELECT * FROM User";
		ResultSet resultSet = db.execQuery(query);
		customerIndex = 0;
		try {
			while (resultSet.next()) {
				customerIndex++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	private void setMouseAction() {
		loginHyperlink.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

			}

		});

		loginHyperlink.setOnMouseClicked(event -> {
			new Main(getS());
		});
		
		
	
		registerButton.setOnMouseClicked(event->{
			boolean checkValidasi = true;
			if (usernameTF.getText().isEmpty() || emailTF.getText().isEmpty() || phoneNumbTF.getText().isEmpty()||addressTA.getText().isEmpty() || passwordPF.getText().isEmpty()) {
				Alert validasi = new Alert(AlertType.ERROR);
				validasi = new Alert(AlertType.ERROR, "All fields must be filled");
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
				return;
				}
//			 else {	
			customerIndex++;
			String idUser = String.format("CU%03d",  customerIndex);
				
			String name = usernameTF.getText();
			if(usernameTF.getText().length()<= 5 || usernameTF.getText().length()>20 ) {
				Alert validasi = new Alert(AlertType.ERROR, "Username must be 5-20 characters", ButtonType.OK);
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}
			if(checkUsername(usernameTF.getText())) {
				Alert validasi = new Alert(AlertType.ERROR, "Username already exist, please choose another username",  ButtonType.OK);
				validasi.show();
				checkValidasi = false;
			}
			String email = emailTF.getText();
			if(!emailTF.getText().endsWith("@gmail.com")) {
				Alert validasi = new Alert(AlertType.ERROR, "Email must end with '@gmail.com'", ButtonType.OK);
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}
			String noHp = phoneNumbTF.getText();
			if(!phoneNumbTF.getText().startsWith("+62")) {
				Alert validasi = new Alert(AlertType.ERROR, "Phone Number must start with '+62'");
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}
			
			String alamat = addressTA.getText();
			if(addressTA.getText().isEmpty()) {
				Alert validasi = new Alert (AlertType.ERROR, "Address must be filled in", ButtonType.OK);
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}
		

			String pass = passwordPF.getText();
			if (passwordPF.getText().length()<5 || passwordPF.getText().isEmpty()) {
				Alert validasi = new Alert (AlertType.ERROR, "Password must be alphanumeric and the length must be at least 5 character", ButtonType.OK);
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}
			if(!confirmPassPF.getText().equals(passwordPF.getText())) {
				Alert validasi = new Alert (AlertType.ERROR, "Confirm Password must equals to password", ButtonType.OK);
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}
			
				
			String role = String.format("%s",  "User");
			String jenisKelamin = maleRB.isSelected() ? maleRB.getText() : femaleRB.getText();
			if (!maleRB.isSelected() && !femaleRB.isSelected()) {
				Alert validasi = new Alert(AlertType.ERROR, "Please choose your gender", ButtonType.OK);
				validasi.show();
				validasi.setHeaderText("Failed to Register");
				checkValidasi = false;
			}

			if (checkValidasi == true) {
				String query = String.format("INSERT INTO User(userID, username, email, password, role, address, phone_num, gender) VALUES ('%s', '%s','%s', '%s', '%s', '%s', '%s', '%s')",
						idUser, name, email, pass, role, alamat, noHp, jenisKelamin);
				
				User newUser = new User(idUser, name, email, pass, role, alamat, noHp, jenisKelamin);
				arr_user.add(newUser);
				
				db.execUpdate(query);
				System.out.println(query);
				
				Alert alert = new Alert(AlertType.INFORMATION, " ", ButtonType.OK);
				alert.show();
				alert.setTitle("Success");
				alert.setHeaderText("Registered Successfully!");
				
		
			}
			
//		}
		});
	}
	
	private boolean checkUsername(String username) {
		for (User user : arr_user) {
			if(user.getName().equals(username)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void handle(Event arg0) {
		// TODO Auto-generated method stub

	}

	public void setScene(Scene registerScene) {
		this.registerScene = registerScene;
	}

	public Scene getScene() {
		
		return registerScene;
	}

	public Stage getS() {
		return s;
	}

	public void setS(Stage s) {
		this.s = s;
	}

}