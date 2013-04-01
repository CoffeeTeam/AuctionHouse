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
	
	public List<String> getServiceList(String userName) {
		return currentState.getServiceList(userName);
	}
	
	public List<String> getCurrentUsers(String serviceName){
		return currentState.getCurrentUsers(serviceName);
	}
	
	public void launchService(String serviceName, String userName) {
		this.currentState.launchService(serviceName, userName);
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
