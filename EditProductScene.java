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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Product;
import util.DatabaseConnect;

public class EditProductScene extends Application {

	public static Stage primaryStage;

	private final DatabaseConnect db = DatabaseConnect.getInstance();
	Connection conn = db.getConnection();

	public EditProductScene(Stage s) {
		initialize();
		setOnAction();
		setItemEvent();
		this.s = s;
		s.setScene(transactionScene);
		s.setTitle("Products");
		s.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		setOnAction();
		setItemEvent();
		primaryStage.setScene(transactionScene);
		primaryStage.setTitle("Home");
		primaryStage.show();
	}

	private void setOnAction() {
		homePage.setOnAction(e -> {
			new HomeAdmin(s);
		});

		logOut.setOnAction(e -> {
			new Main(s);
		});

		addProductButton.setOnAction(e -> {
			productDescVB.getChildren().clear();

			HBox addBackButton = new HBox();
			addBackButton.getChildren().addAll(addProductButton, backButton);
			addBackButton.setSpacing(5);

//			productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
			productDescVB.getChildren().addAll(welcomeLabel, selectLabel);
			productDescVB.getChildren().addAll(inputProductNameLbl, inputProductNametf);
			productDescVB.getChildren().addAll(inputProductPriceLbl, inputProductPricetf);
			productDescVB.getChildren().addAll(inputProductDecLbl, inputProductDescta);
			productDescVB.getChildren().add(addBackButton);

			addProductButton.setOnAction(event -> {

				productIndex++;
				String idProduct = String.format("TE%03d", productIndex);
				String productName = inputProductNametf.getText();
				int productPrice = Integer.parseInt(inputProductPricetf.getText());
				String productDescription = inputProductDescta.getText();

				String query = String.format(
						"INSERT INTO Product(productID, product_name, product_price, product_des) "
								+ "VALUES ('%s', '%s', %d, '%s')",
						idProduct, productName, productPrice, productDescription);

				Product newProduct = new Product(idProduct, productName, productPrice, productDescription);
				arr_product.add(newProduct);

				db.execUpdate(query);
				System.out.println(query);

				Alert alert = new Alert(AlertType.INFORMATION, "Product successfully added!", ButtonType.OK);
				alert.show();
			});

			backButton.setOnAction(event -> {
				productDescVB.getChildren().clear();

				


			});

		});

		updateProductButton.setOnAction(e -> {
			productDescVB.getChildren().clear();

			HBox UpdateBackButton = new HBox();
			UpdateBackButton.getChildren().addAll(updateProductButton, backButton);
			UpdateBackButton.setSpacing(5);

			productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
			productDescVB.getChildren().add(updateProductLbl);
			productDescVB.getChildren().add(inputNewPricetf);
			productDescVB.getChildren().add(UpdateBackButton);

			updateProductButton.setOnAction(event -> {
//				String productName = inputProductNametf.getText();
				int productPrice = Integer.parseInt(inputNewPricetf.getText());
				try {
					String priceText = inputProductPricetf.getText();
					if (!priceText.isEmpty()) {
						productPrice = Integer.parseInt(priceText);
					}
				} catch (NumberFormatException e2) {
					e2.printStackTrace();
				}

				String query = String.format(
						"UPDATE Product SET product_price = %d WHERE productID = '%s'", productPrice, selectedID);
				db.execUpdate(query);

				getData();
			});

			backButton.setOnAction(event -> {
				
			});

		});

		removeProductButton.setOnAction(e -> {
			productDescVB.getChildren().clear();

			HBox RemoveBackButton = new HBox();
			RemoveBackButton.getChildren().addAll(removeProductButton, backButton);
			RemoveBackButton.setSpacing(5);

			productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
			productDescVB.getChildren().add(removeProductLbl);
			productDescVB.getChildren().add(RemoveBackButton);
			
			removeProductButton.setOnAction(event->{
				String query = String.format("DELETE FROM Product WHERE productID = '%s'", selectedID);
		
				
				db.execUpdate(query);
				getData();
			});
			
		});

	}

