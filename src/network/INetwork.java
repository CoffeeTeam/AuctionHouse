package network;

import java.util.List;

import mediator.IMediatorNetwork;

public abstract class INetwork {

	public IMediatorNetwork med;

	public abstract void logInUser(String username, String password,
			String userType, List<String> services);

	/* Methods that handle client actions */
	
	public abstract void launchOfferReq(String userName, String serviceName, List<String> interestedUsers);
	public abstract void dropOfferReq(String buyer, String serviceName, List<String> sellers);
	
	public abstract void makeOffer(String seller, String serviceName, String buyer);
	public abstract void sendOfferExceeded(List<String> sellers, String serviceName, String buyer);
	public abstract void dropAuction(String userName, String serviceName);
	
	public abstract void acceptOffer(String buyer, String offer, String seller);
	public abstract void refuseOffer(String seller, String offer, String buyer);

	/* Methods to handle server feedback */
	
	public abstract void recvLaunchOfferReq(String userName, String serviceName);
	public abstract void recvDropOfferReq(String buyer, String serviceName);

	public abstract void recvAcceptOffer(String buyer, String serviceName, String seller);
	public abstract void recvRefuseOffer(String buyer, String serviceName);
	
	public abstract void recvMakeOffer(String seller, String serviceName, String price);
	public abstract void recvDropAuction(String seller, String serviceName);
	
	public abstract void sendFileToBuyer(String buyer, String serviceName, String seller);
	public abstract void recvFileTransfer(String seller, String serviceName, byte[] fileContent);

	public abstract void recvOfferExceeded(String userName, String serviceName);
}
