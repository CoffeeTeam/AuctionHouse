package mediator;

import gui.IGUI;

import java.util.List;

import network.INetwork;

import states.StateManager;
import web_service.IWSClient;
import web_service.WSClient;

public class Mediator implements IMediatorGUI, IMediatorNetwork,
		IMediatorWSClient {

	StateManager stateMgr;
	private IWSClient wsClient;
	private INetwork network;
	private IGUI gui;
	
	public Mediator() {
		stateMgr = new StateManager(this, this, this);
		wsClient = new WSClient(this);
	}

	@Override
	public List<String> logInBuyer(String name, String passwd) {
		// TODO Auto-generated method stub
		stateMgr.setBuyerState();
		return stateMgr.getServiceList(name);
	}

	@Override
	public List<String> logInSeller(String name, String passwd) {
		// TODO Auto-generated method stub
		stateMgr.setSellerState();
		return stateMgr.getServiceList(name);
	}

	@Override
	public void launchService(String serviceName) {
		// TODO Auto-generated method stub
		stateMgr.launchService(serviceName);
	}

	@Override
	public void dropService(String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchOfferRequest(String serviceName) {
		
		
	}

	@Override
	public void updateUser(List<String> offerList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptOfferNet(String seller, String offer) {
		// TODO Auto-generated method stub
		network.acceptOffer(seller, offer);
	}

	@Override
	public void refuseOfferNet(String seller, String offer) {
		// TODO Auto-generated method stub
		network.refuseOffer(seller, offer);
	}

	@Override
	public void interruptTransfer(String seller, String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void acceptOfferGui(String seller, String offer) {
		// TODO Auto-generated method stub
		stateMgr.acceptOffer(seller, offer);
	}

	@Override
	public void refuseOfferGui(String seller, String offer) {
		// TODO Auto-generated method stub
		stateMgr.refuseOffer(seller, offer);
	}

	@Override
	public List<String> getServiceList(String userName, String userType) {
		// TODO Auto-generated method stub
		return wsClient.getServiceList(userName, userType);
	}

	@Override
	public List<String> getUsers(String serviceName) {
		// TODO Auto-generated method stub
		return stateMgr.getCurrentUsers(serviceName);
	}

	@Override
	public List<String> getUserList(String serviceName, String userType) {
		// TODO Auto-generated method stub
		return wsClient.getCurrentUsers(serviceName, userType);
	}

}
