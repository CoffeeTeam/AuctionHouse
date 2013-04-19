package network;

import java.util.List;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;

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
	public void launchOfferReq(String userName, String serviceName, List<String> interestedUsers) {
		SerializableLaunchOfferReq launchOffer = new SerializableLaunchOfferReq();

		launchOffer.userName = userName;
		launchOffer.serviceName = serviceName;
		launchOffer.commandInfo = interestedUsers;

		//send offer to the client
		netClient.sendData(launchOffer);
	}
	
	@Override
	public void dropOfferReq(String userName, String serviceName) {
		SerializableDropOfferReq dropOffer = new SerializableDropOfferReq();
		
		dropOffer.userName = userName;
		dropOffer.serviceName = serviceName;
		
		netClient.sendData(dropOffer);
	}

	@Override
	public void acceptOffer(String seller, String offer) {
		SerializableAcceptOffer acceptOffer = new SerializableAcceptOffer();
		
		acceptOffer.userName = seller;
		acceptOffer.serviceName = offer;
		
		netClient.sendData(acceptOffer);
	}

	@Override
	public void refuseOffer(String seller, String offer) {
		SerializableDropOfferReq dropOffer= new SerializableDropOfferReq();
		
		dropOffer.userName = seller;
		dropOffer.serviceName = offer;
		
		netClient.sendData(dropOffer);
	}
	
	@Override
	public void makeOffer(String userName, String serviceName) {
		SerializableMakeOffer makeOffer = new SerializableMakeOffer();
		
		makeOffer.userName = userName;
		makeOffer.serviceName = serviceName;
		
		netClient.sendData(makeOffer);
	}

	@Override
	public void dropAuction(String userName, String serviceName) {
		SerializableDropAuction dropAuction = new SerializableDropAuction();
		
		dropAuction.userName = userName;
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
