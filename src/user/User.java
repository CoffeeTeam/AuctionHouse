package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import constants.StatusMessages;

public class User {

	private String username;
	private String password;
	private String userType;

	//associate a list of users to each service
	private HashMap<String,HashMap<String,String>> matchingUsers;
	
	//list of services associated with a user
	private List<String> userServiceList;
	
	public User() {
		this.matchingUsers = new HashMap<String, HashMap<String,String>>();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getUserServiceList() {
		return userServiceList;
	}

	public void setUserServiceList(List<String> userServiceList) {
		HashMap<String, String> tmpHash;
		this.userServiceList = userServiceList;
		
		
		for(String serviceName : this.userServiceList) {
			tmpHash = new HashMap<String, String>();
			this.matchingUsers.put(serviceName, tmpHash);
		}
	}
	
	public void setUserListForService(String serviceName, List<String> users) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		
		for(String user : users) {
			serviceHashMap.put(user, StatusMessages.noOffer);
		}
		
		this.matchingUsers.put(serviceName, serviceHashMap);
	}
	
	public boolean isEmptyService(String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		if(serviceHashMap.isEmpty())
			return true;
		return false;
	}
	
	public boolean isEmptyServiceList() {
		return this.matchingUsers.isEmpty();
	}
	
	/**
	 * get the association between an user and it's status for a certain service
	 * @param serviceName
	 * @return hash map containing the associations
	 */
	public HashMap<String,String> getUserStatus(String serviceName) {
		return this.matchingUsers.get(serviceName);
	}
	
	public void emptyUserListForService(String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		serviceHashMap.clear();
		this.matchingUsers.put(serviceName, serviceHashMap);
		
	}
	
	//this method is used for seller users
	public void addUserToService(String serviceName, String user) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		//find the status with which the user should be added
		if(serviceHashMap.isEmpty())
			serviceHashMap.put(user, StatusMessages.noOffer);
		else {
			String status = ((ArrayList<String>) serviceHashMap.values()).get(0);
			serviceHashMap.put(user, status);
		}
		this.matchingUsers.put(serviceName, serviceHashMap);
	}
	
	public void removeUserFromService(String userName, String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		serviceHashMap.remove(userName);
		this.matchingUsers.put(serviceName, serviceHashMap);
	}

	/**
	 * Update status to offer made for a buyer user
	 * @param serviceName the server for which the offer was made
	 * @param seller the name of the seller of the service
	 */
	public void updateStatusForSeller(String serviceName, String seller) {
		HashMap<String, String> serviceUsers = this.matchingUsers.get(serviceName);
		serviceUsers.put(seller, StatusMessages.offerMade);
		this.matchingUsers.put(serviceName, serviceUsers);
	}
	
	public void updateStatusForService(String serviceName) {
		HashMap<String, String> serviceUsers = this.matchingUsers.get(serviceName);
		HashMap<String, String> tmpHash = new HashMap<String, String>();
		Set<String> users = serviceUsers.keySet();
		Iterator<String> it = users.iterator();
		
		while(it.hasNext()) {
			String user = it.next();
			tmpHash.put(user, StatusMessages.offerMade);
		}
		
		this.matchingUsers.put(serviceName, tmpHash);
	}
	
	public boolean hasStatus(String serviceName, String status) {
		HashMap<String, String> serviceUsers = this.matchingUsers.get(serviceName);
		if(serviceUsers.containsValue(status))
			return true;
		
		return false;
	}
}


