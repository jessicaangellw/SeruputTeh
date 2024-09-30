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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableSelectionModel;
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

public class CartScene extends Application {

	private final DatabaseConnect db = DatabaseConnect.getInstance();
	Connection conn = db.getConnection();

	public CartScene(Stage s) {
		initialize();
		setOnAction();
		setItemEvent();
		this.s = s;
		s.setScene(cartScene);
		s.setTitle("Cart");
		s.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		setOnAction();
		setItemEvent();
		primaryStage.setScene(cartScene);
		primaryStage.setTitle("Cart");
		primaryStage.show();
	}

	private void setOnAction() {
		homePage.setOnAction(e -> {
			new HomeScene(s);
		});

		purchaseHistory.setOnAction(e -> {
			new TransactionScene(s);
		});

		logOut.setOnAction(e -> {
			new Main(s);
		});

		makePurchaseButton.setOnAction(e -> {
//			
		});

		removeCartButton.setOnAction(e -> {
			
		});

		updateCartButton.setOnAction(e -> {

		});
		
		cartView.setOnMouseClicked(e -> {
			
			productDescVB.getChildren().clear();
			
			HBox quantityHB = new HBox();
//			quantityHB.getChildren().addAll(qtyLabel, qtySpinner);
			
			HBox updateRemoveButton = new HBox();
			updateRemoveButton.getChildren().addAll(updateCartButton, removeCartButton);
			updateRemoveButton.setSpacing(5);
		
			
			productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
			productDescVB.getChildren().addAll(quantityHB);
			productDescVB.getChildren().addAll(qtyLabel);
			productDescVB.getChildren().addAll(updateRemoveButton);
			productDescVB.getChildren().addAll(qtySpinner);
			
		});

	}

	Stage s;
	Scene cartScene;
	BorderPane bp;
	GridPane gp;
	GridPane qtyPane;
	VBox mainVB;
	Label titleLabel, welcomeLabel, selectLabel, productNameLabel, productDescriptionLabel, productPriceLabel, qtyLabel,
			totalLbl, orderInfoLbl, usernameLbl, noHpLbl, alamatLbl;
	Button updateCartButton, removeCartButton, makePurchaseButton;
	MenuBar menuBar;
	Menu home, cart, account;
	MenuItem homePage, myCart, purchaseHistory, logOut;
	HBox qtyHB, quantityHB, buttonHB;
	Spinner<Integer> qtySpinner;

	String selectedProduct = null;
	String selectedID = null;
	ListView<String> cartView;
	VBox productDescVB, totalOrderInfoVB;

	ArrayList<Cart> arr_cart = new ArrayList<>();

	private User currentUser;


	private void initialize() {
		bp = new BorderPane();

		titleLabel = new Label(Main.loggedUser.getName() + "'s Cart");
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

		totalLbl = new Label("Total : ");
		orderInfoLbl = new Label("Order Information");
		orderInfoLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
		usernameLbl = new Label("Username : ");
		noHpLbl = new Label("Phone Number : ");
		alamatLbl = new Label("Address : ");

		makePurchaseButton = new Button("Make Purchase");

		updateCartButton = new Button("Update Cart");
		removeCartButton = new Button("Remove From Cart");
		makePurchaseButton = new Button("Make Purchase");

		welcomeLabel = new Label("Welcome, " + Main.loggedUser.getName());
		welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		selectLabel = new Label("Select a product to add and remove");

		productNameLabel = new Label();
		productDescriptionLabel = new Label();
		productDescriptionLabel.setWrapText(true);
		productPriceLabel = new Label();
		qtyLabel = new Label("Quantity : ");

		cartView = new ListView<>();
		cartView.setMinWidth(200);
		cartView.setMaxWidth(200);
		cartView.setMaxHeight(175);

		getData();
		getDataCart();

		productDescVB = new VBox();
		productDescVB.getChildren().addAll(welcomeLabel, selectLabel);

		VBox productDetailsVB = new VBox();
		productDetailsVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);

