package web_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import constants.WebServerInfo;


public class WebServer implements IWebServer {
	private Connection connection = null;

	public WebServer() {
		try {

			Class.forName("com.mysql.jdbc.Driver").newInstance();

			Properties p = new Properties();
			p.put("user", WebServerInfo.username);
			p.put("password", WebServerInfo.password);

			connection = DriverManager.getConnection(WebServerInfo.url
					+ WebServerInfo.db_name, p);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Boolean logIn(String username, String password, String type) {
		boolean userExists = false;
		ResultSet rs;
		String status;

		System.out.println("Enter log in...");
		try {
			PreparedStatement statement = connection
					.prepareStatement(
							"SELECT * FROM Users "
									+ "WHERE username = ?  AND password = ? AND type = ?",
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_UPDATABLE);

			statement.setString(1, username);
			statement.setString(2, password);
			statement.setString(3, type);

			rs = statement.executeQuery();

			// verify if the user with the given characteristics exists

			while (rs.next()) {
				status = rs.getString(WebServerInfo.status);
				System.out.println("Type is " + rs.getString("type"));
				// update status if user is inactive
				if (status.equals(WebServerInfo.inactive)) {
					userExists = true;
					rs.updateString(WebServerInfo.status, WebServerInfo.active);
					rs.updateRow();
				} else {
					System.out.println("User " + username
							+ "is already logged in");
				}

				break;
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userExists;
	}

	private String userType(String username) {
		String type = null;
		ResultSet rs;

		try {

			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM Users "
							+ "WHERE username = ?");

			statement.setString(1, username);

			rs = statement.executeQuery();

			while (rs.next()) {
				type = rs.getString("type");
				break;
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return type;
	}

	private String tableName(String userType) {
		return (userType.equals("seller")) ? WebServerInfo.SELLERS_TABLE
				: WebServerInfo.BUYERS_TABLE;
	}

	@Override
	public String[] userServices(String username) {
		List<String> userServicesList = new ArrayList<String>();
		String serviceName, userType, table;
		ResultSet rs;

		try {
			// get table according to user type
			userType = userType(username);
			table = tableName(userType);
			
			System.out.println("user "+username+" has type "+userType+" and is in table "+table);

			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM " + table
							+ " WHERE username = ?");

			statement.setString(1, username);

			rs = statement.executeQuery();

			while (rs.next()) {
				serviceName = rs.getString("service");
				userServicesList.add(serviceName);
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Arrays.copyOf(userServicesList.toArray(), userServicesList.size(), String[].class);
	}

	@Override
	public String[] getUsersForService(String serviceName) {
		ResultSet rs, rsSearch;
		PreparedStatement statement, searchService;
		List<String> usersList = new ArrayList<String>();

		try {
			// find active sellers
			statement = connection.prepareStatement("SELECT * FROM Users "
					+ "WHERE type = ? AND status = ?");
			statement.setString(1, "seller");
			statement.setString(2, WebServerInfo.active);

			rs = statement.executeQuery();

			// find if the active seller provides a certain service
			while (rs.next()) {
				searchService = connection.prepareStatement("SELECT * "
						+ "FROM ServicesSellers "
						+ "WHERE username = ? AND service = ?");
				System.out.println("Selected user " + rs.getString("username"));
				searchService.setString(1, rs.getString("username"));
				searchService.setString(2, serviceName);

				rsSearch = searchService.executeQuery();

				// get the name of the user that provides the service
				if (rsSearch.next()) {
					usersList.add(rsSearch.getString("username"));
				}

				rsSearch.close();
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Arrays.copyOf(usersList.toArray(), usersList.size(), String[].class);
	}

	@Override
	public void logOut(String username) {
		ResultSet rs;

		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM Users " + "WHERE username = ?",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			statement.setString(1, username);

			rs = statement.executeQuery();

			while (rs.next()) {
				rs.updateString(WebServerInfo.status, WebServerInfo.inactive);
				rs.updateRow();

				break;
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void changeOfferStatus(String username, String status, String servicename) {
		ResultSet rs;

		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM ServicesBuyers " + "WHERE username = ?" +
					" AND service = ?",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

			statement.setString(1, username);
			statement.setString(2, servicename);

			rs = statement.executeQuery();

			while (rs.next()) {
				//update status for the services in the list
				rs.updateString("launch_offer", status);
				rs.updateRow();
			}

			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getPrice(String seller, String service) {
		String price = null;
		ResultSet rs;

		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT * FROM ServicesSellers WHERE username = ?" +
							" AND service = ?");

			statement.setString(1, seller);
			statement.setString(2, service);

			rs = statement.executeQuery();

			if (rs.next()) {
				price = rs.getString("cost");
			}

			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return price;
	}
}
