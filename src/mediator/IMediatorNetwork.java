package mediator;

import java.util.List;

public interface IMediatorNetwork {

	public void logInUser(String username, String password, String userType, List<String> services);
	
	public void launchOfferRequestNet(String serviceName, String userName);
	public void dropOfferRequestNet(String serviceName, String userName);
	public void makeOfferNet(String serviceName, String userName);
	public void dropAuctionNet(String serviceName, String userName);
	public void acceptOfferNet(String seller, String offer);
	public void refuseOfferNet(String seller, String offer, String buyer);
	
	public void recvUserUpdate(String serviceName, String userName);
	public void recvMakeOffer(String serviceName, String seller);
	public void recvLaunchOfferReq(String userName, String serviceName);
	public void recvDropAuction(String userName, String serviceName);
	public void recvAcceptOffer(String buyer, String serviceName);
	public void recvRefuseOffer(String buyer, String serviceName);
	
	public void interruptTransfer(String seller, String serviceName);
}
