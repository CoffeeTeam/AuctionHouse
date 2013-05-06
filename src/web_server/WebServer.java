package web_server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.sql.RowSet;

import com.sun.rowset.JdbcRowSetImpl;

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
		String status;

		System.out.println("Enter log in...");
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * " + "FROM Users "
					+ "WHERE username=" + username + " AND password="
					+ password + " AND type=" + type);

			System.out.println("Connected to DB..");
			// verify if the user with the given characteristics exists

			System.out.println("Before loop");
			while (rs.next()) {
				System.out.println("Enter loop");
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
		return null;
	}

	@Override
	public void updateStatus(String username) {

	}

	@Override
	public List<String> getUsersForService(String serviceName) {
		return null;
	}

	@Override
	public void logOut(String username) {
		try {
			RowSet rs = new JdbcRowSetImpl(WebServerInfo.url,
					WebServerInfo.username, WebServerInfo.password);
			rs.setCommand("SELECT * " + "FROM Users " + "WHERE username = "
					+ username);
			rs.execute();

			while (rs.next()) {
				rs.setString(WebServerInfo.status, WebServerInfo.inactive);
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

		webServer.logIn("bitza", "q1w2e3", "buyer");
		webServer.logIn("bitza", "q1w2e3", "buyer");
	}

}
