package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Spinner;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Product;
import util.DatabaseConnect;

public class HomeAdmin extends Application {

	private final DatabaseConnect db = DatabaseConnect.getInstance();
	Connection conn = db.getConnection();

	public HomeAdmin(Stage s) {
		initialize();
		setOnAction();
		setItemEvent();
		this.s = s;
		s.setScene(homeAdmScene);
		s.setTitle("Home");
		s.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		setOnAction();
		setItemEvent();
		primaryStage.setScene(homeAdmScene);
		primaryStage.setTitle("Home Admin");
		primaryStage.show();
	}

	private void setOnAction() {
		manageProductsItem.setOnAction(e -> {
			new EditProductScene(s);
		});


		logOut.setOnAction(e -> {
			new Main(s);
		});

		addToCartButton.setOnAction(e -> {
			 
		});

	}

	Stage s;
	Scene homeAdmScene;
	BorderPane bp;
	GridPane gp;
	GridPane qtyPane;
	VBox mainVB;
	Label titleLabel, welcomeLabel, selectLabel, productNameLabel, productDescriptionLabel, productPriceLabel, qtyLabel;
	Button addToCartButton;
	MenuBar menuBar;
	Menu home, manageProducts, account;
	MenuItem homePage, manageProductsItem, logOut;
	HBox qtyHB;
	Spinner<Integer> qtySpinner;

	String selectedProduct = null;
	ListView<String> productListView;
	VBox productDescVB;

	private void initialize() {
		bp = new BorderPane();

		titleLabel = new Label("SeRuput Teh");
		titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		titleLabel.setPadding(new Insets(5));

		menuBar = new MenuBar();
		home = new Menu("Home");
		manageProducts = new Menu("Manage Products");
		account = new Menu("Account");
		homePage = new MenuItem("Home Page");
		manageProductsItem = new MenuItem("Manage Products");
		logOut = new MenuItem("Log Out");

		menuBar.getMenus().addAll(home, manageProducts, account);
		home.getItems().add(homePage);
		manageProducts.getItems().add(manageProductsItem);
		account.getItems().addAll(logOut);

		welcomeLabel = new Label("Welcome, " + Main.loggedUser.getName());
		welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		selectLabel = new Label("Select a product to view");
		
		productNameLabel = new Label();
		productDescriptionLabel = new Label();
		productDescriptionLabel.setWrapText(true);
		productPriceLabel = new Label();
		qtyLabel = new Label("Quantity : ");

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
		homeAdmScene = new Scene(bp, 800, 500);

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
				// TODO Auto-generated method stub
				if (newValue != null) {
					selectedProduct = newValue;
					showProductDetails(selectedProduct);
				}
			}

			private void showProductDetails(String newValue) {
				// TODO Auto-generated method stub
				qtySpinner = new Spinner<Integer>(1, 100, 1);
				
				HBox qtyHB = new HBox();
				qtyHB.getChildren().addAll(qtyLabel, qtySpinner);
				
				
				
				Product productDetails = getProductDetails(newValue);
				productNameLabel.setText("Product: " + productDetails.getNamaProduct());
				productNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
				productDescriptionLabel.setText("Description: " + productDetails.getDescProduct());
				productPriceLabel.setText("Price: " + productDetails.getHargaProduct());
				productDescVB.getChildren().clear();
				productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
				
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