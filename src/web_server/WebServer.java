package web_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import constants.UserTypes;
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
	public boolean logIn(String username, String password, String type) {

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

	@Override
	public List<String> userServices(String username) {
		List<String> userServicesList = new ArrayList<String>();
		String serviceName;
		ResultSet rs;
		
		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM Services " + "WHERE username = ?");
			
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

		return userServicesList;
	}

	@Override
	public List<String> getUsersForService(String serviceName) {
		//TODO for each active user verify if it is interested on that service
		ResultSet rs, rsSearch;
		PreparedStatement statement, searchService;
		List<String> usersList = new ArrayList<String>();
		
		try {
			//find active sellers
			statement = connection.prepareStatement("SELECT * FROM Users " +
					"WHERE type = ? AND status = ?");
			statement.setString(1, UserTypes.seller);
			statement.setString(2, WebServerInfo.active);
			
			rs = statement.executeQuery();
			
			//find if the active seller provides a certain service
			while (rs.next()) {
				searchService = connection.prepareStatement("SELECT * " +
								"FROM Services " +
								"WHERE username = ? AND service = ?");
				System.out.println("Selected user " + rs.getString("username"));
				searchService.setString(1, rs.getString("username"));
				searchService.setString(2, serviceName);
				
				rsSearch = searchService.executeQuery();
				
				//get the name of the user that provides the service
				if (rsSearch.next()) {
					usersList.add(rsSearch.getString("username"));
				}
				
				rsSearch.close();
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		return usersList;
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

	public static void main(String args[]) {
		IWebServer webServer = new WebServer();
		List<String> serviceList;

		//verify login
		webServer.logIn("claudiu", "q1w2e3", "buyer");
		webServer.logIn("mozzie", "q1w2e3", "seller");
		webServer.logIn("neal", "q1w2e3", "seller");
		webServer.logIn("ross", "q1w2e3", "seller");
		webServer.logIn("claudiu", "q1w2e3", "buyer");

		//verify userServies
		serviceList = webServer.userServices("rares");
		System.out.println("----- Services rares -----");
		System.out.println(serviceList);
		
		serviceList = webServer.userServices("rachel");
		System.out.println("---- Services rachel ----");
		System.out.println(serviceList);
		
		//verify getUsersForService
		serviceList = webServer.getUsersForService("travelling");
		System.out.println("----- TRAVELLING --------");
		System.out.println(serviceList);
		
		serviceList = webServer.getUsersForService("opera");
		System.out.println("---- OPERA -----");
		System.out.println(serviceList);
		
		serviceList = webServer.getUsersForService("books");
		System.out.println("---- BOOKS -----");
		System.out.println(serviceList);
		
		//verify logout
		webServer.logOut("claudiu");
		webServer.logOut("mozzie");
		webServer.logOut("neal");
		webServer.logOut("ross");
		
		
	}

}
