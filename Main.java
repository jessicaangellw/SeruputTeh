package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import database.Database;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Product;
import model.User;
import util.DatabaseConnect;

public class Main extends Application implements EventHandler<ActionEvent> {
	public static Stage primaryStage;

	public static User loggedUser;

	public Main() {

	}

	public Main(Stage s) {
		this.s = s;
		initialize();
		setMouseAction();
		s.setScene(scene);
		s.setTitle("Login");
		s.show();
	
	}

	public static void main(String[] args) {
		launch(args);
	}

	Scene scene;
	public static BorderPane bp;
	GridPane gp;
	VBox vb;
	Label label_login, label_username, label_password, label_description;
	TextField email, tf_username;
	PasswordField pf_password;
	Button button_login;
	Hyperlink hyperlink_register;
	Stage s;

	private final DatabaseConnect db = DatabaseConnect.getInstance();
	Connection conn = db.getConnection();

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.s = primaryStage;
		initialize();
		setMouseAction();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.show();
		Main.primaryStage = primaryStage;
	}

	private void setMouseAction() {
		hyperlink_register.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

			}

		});

		hyperlink_register.setOnMouseClicked(event -> {
			new RegisterPage(s);
		});

		button_login.setOnAction(this);
		button_login.setOnAction(e -> {
			System.out.println("Click this");
			String username = tf_username.getText();
			String password = pf_password.getText();
			String query = "SELECT * FROM user wHERE username = ? and password = ?";
			PreparedStatement preparedStatement;
			Boolean safeToLogin = false;
			try {
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				try (ResultSet rs = preparedStatement.executeQuery()) {
					System.out.println(rs.getStatement());
					while (rs.next()) {
						String idUser = rs.getString("userID");
						String namaUser = rs.getString("username");
						String pass = rs.getString("password");
						String roleUser = rs.getString("role");
						String alamat = rs.getString("address");
						String noHp = rs.getString("phone_num");
						String genderUser = rs.getString("gender");
						String emailUser = rs.getString("email");

						loggedUser = new User(idUser, namaUser, emailUser, pass, roleUser, alamat, noHp, genderUser);
						safeToLogin = true;

					}
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			if (!safeToLogin) {
				Alert alert = new Alert(AlertType.ERROR, "Invalid credential", ButtonType.OK);
				alert.show();
				alert.setTitle("Error");
				alert.setHeaderText("Failed to Login");
			}

			if (username.isEmpty() || password.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "All fields must be filled", ButtonType.OK);
				alert.show();
				alert.setTitle("Error");
				alert.setHeaderText("Failed to Login");
			}

			System.out.println(username);
			System.out.println(password);
			System.out.println(safeToLogin);

			if (safeToLogin == true) {
				if (username.equals("admin") || username.equals("admin2")) {
					new HomeAdmin(s);
				} else {
					new HomeScene(s);
				}

			}
		});

	}

//	@Override
//	public void handle(ActionEvent event) {
//		
//	if(event.getSource() == button_login) {
////			Alert validasi = UserController.login(email.getText(), pf_password.getText());
////			validasi.show();	
//		
//	}
//
//	}

	private void initialize() {

		bp = new BorderPane();

		label_login = new Label("Login");
		label_login.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		label_login.setPadding(new Insets(5));
		label_login.setAlignment(Pos.CENTER);

		label_username = new Label("Username : ");
		label_password = new Label("Password :");
		hyperlink_register = new Hyperlink("register here");
		label_description = new Label("Don't have an account yet?");
		tf_username = new TextField();
		tf_username.setPromptText("Input username");
		pf_password = new PasswordField();
		pf_password.setPromptText("Input password");
		button_login = new Button("Login");
		button_login.setMinSize(200, 30);

		HBox registerHB = new HBox();
		registerHB.getChildren().addAll(label_description, hyperlink_register);
		registerHB.setSpacing(5);

		gp = new GridPane();
		gp.add(label_username, 0, 0);
		gp.add(tf_username, 1, 0);
		gp.add(label_password, 0, 1);
		gp.add(pf_password, 1, 1);
		gp.add(registerHB, 1, 2);
		gp.add(button_login, 1, 3);
		gp.setVgap(10);
		gp.setHgap(5);
		gp.setAlignment(Pos.CENTER);

		vb = new VBox();
		vb.getChildren().addAll(label_login, gp);
		vb.setSpacing(5);
		vb.setAlignment(Pos.CENTER);

		bp.setCenter(vb);
		bp.setPadding(new Insets(24));

		scene = new Scene(bp, 800, 500);
		

	}

	public Scene getScene() {
		// TODO Auto-generated method stub
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}