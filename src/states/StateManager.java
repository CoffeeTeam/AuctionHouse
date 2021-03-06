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
	
	public void launchService(String serviceName, String userName, String... auxInfo) {
		if (auxInfo.length == 0)
			this.currentState.launchService(serviceName, userName);
		else
			this.currentState.launchService(serviceName, userName, auxInfo[0]);
	}

	public void dropService(String serviceName, String userName, String... auxInfo) {
		this.currentState.dropService(serviceName, userName, auxInfo[0]);
	}

	public void acceptOffer(String buyer, String serviceName, String seller, List<String> otherSellers) {
		this.currentState.acceptOffer(buyer, serviceName, seller, otherSellers);
	}

	public void refuseOffer(String seller, String serviceName, String buyer) {
		this.currentState.refuseOffer(seller, serviceName, buyer);
	}
}
