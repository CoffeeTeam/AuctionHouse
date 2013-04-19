package network;

import java.util.List;

import mediator.IMediatorNetwork;

public abstract class INetwork {

	public IMediatorNetwork med;

	public abstract void logInUser(String username, String password,
			String userType, List<String> services);

	public abstract void launchOfferReq(String userName, String serviceName);
	public abstract void dropOfferReq(String userName, String serviceName);
	
	public abstract void makeOffer(String userName, String serviceName);
	public abstract void dropAuction(String userName, String serviceName);
	
	public abstract void acceptOffer(String seller, String offer);
	public abstract void refuseOffer(String seller, String offer);
	
}
