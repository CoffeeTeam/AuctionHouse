package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

	private String username;
	private String password;
	private String userType;
	
	/**
	 * Mantain an association between user and auction status with that user 
	 * @author mozzie
	 *
	 */
	private class UserStatusAssoc {
		public String userName;
		public String status;
	}
	
	//associate a list of users to each service
	private HashMap<String,ArrayList<UserStatusAssoc>> matchingUsers;
	
	//list of services associated with a user
	private List<String> userServiceList;

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
		this.userServiceList = userServiceList;
	}

}


