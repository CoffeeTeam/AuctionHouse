package gui;

import java.util.List;


public interface IGUI {
	
	public void updateServices(String serviceName, String userName);
	public void interruptTransfer(String serviceName);

	void launchOfferRequest(String serviceName);
	void dropOfferRequest(String serviceName);
	
	void acceptOffer(String serviceName, String seller);
	void refuseOffer(String serviceName, String seller);
	
	void recvMakeOffer(String serviceName, String seller);
	void recvDropAuction(String userName, String serviceName);
	
	void recvLaunchOfferReq(String userName, String serviceName);
	void recvDropOfferReq(String buyer, String serviceName);

	void recvRefuseOffer(String buyer, String serviceName);
	
	void recvAcceptOffer(String serviceName, String buyer);
	
	void updateServiceUsers(String serviceName, List<String> users);
	
	void updateTransfer(String service, int status);
}
