package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;

import constants.StatusMessages;

import user.User;
import user.UserPacket;

public class Network extends INetwork {

	private NetworkClient netClient;

	public Network() {
		netClient = NetworkClient.getClientObject();
	}

	/**
	 * The buyer launches a request for a certain service
	 */
	@Override
	public void launchOfferReq(String userName, String serviceName) {
		SerializableLaunchOfferReq launchOffer = new SerializableLaunchOfferReq();

		launchOffer.username = userName;
		launchOffer.serviceName = serviceName;

		//send offer to the client
		netClient.sendData(launchOffer);
	}
	
	@Override
	public void dropOfferReq(String userName, String serviceName) {
		SerializableDropOfferReq dropOffer = new SerializableDropOfferReq();
		
		dropOffer.username = userName;
		dropOffer.serviceName = serviceName;
		
		netClient.sendData(dropOffer);
	}

	@Override
	public void acceptOffer(String seller, String offer) {
		SerializableAcceptOffer acceptOffer = new SerializableAcceptOffer();
		
		acceptOffer.username = seller;
		acceptOffer.serviceName = offer;
		
		netClient.sendData(acceptOffer);
	}

	@Override
	public void refuseOffer(String seller, String offer) {
		SerializableDropOfferReq dropOffer= new SerializableDropOfferReq();
		
		dropOffer.username = seller;
		dropOffer.serviceName = offer;
		
		netClient.sendData(dropOffer);
	}
	
	@Override
	public void makeOffer(String userName, String serviceName) {
		SerializableMakeOffer makeOffer = new SerializableMakeOffer();
		
		makeOffer.username = userName;
		makeOffer.serviceName = serviceName;
		
		netClient.sendData(makeOffer);
	}

	@Override
	public void dropAuction(String userName, String serviceName) {
		SerializableDropAuction dropAuction = new SerializableDropAuction();
		
		dropAuction.username = userName;
		dropAuction.serviceName = serviceName;
		
		netClient.sendData(dropAuction);
	}

	@Override
	public void logInUser(String username, String password, String userType,
			List<String> serviceList) {

		UserPacket usrPack = new UserPacket();
		usrPack.username = username;
		System.out.println("Recv pass " + password);
		usrPack.password = password;
		usrPack.userType = userType;
		usrPack.toDelete = 0;

		netClient.sendData(usrPack);
	}


	
}
