package gui;

import java.util.List;


public interface IGUI {
	
	public void updateServices(String serviceName, String userName);
	public void interruptTransfer(String serviceName);

	void launchOffer(String serviceName);
	void dropOffer(String serviceName);
	
	void acceptOffer(String serviceName, String seller);
	void refuseOffer(String serviceName, String seller);
	
	void recvMakeOffer(String serviceName, String seller);
	void recvDropAuction(String userName, String serviceName);
	void recvLaunchOfferReq(String userName, String serviceName);

	void recvRefuseOffer(String buyer, String serviceName);
	void updateServiceUsers(String serviceName, List<String> users);
}
