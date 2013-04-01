package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import constants.StatusMessages;

import user.User;

public class Network extends INetwork {

	public Network() {
		users = new ArrayList<User>();
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
	public void acceptOffer(String seller, String offer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void refuseOffer(String seller, String offer) {
		// TODO 1 => refuza

	}

	@Override
	public void logInUser(String username, String password, String userType,
			List<String> serviceList) {
		// TODO Auto-generated method stub
		User usr = new User();
		usr.setUsername(username);
		usr.setPassword(password);
		usr.setUserType(userType);
		usr.setUserServiceList(serviceList);
		users.add(usr);
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
		// TODO Auto-generated method stub
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
		
		System.out.println("I dropped offers");

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
		System.out.println("Offer was made");
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
		System.out.println("I dropped the auction");
	}
}
