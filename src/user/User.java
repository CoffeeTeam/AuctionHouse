package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import constants.StatusMessages;
import constants.Symbols;

public class User extends UserPacket {

	private static final long serialVersionUID = -67237845923510708L;

	// associate a list of users to each service - status
	private HashMap<String, HashMap<String, String>> matchingUsers;

	// active transfers (if any)
	private HashMap<String, UserTransferStatus> transfersInfo;

	public HashMap<String, UserTransferStatus> getTransfersInfo() {
		return transfersInfo;
	}

	public void setTransfersInfo(HashMap<String, UserTransferStatus> transfersInfo) {
		this.transfersInfo = transfersInfo;
	}

	public User() {
		this.matchingUsers = new HashMap<String, HashMap<String, String>>();
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

		for (String serviceName : userServiceList) {
			tmpHash = new HashMap<String, String>();
			this.matchingUsers.put(serviceName, tmpHash);
		}
	}

	public void setUserListForService(String serviceName, List<String> users) {
		HashMap<String, String> serviceHashMap = this.matchingUsers
				.get(serviceName);

		for (String user : users) {
			serviceHashMap.put(user, StatusMessages.noOffer);
		}

	}

	/**
	 * Checks whether this user has any matches for the given service
	 */
	public boolean isEmptyService(String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers
				.get(serviceName);

		return serviceHashMap.isEmpty();
	}

	/**
	 * Checks whether this user has any services launched
	 */
	public boolean isEmptyServiceList() {
		return this.matchingUsers.isEmpty();
	}

	/**
	 * Gets the info on this transfer for the given service if any (null
	 * otherwise)
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
	public HashMap<String, String> getServiceProviders(String serviceName) {
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
		HashMap<String, String> serviceHashMap = this.matchingUsers
				.get(serviceName);
		serviceHashMap.clear();
		this.matchingUsers.put(serviceName, serviceHashMap);

	}

	public void removeUserFromService(String userName, String serviceName) {
		HashMap<String, String> serviceHashMap = this.matchingUsers
				.get(serviceName);
		serviceHashMap.remove(userName);
		this.matchingUsers.put(serviceName, serviceHashMap);
	}

	/**
	 * Starts the transfer when an offer is accepted and refuses all other
	 * offers
	 */
	public void startTransfer(String serviceName, String otherUser) {
		// Refuse all other offers, then the service will start
		HashMap<String, String> serviceHashMap = this.matchingUsers
				.get(serviceName);
		for (String user : serviceHashMap.keySet()) {
			if (user.equals(otherUser)) {
				serviceHashMap.put(user, StatusMessages.offerAccepted);
				transfersInfo.put(serviceName, new UserTransferStatus(
						otherUser, StatusMessages.transferStarted));
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
		this.matchingUsers.get(serviceName).put(otherUser,
				StatusMessages.offerRefused);
	}

	/**
	 * Update status of a service provided by another user
	 */
	public void updateStatus(String serviceName, String provider, String status) {
		HashMap<String, String> serviceUsers = this.matchingUsers.get(serviceName);

		if (null == serviceUsers) {
			System.out.println("Hash for this service is NULL");
			System.exit(1);
		}

		serviceUsers.put(provider, status);
	}

	public boolean hasStatus(String serviceName, String status) {
		HashMap<String, String> serviceUsers = this.matchingUsers
				.get(serviceName);
		if (serviceUsers.containsValue(status))
			return true;

		return false;
	}

	public boolean allOffersHaveStatus(String status) {
		List<String> services = this.getUserServiceList();

		for (String service : services) {
			if (!hasStatus(service, status))
				return false;
		}

		return true;
	}

	public List<String> getUsersHavingStatus(String sellerName, String status,
			String serviceName) {
		List<String> usersWithStatus = new ArrayList<String>();
		HashMap<String, String> usersService = this.matchingUsers
				.get(serviceName);

		String userName;
		String currentStatus;
		Set<String> userNames = usersService.keySet();
		Iterator<String> userNamesIt = userNames.iterator();

		while (userNamesIt.hasNext()) {
			userName = userNamesIt.next();
			currentStatus = usersService.get(userName);

			if (!userName.equals(sellerName) && currentStatus.equals(status)) {
				usersWithStatus.add(userName);
			}
		}

		return usersWithStatus;
	}
	
	/**
	 * Verify logout conditions for seller.
	 * @return true if the seller can logout or false otherwise
	 */
	
	public boolean canSellerLogout() {
		String serviceName;
		
		Set<String> services = this.matchingUsers.keySet();
		Iterator<String> serviceIt = services.iterator();
		
		HashMap<String, String> userStatuses;
		
		while (serviceIt.hasNext()) {
			serviceName = serviceIt.next();
			
			if (!this.matchingUsers.isEmpty()) {
				userStatuses = this.matchingUsers.get(serviceName);
				if (userStatuses.containsValue(StatusMessages.offerExceeded) ||
						userStatuses.containsValue(StatusMessages.offerMade) ||
						userStatuses.containsValue(StatusMessages.transferStarted) ||
						userStatuses.containsValue(StatusMessages.transferInProgress))
					
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Empty all structures used by the user
	 */
	public void emptyData() {
		setUsername(Symbols.emptyString);
		setPassword(Symbols.emptyString);
		matchingUsers.clear();
		transfersInfo.clear();
	}

}
