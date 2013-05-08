package web_service;

import java.util.List;

import mediator.IMediatorWSClient;

public abstract class IWSClient {

	public IMediatorWSClient med;
	public static final String WS_URL = "http://localhost:8080/axis/services/WebService?wsdl";
	
	public abstract List<String> getCurrentUsers(String serviceName, String userType);
	public abstract List<String> getServiceList(String userName, String userType);
	
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
	public  abstract boolean callLogIn(String username, String password,
			String type);
	
	/**
	 * Mark the user as inactive
	 * @param username
	 */
	public abstract void callLogOut(String username);

	/**
	 * Get all services that a user offers or is interested in
	 * 
	 * @param username
	 *            the name of the user
	 * @return list of services
	 */
	public abstract List<String> callUserServices(String username);

	/**
	 * Finds all active users that provide a certain service
	 * 
	 * @param serviceName
	 *            name of the service
	 */
	public abstract List<String> callGetUsersForService(String serviceName);
	
	/**
	 * Used by users to launch an offer or to drop an offer
	 * @param username the name of the user
	 */
	public abstract void callChangeOfferStatus(String username, String status, String servicename);
	
	/**
	 * Given a user name and a service name, gets the service's price from the
	 * database
	 * 
	 * @param username		user that provides a service
	 * @param serviceName	service to look for
	 * @return				cost of the service provided by the given user or
	 * 						null if no entry is found in the database
	 */
	/**
	 * @param username
	 * @param serviceName
	 * @return
	 */
	public abstract String getUserServicePrice(String seller, String serviceName);
}
