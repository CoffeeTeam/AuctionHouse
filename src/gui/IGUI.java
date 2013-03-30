package gui;

import java.util.List;

import mediator.IMediatorGUI;

public interface IGUI {
	
	public void updateServices(List<String> offers);
	public void interruptTransfer(String seller, String serviceName);
}
