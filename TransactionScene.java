package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import util.DatabaseConnect;
import model.Cart;
import model.TransactionDetail;
import model.TransactionHeader;
import model.User;

public class TransactionScene extends Application implements EventHandler {

	private final DatabaseConnect db = DatabaseConnect.getInstance();
	Connection conn = db.getConnection();
	
	private int selectedID = -1;

	Stage s;
	Scene transactionScene;
	GridPane gp;
	BorderPane bp;
	Label titleLbl, purHisLbl, statusLbl, selectLbl, saranLbl, usernameLbl, phoneNumberLbl, addressLbl,
			welcomeMessageLbl, transactionIDLbl, priceLbl, noHistoryLabel, considerLbl;
	ListView<String> transactionIDView, productView;
	MenuBar menuBar;
	Menu home, cart, account;
	MenuItem homePage, myCart, purchaseHistory, logOut;
	VBox mainVB, transactionVBox;
	String selectedProduct = null;
	
	ArrayList<TransactionDetail> arr_transD = new ArrayList<>();
	ArrayList<TransactionHeader> arr_transH = new ArrayList<>();
	
	public TransactionScene(Stage s) {
		initialize();
		setOnAction();
		setItemEvent();
		s.setScene(transactionScene);
		s.setTitle("History");
		s.show();
	}

	private void setOnAction() {
		myCart.setOnAction(e -> {
			new CartScene(s);
		});

		homePage.setOnAction(e -> {
			new HomeScene(s);
		});

		logOut.setOnAction(e -> {
			new Main(s);
		});

	
		

		
	}

	
	private void setItemEvent() {
		
		transactionIDView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue != null) {
					selectedProduct = newValue;
					showTransactionDetail(selectedProduct);
					getDataCart();
				} 
			}
			
			private void getDataCart() {
				String idUser = Main.loggedUser.getIdUser();
				
				String query = "SELECT * FROM Cart c JOIN Product p "
						+ "ON c.productID = p.productID WHERE userID = ?";
				try	(PreparedStatement ps = conn.prepareStatement(query)){
					ps.setString(1,  idUser);
					ResultSet rs = ps.executeQuery();
					int totalTransaction = 0;
					while(rs.next()) {
						int qty = rs.getInt("quantity");
						String namaProduct = rs.getString("product_name");
						int hargaProduct = rs.getInt("product_price");
						int total = 0;
					
							total = hargaProduct*qty;
							totalTransaction += total;
						arr_transD.add(new TransactionDetail(namaProduct, total, qty)); 
						productView.getItems().addAll(qty + "x " +  namaProduct  + " (Rp." + total + ")");

					}
					priceLbl.setText("Total : Rp." + totalTransaction);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
			
			private void showTransactionDetail (String newValue) {
				
				TransactionHeader transactionDetail = getTransactionDetail(newValue);
				transactionIDLbl.setText("TransactionID : " + transactionDetail.getIdTransaction());
				transactionIDLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
				usernameLbl.setText("Username: " + Main.loggedUser.getName());
				usernameLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 13));
				phoneNumberLbl.setText("Phone Number : " + Main.loggedUser.getNoHp());
				addressLbl.setText("Address : " + Main.loggedUser.getAlamat());
				transactionVBox.getChildren().clear();
				transactionVBox.getChildren().addAll(transactionIDLbl, usernameLbl, phoneNumberLbl, addressLbl, priceLbl);
				transactionVBox.getChildren().add(productView);
				gp.add(transactionVBox, 1, 1);
			}
			
			
			private TransactionHeader getTransactionDetail (String selectedTransaction) {
				String query = "SELECT * FROM transaction_header join user on "
						+ "transaction_header.userID = user.userID join transaction_detail on "
						+ "transaction_header.transactionID = transaction_detail.transactionID "
						+ "WHERE transaction_header.transactionID = ?";
				PreparedStatement ps;
				try {
					ps = conn.prepareStatement(query);
					TransactionHeader transH;
					try {
						ps.setString(1, selectedTransaction);
						
						try (ResultSet rs = ps.executeQuery())  {
							while (rs.next()) {
								String idUser = Main.loggedUser.getIdUser();
								String idTransaction = rs.getString("transactionId");
								String namaUser = rs.getString("username");
								String noHP = rs.getString("phone_num");
								String alamat = rs.getString("address");
							//total
								transH = new TransactionHeader(idTransaction, idUser);
								return transH;
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

	

	private void purchaseHistory() {
		if (transactionIDView.getSelectionModel().isEmpty())
			return;

		MultipleSelectionModel<String> selectModel = transactionIDView.getSelectionModel();

		selectModel.setSelectionMode(SelectionMode.SINGLE);
		String selectedTransaction = selectModel.getSelectedItem();
	
		
	}

	private void initialize() {
		bp = new BorderPane();
		
		titleLbl = new Label(Main.loggedUser.getName() + "'s Purchase History");
		titleLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
		titleLbl.setPadding(new Insets(5));

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

		statusLbl = new Label ("Select a Transaction to view Details");
		saranLbl = new Label("Consider Purchasing Our Products");
		
		transactionVBox = new VBox();
		transactionIDLbl = new Label("Transaction ID: ");
		usernameLbl = new Label("Usename: ");
		phoneNumberLbl = new Label("Phone Number: ");
		addressLbl = new Label("Address");
		priceLbl = new Label ("Total: ");
		transactionVBox.getChildren().addAll(transactionIDLbl, usernameLbl, phoneNumberLbl, addressLbl, priceLbl);

		transactionIDView = new ListView<>();
		productView = new ListView<>();
		
		VBox transactionStatusVB = new VBox();
		purHisLbl = new Label("test");
		transactionStatusVB.getChildren().addAll(purHisLbl);

		gp = new GridPane();
		gp.add(titleLbl, 0, 0);
		gp.add(transactionIDView, 0, 1);
		gp.setHgap(20);
		gp.setVgap(10);

		mainVB = new VBox();
		mainVB.getChildren().add(gp);
		
		getData();
		
		bp.setTop(menuBar);
		bp.setCenter(mainVB);
		bp.setPadding(new Insets(10));
		transactionScene = new Scene(bp, 800, 500);
	}
	

	private void getData() {
		String query = "SELECT * FROM transaction_header join user on transaction_header.userID = user.userID";
		ResultSet rs = db.execQuery(query);
		
		try {
			if(!rs.isBeforeFirst()) {
				noHistoryLabel = new Label ("There's No History");
				noHistoryLabel.setFont(Font.font("Verdana",  FontWeight.BOLD, 12));
				considerLbl = new Label("Consider Purchasing Our Products");
				VBox noTransaction = new VBox();
				noTransaction.getChildren().addAll(noHistoryLabel, considerLbl);
				gp.add(noTransaction, 1, 1);
			}
			
			else {
				while (rs.next()) {
					String idTransaction = rs.getString("transactionID");
					transactionIDView.getItems().add(idTransaction);
				}
			}
			
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void handle(Event event) {
		 if (event.getSource() == purchaseHistory) {
	            purchaseHistory();
	        }
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		setOnAction();
		setItemEvent();
		primaryStage.setScene(transactionScene);
		primaryStage.setTitle("History");
		primaryStage.show();
		
	}

}