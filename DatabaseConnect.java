package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Product;

public class DatabaseConnect {
	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String DATABASE = "seruputteh";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	private Connection connect;
	private Statement statement;

	public static DatabaseConnect db;
	public ResultSet resultSet;

	private DatabaseConnect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static DatabaseConnect getInstance() {
		if (db == null) {
			db = new DatabaseConnect();
		}
		return db;
	}

	public Connection getConnection() {
		return connect;
	}

	public ResultSet execQuery(String query) {
		try {
			if (connect == null || connect.isClosed()) {
				connect = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
			}

			PreparedStatement ps = connect.prepareStatement(query);
			return ps.executeQuery();

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

//		return resultSet;
		return null;
	}
	

	public void execUpdate(String query) {
		try {
	        if (connect == null || connect.isClosed()) {
	            connect = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
	        }

	        statement = connect.createStatement(); // Initialize the statement object
	        statement.executeUpdate(query);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public PreparedStatement prepareStatement(String query) {
		// TODO Auto-generated method stub
		return null;
	}



}