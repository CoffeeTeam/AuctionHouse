package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

}


