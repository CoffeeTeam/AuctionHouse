package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

	private String username;
	private String password;
	private String userType;

	//associate a list of users to each service
	private HashMap<String,HashMap<String,String>> matchingUsers;
	
	//list of services associated with a user
	private List<String> userServiceList;
	
	/**
	 * Maintain an association between user and auction status with that user 
	 * @author mozzie
	 *
	 */
	private class UserStatusAssoc {
		public String userName;
		public String status;
		
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
		this.matchingUsers = new HashMap<String, HashMap<String,String>>();
		
		for(String serviceName : this.userServiceList) {
			tmpHash = new HashMap<String, String>();
			this.matchingUsers.put(serviceName, tmpHash);
		}

	}

}