	private int productIndex;

	private void getDataProductIndex() {
		String query = "SELECT * FROM Product";
		ResultSet resultSet = db.execQuery(query);
		productIndex = 0;
		try {
			while (resultSet.next()) {
				productIndex++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Stage s;
	Scene transactionScene;
	BorderPane bp;
	GridPane gp;
	GridPane qtyPane;
	VBox mainVB;
	Label titleLabel, welcomeLabel, selectLabel, productNameLabel, productDescriptionLabel, productPriceLabel, qtyLabel,
			inputProductNameLbl, inputProductPriceLbl, inputProductDecLbl, updateProductLbl, removeProductLbl;
	Button addProductButton, updateProductButton, removeProductButton, backButton;
	MenuBar menuBar;
	Menu home, manageProducts, account;
	MenuItem homePage, manageProductsItem, logOut;
	HBox qtyHB, addBackButton;
	Spinner<Integer> qtySpinner;
	TextField inputProductNametf, inputProductPricetf, inputNewPricetf;
	TextArea inputProductDescta;

	ArrayList<Product> arr_product = new ArrayList<>();

	String selectedProduct = null;
	String selectedID = null;
	ListView<String> productListView;
	VBox productDescVB;

	private void initialize() {
		bp = new BorderPane();

		titleLabel = new Label("Manage Products");
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
		welcomeLabel = new Label("Welcome, " + Main.loggedUser.getName());
		welcomeLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		selectLabel = new Label("Select a product to update");

		productNameLabel = new Label();
		productDescriptionLabel = new Label();
		productDescriptionLabel.setWrapText(true);
		productPriceLabel = new Label();
		qtyLabel = new Label("Quantity : ");

		inputProductNameLbl = new Label("Input product name");
		inputProductNameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		inputProductPriceLbl = new Label("Input product price");
		inputProductPriceLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
		inputProductDecLbl = new Label("Input product description");
		inputProductDecLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 10));

		updateProductLbl = new Label("Update Product");
		removeProductLbl = new Label("Are you sure you want to remove this product?");
		removeProductLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));

		inputProductNametf = new TextField();
		inputProductNametf.setPromptText("Input product name");
		inputProductPricetf = new TextField();
		inputProductPricetf.setPromptText("Input product price");
		inputProductDescta = new TextArea();
		inputProductDescta.setPromptText("Input product description");
		inputNewPricetf = new TextField();
		inputNewPricetf.setPromptText("Input new price");

		updateProductButton = new Button("Update Product");
		removeProductButton = new Button("Remove Product");
		addProductButton = new Button("Add Product");
		backButton = new Button("Back");

		productListView = new ListView<>();
		productListView.setMinWidth(200);
		productListView.setMaxWidth(200);

		getData();

		productDescVB = new VBox();
		productDescVB.getChildren().addAll(welcomeLabel, selectLabel, addProductButton);
		productDescVB.setSpacing(10);

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
		transactionScene = new Scene(bp, 800, 500);

		getDataProductIndex();
	}

	private void getData() {
		productListView.getItems().clear();
		
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
				qtySpinner = new Spinner<Integer>(1, 100, 1);

				HBox qtyHB = new HBox();
				qtyHB.getChildren().addAll(qtyLabel, qtySpinner);

				HBox manageButton = new HBox();
				manageButton.getChildren().addAll(updateProductButton, removeProductButton);
				manageButton.setSpacing(10);

				Product productDetails = getProductDetails(newValue);
				productNameLabel.setText("Product: " + productDetails.getNamaProduct());
				productNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
				productDescriptionLabel.setText("Description: " + productDetails.getDescProduct());
				productPriceLabel.setText("Price: " + productDetails.getHargaProduct());
				productDescVB.getChildren().clear();
				productDescVB.getChildren().addAll(productNameLabel, productDescriptionLabel, productPriceLabel);
				productDescVB.getChildren().add(addProductButton);
				productDescVB.getChildren().add(manageButton);
				productDescVB.setSpacing(5);

				selectedID = productDetails.getIdProduct();

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