package states;

import java.util.List;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class StateManager {

	private State currentState;
	public BuyerState buyerState;
	public SellerState sellerState;

	public StateManager(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		buyerState = new BuyerState(medGUI, medWS, medNetwork);
		sellerState = new SellerState(medGUI, medWS, medNetwork);
	}
	
	public void setSellerState() {
		this.currentState = sellerState;
	}
	
	public void setBuyerState() {
		this.currentState = buyerState;
	}
	
	public List<String> getServiceList() {
		return currentState.getServiceList();
	}
	
	public void launchService(String serviceName) {
		this.currentState.launchService(serviceName);
	}

	public void dropService(String serviceName) {
		this.currentState.dropService(serviceName);
	}

	public void acceptOffer(String seller, String serviceName) {
		this.currentState.acceptOffer(seller, serviceName);
	}

	public void refuseOffer(String seller, String serviceName) {
		this.currentState.acceptOffer(seller, serviceName);
	}
}
