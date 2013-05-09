package mediator;

import gui.GUI;
import gui.IGUI;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import constants.UserTypes;

import network.*;

import states.StateManager;
import web_service.IWSClient;
import web_service.WSClient;

public class Mediator implements IMediatorGUI, IMediatorNetwork,
		IMediatorWSClient {

	static Logger loggerMediator = Logger.getLogger(Mediator.class);
	StateManager stateMgr;
	private IWSClient wsClient;
	private INetwork network;
	private IGUI gui;

	public Mediator(IGUI gui) {
		PropertyConfigurator.configure("log4j.properties");
		loggerMediator.addAppender(GUI.customAppender.getFileAppender());
		stateMgr = new StateManager(this, this, this);
		wsClient = new WSClient(this);
		network = new Network(this);
		this.gui = gui;
	}

	@Override
	public List<String> logInBuyer(String name, String passwd) {
		loggerMediator.info("Log in buyer " + name);
		
		List<String> serviceList;
		stateMgr.setBuyerState();
		serviceList = stateMgr.getServiceList(name);

		if (!serviceList.isEmpty())
			logInUser(name, passwd, UserTypes.buyer, serviceList);

		return serviceList;
	}

	@Override
	public List<String> logInSeller(String name, String passwd) {
		loggerMediator.info("Log in seller " + name);
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
			loggerMediator.warn("[Mediator] drop service must receive one aditional info:" +
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
		loggerMediator.info("Buyer " + buyer + " accepted offer " + offer +
				" from " + seller);
		
		stateMgr.acceptOffer(buyer, offer, seller, otherSellers);
	}

	@Override
	public void refuseOfferGui(String seller, String offer, String buyer) {
		loggerMediator.info("Buyer " + buyer + " refused offer " + offer +
				" from " + seller);
		
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
		// check user (get info from database)
		if (!wsClient.callLogIn(username, password, userType)) {
			loggerMediator.error("No such record for " + userType + " " + username);
			return;
		}
		
		// let the others know of the newly entered user
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
		// Get price from database
		String price = wsClient.getUserServicePrice(seller, serviceName);

		// Send make offer info to the network module
		network.makeOffer(seller, serviceName, buyer, price);
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
	public void recvTransferFailed(String buyer, String serviceName) {
		loggerMediator.info("Buyer " + buyer + " failed to received service " + serviceName);
		gui.recvTransferFailed(buyer, serviceName);
	}

	/* Network to user calls */

	@Override
	public void recvLaunchOfferReq(String userName, String serviceName) {
		loggerMediator.info("Seller  " + userName + " is interested in " + 
					serviceName + " service");
		
		gui.recvLaunchOfferReq(userName, serviceName);
	}

	@Override
	public void recvDropOfferReq(String buyer, String serviceName) {
		loggerMediator.info("Buyer  " + buyer + " isn't interested in " + 
				serviceName + " service");
		
		gui.recvDropOfferReq(buyer, serviceName);
	}
	
	@Override
	public void recvMakeOffer(String serviceName, String seller, String price) {
		loggerMediator.info("Seller  " + seller + " made an offer for " + 
				serviceName + " service with price " + price);
		
		// update current user & check for exceeded offers
		List<String> exceeded = gui.recvMakeOffer(serviceName, seller, price);
		
		// if there are any exceeded offers, announce the suppliers
		if (! exceeded.isEmpty()) {
			network.sendOfferExceeded(exceeded, serviceName, gui.getCurrentUser());
		}
	}

	@Override
	public void recvDropAuction(String userName, String serviceName) {
		loggerMediator.info("Seller  " + userName + " dropped auction for " + 
				serviceName + " service");
		
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
		loggerMediator.info("Buyer  " + buyer + " accepted  offer " + 
				serviceName + " from " + seller);
		
		// update status in gui
		gui.recvAcceptOffer(serviceName, buyer);

		// start sending file to buyer
		network.sendFileToBuyer(buyer, serviceName, seller);
	}

	@Override
	public void recvRefuseOffer(String buyer, String serviceName) {
		loggerMediator.info("Buyer  " + buyer + " refused  offer " + 
				serviceName);
		
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
		loggerMediator.info("User " + userName + " received offer exceeded "+ 
				 " for service " + serviceName);
		
		gui.recvOfferExceeded(userName, serviceName);
	}

	@Override
	public void startFileTransfer(String serviceName, String buyer) {
		gui.acceptOffer(serviceName, buyer);
	}

	@Override
	public void transferFailed(String seller, String serviceName, String buyer) {
		loggerMediator.warn("Transferring service " + serviceName + " from seller" +
					seller + " to buyer " + buyer + " failed");
		
		network.transferFailed(seller, serviceName, buyer);
	}

}
