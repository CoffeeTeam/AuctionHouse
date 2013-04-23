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
	public void launchService(String serviceName, String userName,
			String... auxInfo) {
		if (0 != auxInfo.length)
			stateMgr.launchService(serviceName, userName, auxInfo[0]);
		else
			stateMgr.launchService(serviceName, userName);
	}

	@Override
	public void dropService(String serviceName, String userName,
			String... auxInfo) {
		if (auxInfo.length != 1) {
			System.err.println("[Mediator] drop service must receive one aditional info:" +
					" the seller's name");
			return;
		}
				
		stateMgr.dropService(serviceName, userName, auxInfo[0]);
	}

	/*
	 * User specific methods => they are called depending on the user's state
	 * (buyer or seller)
	 */

	@Override
	public void launchOfferRequestNet(String serviceName, String userName) {
		List<String> interestedUsers = wsClient.getCurrentUsers(serviceName,
				UserTypes.buyer);
		gui.updateServiceUsers(serviceName, interestedUsers);
		network.launchOfferReq(userName, serviceName, interestedUsers);
	}
	
	
	
	

	/* GUI calls */

	@Override
	public void acceptOfferGui(String buyer, String offer, String seller,
			List<String> otherSellers) {
		stateMgr.acceptOffer(buyer, offer, seller, otherSellers);
	}

	@Override
	public void refuseOfferGui(String seller, String offer, String buyer) {
		stateMgr.refuseOffer(seller, offer, buyer);
	}

	@Override
	public List<String> getUsers(String serviceName) {
		return stateMgr.getCurrentUsers(serviceName);
	}

	
	
	
	
	
	/* Network calls */

	/* User to network calls */

	@Override
	public void logInUser(String username, String password, String userType,
			List<String> services) {
		network.logInUser(username, password, userType, services);
	}

	@Override
	public void dropOfferRequestNet(String serviceName, String userName) {
		// get the list of users matching this service from the database
		List<String> providers = this.getUserList(serviceName, UserTypes.buyer);

		// send drop offer to network
		network.dropOfferReq(userName, serviceName, providers);
	}

	@Override
	public void makeOfferNet(String seller, String serviceName, String buyer) {
		network.makeOffer(seller, serviceName, buyer);
	}

	@Override
	public void dropAuctionNet(String serviceName, String buyer, String seller) {
		network.dropAuction(buyer, serviceName, seller);
	}

	@Override
	public void acceptOfferNet(String buyer, String offer, String sellers) {
		network.acceptOffer(buyer, offer, sellers);
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
	public void recvDropOfferReq(String buyer, String serviceName) {
		gui.recvDropOfferReq(buyer, serviceName);
	}
	
	@Override
	public void recvMakeOffer(String serviceName, String seller, String price) {
		// update current user & check for exceeded offers
		List<String> exceeded = gui.recvMakeOffer(serviceName, seller, price);
		
		// if there are any exceeded offers, announce the suppliers
		if (! exceeded.isEmpty()) {
			network.sendOfferExceeded(exceeded, serviceName, gui.getCurrentUser());
		}
	}

	@Override
	public void recvDropAuction(String userName, String serviceName) {
		gui.recvDropAuction(userName, serviceName);
	}

	@Override
	public void recvUserUpdate(String serviceName, String userName) {
		// add user to the corresponding service list with the appropriate
		// status
		gui.updateServices(serviceName, userName);
	}

	@Override
	public void recvAcceptOffer(String buyer, String serviceName, String seller) {
		// update status in gui
		gui.recvAcceptOffer(serviceName, buyer);

		// start sending file to buyer
		network.sendFileToBuyer(buyer, serviceName, seller);
	}

	@Override
	public void recvRefuseOffer(String buyer, String serviceName) {
		gui.recvRefuseOffer(buyer, serviceName);
	}

	/* Web Service Client calls */

	@Override
	public List<String> getServiceList(String userName, String userType) {
		return wsClient.getServiceList(userName, userType);
	}

	@Override
	public List<String> getUserList(String serviceName, String userType) {
		return wsClient.getCurrentUsers(serviceName, userType);
	}

	@Override
	public void acceptFileTransfer(String service,
			int progress) {
		gui.updateTransfer(service, progress);
	}

	@Override
	public void recvOfferExceeded(String userName, String serviceName) {
		gui.recvOfferExceeded(userName, serviceName);
	}

}
