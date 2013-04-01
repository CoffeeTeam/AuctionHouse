package network;

import java.util.ArrayList;
import java.util.List;

import user.User;

import mediator.IMediatorNetwork;

public abstract class INetwork {

	public IMediatorNetwork med;
	public ArrayList<User> users;

	public abstract void logInUser(String username, String password,
			String userType, List<String> services);

	public abstract void launchOfferReq(String userName, String serviceName);

	public abstract void dropOffer(String userName, String serviceName);
	
	public abstract void acceptOffer(String seller, String offer);

	public abstract void refuseOffer(String seller, String offer);

}