		VBox totalOrderInfoVB = new VBox();
		totalOrderInfoVB.getChildren().addAll(totalLbl, orderInfoLbl, usernameLbl, noHpLbl, alamatLbl, makePurchaseButton);
		totalOrderInfoVB.setSpacing(5);
		
		gp = new GridPane();
		gp.add(titleLabel, 0, 0);
		gp.add(cartView, 0, 1);
		gp.add(productDescVB, 1, 1);
		gp.add(totalOrderInfoVB, 0, 2);
	

		gp.setHgap(20);
		gp.setVgap(10);

		mainVB = new VBox();
		mainVB.getChildren().addAll(titleLabel, gp);
		mainVB.setSpacing(20);

		bp.setTop(menuBar);
		bp.setCenter(mainVB);
		bp.setPadding(new Insets(10));
		cartScene = new Scene(bp, 800, 500);

		
	}

	private void getData() {

		String query = "SELECT * FROM Product";
		ResultSet rs = db.execQuery(query);

		try {
			while (rs.next()) {
				String namaProduct = rs.getString("product_name");
				
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void getDataCart() {
		String idUser = Main.loggedUser.getIdUser();
		
		String query = "SELECT * FROM Cart c JOIN Product p "
				+ "ON c.productID = p.productID WHERE userID = ?";
		try	(PreparedStatement ps = conn.prepareStatement(query)){
			ps.setString(1,  idUser);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int qty = rs.getInt("quantity");
				String namaProduct = rs.getString("product_name");
				int hargaProduct = rs.getInt("product_price");
				int total = 0;
			
					total = hargaProduct*qty;
				
				 
				cartView.getItems().addAll(qty + "x " +  namaProduct  + " (Rp." + total + ")");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}


	private void setItemEvent() {
		cartView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					selectedProduct = newValue;
					showProductDetails(selectedProduct);
				}
			}

			private void showProductDetails(String newValue) {
				Product productDetails = getProductDetails(newValue);
				if(productDetails != null) {
					int productPrice = productDetails.getHargaProduct();
					
					qtySpinner = new Spinner<Integer>(1, 100, 1);

					qtySpinner.valueProperty().addListener((obs, oldValue, newValue2) -> {

						HBox qtyHB = new HBox();
						qtyHB.getChildren().addAll(qtyLabel, qtySpinner, totalLbl);
						qtyHB.setSpacing(5);

						HBox buttonHB = new HBox();
						buttonHB.getChildren().addAll(updateCartButton, removeCartButton);
						buttonHB.setSpacing(5);

						int total = productPrice*qtySpinner.getValue();
						totalLbl.setText("Total Price : " + total);
						
						productDescVB.getChildren().clear();
						productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
						productDescVB.getChildren().add(qtyHB);
						productDescVB.getChildren().add(buttonHB);

						updateCartButton.setOnAction(e -> {
							String idProduct = productDetails.getIdProduct();
							String idUser = Main.loggedUser.getIdUser();
							int qty = qtySpinner.getValue();

							String query = "INSERT INTO cart (idProduct, idUser, qty) VALUES (?, ?, ?)";

							PreparedStatement ps = db.prepareStatement(query);

							try {
								ps.setString(1, idProduct);
								ps.setString(2, idUser);
								ps.setInt(3, qty);

								ps.execute();
							} catch (Exception e2) {

							}

							getData();
						});

						removeCartButton.setOnMouseClicked(event -> {
							String query = String.format("DELETE FROM cart WHERE productId = %s", selectedID);
						});

					});

					HBox qtyHB = new HBox();
					qtyHB.getChildren().addAll(qtyLabel, qtySpinner);

					HBox buttonHB = new HBox();
					buttonHB.getChildren().addAll(updateCartButton, removeCartButton);
					buttonHB.setSpacing(5);

					productNameLabel.setText("Product: " + productDetails.getNamaProduct());
					productDescriptionLabel.setText("Description: " + productDetails.getDescProduct());
					productPriceLabel.setText("Price: " + productDetails.getHargaProduct());
				
					productDescVB.getChildren().clear();
					productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
					productDescVB.getChildren().add(qtyHB);
					productDescVB.getChildren().add(buttonHB);
					productDescVB.setSpacing(10);
					
			
				}
			
			
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