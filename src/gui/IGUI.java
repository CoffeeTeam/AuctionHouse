package gui;


public interface IGUI {
	
	public void updateServices(String serviceName, String userName);
	public void interruptTransfer(String serviceName);

	void launchOffer(String serviceName);
	void dropOffer(String serviceName);
	void acceptOffer(String serviceName, String seller);
	void refuseOffer(String serviceName, String seller);
}
