package main;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Cart;
import model.Product;
import model.User;
import util.DatabaseConnect;

public class HomeScene extends Application {

	private final DatabaseConnect db = DatabaseConnect.getInstance();
	Connection conn = db.getConnection();

	public HomeScene(Stage s) {
		initialize();
		setOnAction();
		setItemEvent();
		this.s = s;
		s.setScene(homeScene);
		s.setTitle("Home");
		s.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		setOnAction();
		setItemEvent();
		primaryStage.setScene(homeScene);
		primaryStage.setTitle("Home");
		primaryStage.show();
	}

	private void setOnAction() {
		myCart.setOnAction(e -> {
			new CartScene(s);
		});

		purchaseHistory.setOnAction(e -> {
			new TransactionScene(s);
		});

		logOut.setOnAction(e -> {
			new Main(s);
		});

	
		

		
	}

	Stage s;
	Scene homeScene;
	BorderPane bp;
	GridPane gp;
	GridPane qtyPane;
	VBox mainVB;
	Label titleLabel, welcomeLabel, selectLabel, productNameLabel, productDescriptionLabel, productPriceLabel, qtyLabel, totalLbl;
	Button addToCartButton;
	MenuBar menuBar;
	Menu home, cart, account;
	MenuItem homePage, myCart, purchaseHistory, logOut;
	HBox qtyHB;
	Spinner<Integer> qtySpinner;

	String selectedProduct = null;
	ListView<String> productListView;
	VBox productDescVB;
	
	ArrayList<Cart> arr_cart = new ArrayList<>();

	private void initialize() {
		bp = new BorderPane();

		titleLabel = new Label("SeRuput Teh");
		titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		titleLabel.setPadding(new Insets(5));

		menuBar = new MenuBar();
		home = new Menu("Home");
		cart = new Menu("Cart");
		account = new Menu("Account");
		homePage = new MenuItem("Home Page");
		myCart = new MenuItem("My Cart");
		purchaseHistory = new MenuItem("Purchase History");
		logOut = new MenuItem("Log Out");

		menuBar.getMenus().addAll(home, cart, account);
		home.getItems().add(homePage);
		cart.getItems().add(myCart);
		account.getItems().addAll(purchaseHistory, logOut);

		welcomeLabel = new Label("Welcome, " + Main.loggedUser.getName());
		welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		selectLabel = new Label("Select a product to view");
		
		productNameLabel = new Label();
		productNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		productDescriptionLabel = new Label();
		productDescriptionLabel.setWrapText(true);
		productPriceLabel = new Label();
		qtyLabel = new Label("Quantity : ");
		totalLbl = new Label ("Total: ");

		addToCartButton = new Button("Add to Cart");

		productListView = new ListView<>();
		productListView.setMinWidth(200);
		productListView.setMaxWidth(200);

		getData();

		productDescVB = new VBox();
		productDescVB.getChildren().addAll(welcomeLabel, selectLabel);

		VBox productDetailsVB = new VBox();
		productDetailsVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);

		gp = new GridPane();
		gp.add(titleLabel, 0, 0);
		gp.add(productListView, 0, 1);
		gp.add(productDescVB, 1, 1);
		gp.setHgap(20);
		gp.setVgap(10);
		

		mainVB = new VBox();
		mainVB.getChildren().addAll(titleLabel, gp);
		mainVB.setSpacing(20);

		bp.setTop(menuBar);
		bp.setCenter(mainVB);
		bp.setPadding(new Insets(10));
		homeScene = new Scene(bp, 800, 500);

	}
	

	

	private void getData() {

		String query = "SELECT * FROM Product";
		ResultSet rs = db.execQuery(query);

		try {
			while (rs.next()) {
				String namaProduct = rs.getString("product_name");
				productListView.getItems().add(namaProduct);
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void setItemEvent() {
		productListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					selectedProduct = newValue;
					showProductDetails(selectedProduct);
				}
			}

			private void showProductDetails(String newValue) {
				// TODO Auto-generated method stub
				qtySpinner = new Spinner<Integer>(1, 100, 1);
				
				qtySpinner.valueProperty().addListener((obs, oldValue, newValue2)-> {
					Product productDetails = getProductDetails(newValue);
					
					HBox qtyHB = new HBox();
					qtyHB.getChildren().addAll(qtyLabel, qtySpinner, totalLbl);
					qtyHB.setSpacing(5);
					
					int total = qtySpinner.getValue()* Integer.parseInt(productPriceLabel.getText().substring(7)); 
					totalLbl.setText("Total Price : " + total);
					productDescVB.getChildren().clear();
					productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
					productDescVB.getChildren().add(qtyHB);
					productDescVB.getChildren().add(addToCartButton);
					
					addToCartButton.setOnAction(e -> {
		
						 String idProduct = productDetails.getIdProduct();
						 String idUser = Main.loggedUser.getIdUser();
						 int qty = qtySpinner.getValue();
						 
						 String query = String.format("INSERT INTO Cart (productID, userID, quantity) VALUES"
						 		+ "('%s', '%s', %d)",idProduct, idUser, qty);
						 
						 Cart newCart = new Cart(idProduct, idUser, qty);
						 arr_cart.add(newCart);
						 
						 db.execUpdate(query);
						 System.out.println(query);
						 
						 Alert alert = new Alert(AlertType.INFORMATION, " ", ButtonType.OK);
						 alert.show();
						 alert.setHeaderText("Added to Cart");
					});

					
				});
				
				
				HBox qtyHB = new HBox();
				qtyHB.getChildren().addAll(qtyLabel, qtySpinner);
				
				Product productDetails = getProductDetails(newValue);
				productNameLabel.setText("Product: " + productDetails.getNamaProduct());
				productDescriptionLabel.setText("Description: " + productDetails.getDescProduct());
				productPriceLabel.setText("Price: " + productDetails.getHargaProduct());
				productDescVB.getChildren().clear();
				productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
				productDescVB.getChildren().add(qtyHB);
				productDescVB.getChildren().add(addToCartButton);
				productDescVB.setSpacing(10);
			}
			
			
			
			private Product getProductDetails(String selectedProduct) {
				String query = "SELECT * FROM Product wHERE product_name = ?";
				PreparedStatement preparedStatement;
				try {
					preparedStatement = conn.prepareStatement(query);
					Product prod;
					try {
						preparedStatement.setString(1, selectedProduct);

						try (ResultSet rs = preparedStatement.executeQuery()) {
							while (rs.next()) {
								String idProduct = rs.getString("productId");
								String namaProduct = rs.getString("product_name");
								int hargaProduct = rs.getInt("product_price");
								String descProduct = rs.getString("product_des");
								prod = new Product(idProduct, namaProduct, hargaProduct, descProduct);
								return prod;
							}
							rs.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				return null;
			}

		});
	}

}