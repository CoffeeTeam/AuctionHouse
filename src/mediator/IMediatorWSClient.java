package mediator;

import java.util.List;

public interface IMediatorWSClient {

	/**
	 * Gets the list of services provided/asked for by a user
	 */
	public List<String> getServiceList(String userName, String userType);
	
	/**
	 * Gets the list of matching users for a given service * user type
	 * (buyer or seller)
	 */
	public List<String> getUserList(String serviceName, String userType);
}
