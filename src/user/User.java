package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import constants.StatusMessages;

public class User extends UserPacket{

	private static final long serialVersionUID = -67237845923510708L;

	//associate a list of users to each service - status
	private HashMap<String,HashMap<String,String>> matchingUsers;

	// active transfers (if any)
	private HashMap<String, UserTransferStatus> transfersInfo;
	
	public User() {
		this.matchingUsers = new HashMap<String, HashMap<String,String>>();
		this.transfersInfo = new HashMap<String, UserTransferStatus>();
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

	public int isToDelete() {
		return toDelete;
	}

	public void setToDelete(int toDelete) {
		this.toDelete = toDelete;
	}
	
	public List<String> getUserServiceList() {
		return new LinkedList<String>(matchingUsers.keySet());
	}

	public void setUserServiceList(List<String> userServiceList) {
		HashMap<String, String> tmpHash;
		
		for(String serviceName : userServiceList) {
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
	
	/**
	 * Checks whether this user has any matches for the given service
	 */
	public boolean isEmptyService(String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		
		return serviceHashMap.isEmpty();
	}
	
	/**
	 * Checks whether this user has any services launched
	 */
	public boolean isEmptyServiceList() {
		return this.matchingUsers.isEmpty();
	}
	
	/**
	 * Gets the info on this transfer for the given service if any (null otherwise)
	 */
	public UserTransferStatus getServiceTransfer(String serviceName) {
		return transfersInfo.get(serviceName);
	}
	
	/**
	 * get the association between an user and it's status for a certain service
	 * 
	 * @param serviceName
	 * @return hash map containing the associations
	 */
	public HashMap<String,String> getServiceStatus(String serviceName) {
		return this.matchingUsers.get(serviceName);
	}
	
	/**
	 * Gets the status of this user's service provided by another user
	 */
	public String getUserServiceStatus(String serviceName, String anotherUser) {
		HashMap<String, String> providers = matchingUsers.get(serviceName);
		
		if (providers == null)
			return null;
		
		return providers.get(anotherUser);
	}
	
	public void emptyUserListForService(String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		serviceHashMap.clear();
		this.matchingUsers.put(serviceName, serviceHashMap);
		
	}
	
	//this method is used for seller users
	public void addUserToService(String serviceName, String user) {
		System.out.println("I added " + user  +  "  " + serviceName);
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
	 * Starts the transfer when an offer is accepted and refuses all other offers 
	 */
	public void startTransfer(String serviceName, String otherUser) {
		// Refuse all other offers, then the service will start
		HashMap<String, String> serviceHashMap = this.matchingUsers.get(serviceName);
		for (String user:serviceHashMap.keySet()) {
			if (user.equals(otherUser)) {
				serviceHashMap.put(user, StatusMessages.offerAccepted);
				transfersInfo.put(serviceName, new UserTransferStatus(otherUser, StatusMessages.transferStarted));
			} else {
				serviceHashMap.put(user, StatusMessages.offerRefused);
			}
		}
	}
	
	/**
	 * Called by the mediator when an user who has transfered services with the
	 * current one exits
	 */
	public void endTransfer(String serviceName) {
		UserTransferStatus info = transfersInfo.get(serviceName);
		
		if (info.getProgress() < 100) {
			info.setStatus(StatusMessages.transferFailed);
		}
	}
	
	public void refuseOffer(String serviceName, String otherUser) {
		// Change status
		this.matchingUsers.get(serviceName).put(otherUser, StatusMessages.offerRefused);
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
	
	public boolean allOffersHaveStatus(String status) {
		List<String> services = this.getUserServiceList();
		
		for(String service : services){
			if(!hasStatus(service, status))
				return false;
		}
		
		return true;
	}
	
}


