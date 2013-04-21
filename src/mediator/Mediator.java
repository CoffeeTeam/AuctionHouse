package mediator;

import gui.IGUI;

import java.util.List;

import constants.UserTypes;

import network.*;

import states.StateManager;
import web_service.IWSClient;
import web_service.WSClient;

public class Mediator implements IMediatorGUI, IMediatorNetwork,
		IMediatorWSClient {

	StateManager stateMgr;
	private IWSClient wsClient;
	private INetwork network;
	private IGUI gui;

	public Mediator(IGUI gui) {
		stateMgr = new StateManager(this, this, this);
		wsClient = new WSClient(this);
		network = new Network(this);
		this.gui = gui;
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
		List<String> interestedUsers = wsClient.getCurrentUsers(serviceName, UserTypes.buyer);
		network.launchOfferReq(userName, serviceName, interestedUsers);
	}

	@Override
	public void acceptOfferNet(String seller, String offer) {
		network.acceptOffer(seller, offer);
	}

	@Override
	public void refuseOfferNet(String seller, String offer, String buyer) {
		network.refuseOffer(seller, offer, buyer);
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
	public void refuseOfferGui(String seller, String offer, String buyer) {
		stateMgr.refuseOffer(seller, offer, buyer);
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
		//add user to the corresponding service list with the appropriate status
		gui.updateServices(serviceName, userName);
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
		network.dropOfferReq(userName, serviceName);
	}

	@Override
	public void makeOffer(String serviceName, String userName) {
		// TODO Auto-generated method stub
		network.makeOffer(userName, serviceName);
	}

	@Override
	public void dropAuction(String serviceName, String userName) {
		// TODO Auto-generated method stub
		network.dropAuction(userName, serviceName);
	}

	@Override
	public void makeOfferToBuyer(String serviceName,
			String seller) {
		gui.makeOfferToBuyer(serviceName, seller);
	}

	@Override
	public void dropAuctionSeller(String userName, String serviceName) {
		gui.dropAuctionSeller(userName, serviceName);
	}

	@Override
	public void recvLaunchOfferReq(String userName, String serviceName) {
		System.out.println("Mediator network");
		gui.recvLaunchOfferReq(userName, serviceName);
	}

}
