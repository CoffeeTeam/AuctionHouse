package web_server;

import java.util.List;

public interface IWebServer {

	/**
	 * Verify if a user which attempts to login introduced valid information
	 * 
	 * @param username
	 *            name of the user
	 * @param password
	 * @param type
	 *            specifies if the user is a buyer or a seller
	 * @return true if introduced data are correct, else false
	 */
	public boolean logIn(String username, String password,
			String type);
	
	/**
	 * Mark the user as inactive
	 * @param username
	 */
	public void logOut(String username);

	/**
	 * Get all services that a user offers or is interested in
	 * 
	 * @param username
	 *            the name of the user
	 * @return list of services
	 */
	public List<String> userServices(String username);

	/**
	 * Finds all active users that provide a certain service
	 * 
	 * @param serviceName
	 *            name of the service
	 */
	public List<String> getUsersForService(String serviceName);
	
	/**
	 * Used by users to launch an offer or to drop an offer
	 * @param username the name of the user
	 */
	public void changeOfferStatus(String username, String status, String servicename);

}
