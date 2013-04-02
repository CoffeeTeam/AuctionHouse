package mediator;

import gui.IGUI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import constants.StatusMessages;
import constants.UserTypes;

import network.INetwork;
import network.Network;

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
		network = new Network();
	}

	@Override
	public List<String> logInBuyer(String name, String passwd) {
		// TODO Auto-generated method stub
		List<String> serviceList;
		stateMgr.setBuyerState();
		serviceList = stateMgr.getServiceList(name);

		if (!serviceList.isEmpty())
			registerUser(name, passwd, UserTypes.buyer, serviceList);

		return serviceList;
	}

	@Override
	public List<String> logInSeller(String name, String passwd) {
		// TODO Auto-generated method stub
		List<String> serviceList;

		stateMgr.setSellerState();
		serviceList = stateMgr.getServiceList(name);

		if (!serviceList.isEmpty())
			registerUser(name, passwd, UserTypes.seller, serviceList);

		return serviceList;
	}

	@Override
	public void launchService(String serviceName, String userName) {
		// TODO Auto-generated method stub
		stateMgr.launchService(serviceName, userName);
	}

	@Override
	public void dropService(String serviceName, String userName) {
		// TODO Auto-generated method stub
		stateMgr.dropService(serviceName, userName);

	}

	@Override
	public void launchOfferRequest(String serviceName, String userName) {
		network.launchOfferReq(userName, serviceName);
	}

	@Override
	public void acceptOfferNet(String seller, String offer) {
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

	@Override
	public void updateUser(String serviceName, String userName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerUser(String username, String password, String userType,
			List<String> services) {
		// TODO Auto-generated method stub
		network.logInUser(username, password, userType, services);
	}

	@Override
	public void dropOfferRequest(String serviceName, String userName) {
		// TODO Auto-generated method stub
		network.dropOffer(userName, serviceName);
	}

}
