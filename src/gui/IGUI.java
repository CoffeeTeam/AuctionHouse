package gui;

import java.util.List;

import mediator.IMediatorGUI;

public interface IGUI {
	
	public void updateServices(String serviceName, String userName);
	public void interruptTransfer(String seller, String serviceName);
	public void makeOfferToBuyer(String serviceName, String seller);
}
