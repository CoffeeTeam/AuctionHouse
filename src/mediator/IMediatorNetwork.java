package mediator;

import java.util.List;

public interface IMediatorNetwork {

	public void registerUser(String username, String password, String userType, List<String> services);
	public void launchOfferRequest(String serviceName, String userName);
	public void dropOfferRequest(String serviceName, String userName);
	public void updateUser(String serviceName, String userName);
	public void acceptOfferNet(String seller, String offer);
	public void refuseOfferNet(String seller, String offer);
	public void interruptTransfer(String seller, String serviceName);
}
