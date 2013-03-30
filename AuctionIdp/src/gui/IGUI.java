package gui;

import java.util.List;

import mediator.IMediatorGUI;

public abstract class IGUI {
	
	public IMediatorGUI medGUI;

	public abstract void updateServices(List<String> offers);
	public abstract void interruptTransfer(String seller, String serviceName);
}
