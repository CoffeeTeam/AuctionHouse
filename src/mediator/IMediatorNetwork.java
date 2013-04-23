package mediator;

import java.util.List;

public interface IMediatorNetwork {

	public void logInUser(String username, String password, String userType, List<String> services);
	
	public void launchOfferRequestNet(String serviceName, String userName);
	public void dropOfferRequestNet(String serviceName, String userName);
	public void makeOfferNet(String seller, String serviceName, String buyer);
	public void dropAuctionNet(String serviceName, String userName);
	public void acceptOfferNet(String buyer, String offer, String sellers);
	public void refuseOfferNet(String seller, String offer, String buyer);
	
	public void recvUserUpdate(String serviceName, String userName);
	
	public void recvMakeOffer(String serviceName, String seller, String price);
	public void recvLaunchOfferReq(String userName, String serviceName);
	public void recvDropOfferReq(String buyer, String serviceName);
	public void recvDropAuction(String userName, String serviceName);
	public void recvAcceptOffer(String buyer, String serviceName, String seller);
	public void recvRefuseOffer(String buyer, String serviceName);
	
	public void interruptTransfer(String seller, String serviceName);
	public void acceptFileTransfer(String service, int progress);

	public void recvOfferExceeded(String userName, String serviceName);
}
