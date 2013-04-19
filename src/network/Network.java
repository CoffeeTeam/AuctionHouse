package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import constants.StatusMessages;

import user.User;
import user.UserPackage;

public class Network extends INetwork {

	private NetworkClient netClient; 
	
	public Network() {
		users = new ArrayList<User>();
		netClient = NetworkClient.getClientObject();
	}

	@Override
	public void launchOfferReq(String userName, String serviceName) {
		// TODO Auto-generated method stub
		User usr = findUser(userName);
		for (int i = 0; i < users.size(); ++i) {
			if (!users.get(i).getUsername().equals(userName)
					&& !users.get(i).getUserType().equals(usr.getUserType())) {
				this.med.updateUser(serviceName, userName);
			}
		}
		System.out.println("Inform the other users");
	}
	
	@Override
	public void dropOfferReq(String serviceName) {
		
	}
	
	@Override
	public void acceptOffer(String seller, String offer) {

	}

	@Override
	public void refuseOffer(String seller, String offer) {
		// TODO 1

	}

	@Override
	public void logInUser(String username, String password, String userType,
			List<String> serviceList) {
		// TODO 1 => refuza
		User usr = new User();
		usr.setUsername(username);
		usr.setPassword(password);
		usr.setUserType(userType);
		usr.setUserServiceList(serviceList);
		users.add(usr);
		
		UserPackage usrPack = new UserPackage();
		usrPack.username = username;
		System.out.println("Recv pass " + password);
		usrPack.password = password;
		usrPack.userType = userType;
		usrPack.toDelete = 0;
		
		try {
			netClient.sendData(usrPack);
		} catch (Exception e) {
			System.err.println("Error serializing login object");
			e.printStackTrace();
		}
	}
	

	private User findUser(String userName) {
		for (User user : users) {
			if (user.getUsername().equals(userName))
				return user;
		}

		return null;
	}

	@Override
	public void dropOfferReq(String userName, String serviceName) {
		HashMap<String, String> serviceUsers = findUser(userName)
				.getUserStatus(serviceName);

		Set<String> assocUsers = serviceUsers.keySet();
		Iterator<String> it = assocUsers.iterator();
		// refuse all offers from sellers if the buyer didn't already accepted
		// it
		while (it.hasNext()) {
			String user = it.next();
			String status = serviceUsers.get(user);

			if (!status.equals(StatusMessages.offerAccepted))
				refuseOffer(user, serviceName);
		}
		
	}

	@Override
	public void makeOffer(String userName, String serviceName) {
		HashMap<String, String> tmpOffers;
		//all users interested by this service will be notified about the offer
		for(User user : users) {
			if(user.getUserServiceList().contains(serviceName)) {
				tmpOffers = user.getUserStatus(serviceName);
				if(!tmpOffers.isEmpty())
					this.med.makeOfferToBuyer(serviceName, serviceName);
			}
		}
	}

	@Override
	public void dropAuction(String userName, String serviceName) {
		HashMap<String, String> tmpOffers;
		//every user that had contact with the user will be informed
		for(User user : users) {
			tmpOffers = user.getUserStatus(serviceName);
			if(tmpOffers.containsKey(userName))
				med.dropAuctionSeller(userName, serviceName);
		}
	}
}
