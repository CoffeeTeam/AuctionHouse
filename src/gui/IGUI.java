package gui;


public interface IGUI {
	
	public void updateServices(String serviceName, String userName);
	public void interruptTransfer(String serviceName);

	void launchOffer(String serviceName);
	void dropOffer(String serviceName);
	
	void acceptOffer(String serviceName, String seller);
	void refuseOffer(String serviceName, String seller);
	
	void makeOfferToBuyer(String serviceName, String seller);
	void dropAuctionSeller(String userName, String serviceName);
	
	void recvLaunchOfferReq(String userName, String serviceName);
}
