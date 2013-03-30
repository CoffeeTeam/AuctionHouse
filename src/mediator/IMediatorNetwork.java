package mediator;

import java.util.List;

public interface IMediatorNetwork {

	public void launchOfferRequest(String serviceName);
	public void updateUser(List<String> offerList);
	public void acceptOfferNet(String seller, String offer);
	public void refuseOfferNet(String seller, String offer);
	public void interruptTransfer(String seller, String serviceName);
}
