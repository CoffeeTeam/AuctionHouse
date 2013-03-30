package mediator;

import java.util.List;

import states.StateManager;

public class Mediator implements IMediatorGUI, IMediatorNetwork,
		IMediatorWSClient {

	StateManager stateMgr;

	@Override
	public List<String> logInBuyer(String name, String passwd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> logInSeller(String name, String passwd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void launchService(String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropService(String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchOfferRequest(String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(List<String> offerList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptOfferNet(String seller, String offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refuseOfferNet(String seller, String offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void interruptTransfer(String seller, String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptOfferGui(String seller, String offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refuseOfferGui(String seller, String offer) {
		// TODO Auto-generated method stub
		
	}

}
