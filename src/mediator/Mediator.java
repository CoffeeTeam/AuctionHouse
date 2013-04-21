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
		List<String> serviceList;
		stateMgr.setBuyerState();
		serviceList = stateMgr.getServiceList(name);

		if (!serviceList.isEmpty())
			logInUser(name, passwd, UserTypes.buyer, serviceList);

		return serviceList;
	}

	@Override
	public List<String> logInSeller(String name, String passwd) {
		List<String> serviceList;

		stateMgr.setSellerState();
		serviceList = stateMgr.getServiceList(name);

		if (!serviceList.isEmpty())
			logInUser(name, passwd, UserTypes.seller, serviceList);

		return serviceList;
	}

	
	
	/* Generic calls => do not consider if the user is a buyer or a seller */
	
	@Override
	public void launchService(String serviceName, String userName, String... auxInfo) {
		if (0 != auxInfo.length)
			stateMgr.launchService(serviceName, userName, auxInfo[0]);
		else 
			stateMgr.launchService(serviceName, userName);
	}

	@Override
	public void dropService(String serviceName, String userName, String... auxInfo) {
		stateMgr.dropService(serviceName, userName);
	}
	
	/* User specific methods => they are called depending on the user's state
	 * (buyer or seller) */
	
	@Override
	public void launchOfferRequestNet(String serviceName, String userName) {
		List<String> interestedUsers = wsClient.getCurrentUsers(serviceName, UserTypes.buyer);
		gui.updateServiceUsers(serviceName, interestedUsers);
		network.launchOfferReq(userName, serviceName, interestedUsers);
	}

	
	
	/* GUI calls */
	
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
	public List<String> getUsers(String serviceName) {
		// TODO Auto-generated method stub
		return stateMgr.getCurrentUsers(serviceName);
	}
	
	
	
	/* Network calls */
	
	/* User to network calls */
	
	@Override
	public void logInUser(String username, String password, String userType,
			List<String> services) {
		// TODO Auto-generated method stub
		network.logInUser(username, password, userType, services);
	}

	@Override
	public void dropOfferRequestNet(String serviceName, String userName) {
		// TODO Auto-generated method stub
		network.dropOfferReq(userName, serviceName);
	}

	@Override
	public void makeOfferNet(String seller, String serviceName, String buyer) {
		network.makeOffer(seller, serviceName, buyer);
	}

	@Override
	public void dropAuctionNet(String serviceName, String userName) {
		// TODO Auto-generated method stub
		network.dropAuction(userName, serviceName);
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

	
	/* Network to user calls */
	
	@Override
	public void recvLaunchOfferReq(String userName, String serviceName) {
		gui.recvLaunchOfferReq(userName, serviceName);
	}
	
	@Override
	public void recvMakeOffer(String serviceName,
			String seller) {
		gui.recvMakeOffer(serviceName, seller);
	}

	@Override
	public void recvDropAuction(String userName, String serviceName) {
		gui.recvDropAuction(userName, serviceName);
	}

	@Override
	public void recvUserUpdate(String serviceName, String userName) {
		//add user to the corresponding service list with the appropriate status
		gui.updateServices(serviceName, userName);
	}
	
	@Override
	public void recvAcceptOffer(String buyer, String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvRefuseOffer(String buyer, String serviceName) {
		gui.recvRefuseOffer(buyer, serviceName);
	}
	
	
	
	/* Web Service Client calls */
	
	@Override
	public List<String> getServiceList(String userName, String userType) {
		// TODO Auto-generated method stub
		return wsClient.getServiceList(userName, userType);
	}

	@Override
	public List<String> getUserList(String serviceName, String userType) {
		// TODO Auto-generated method stub
		return wsClient.getCurrentUsers(serviceName, userType);
	}

	
}
