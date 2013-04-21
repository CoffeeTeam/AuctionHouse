package network;

import java.util.List;

import mediator.IMediatorNetwork;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableRefuseOffer;

import user.UserPacket;

public class Network extends INetwork {

	private NetworkClient netClient;

	public Network(IMediatorNetwork med) {
		this.med = med;
		netClient = NetworkClient.getClientObject(this);
		netClient.execute();
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

		System.out.println("Interested users are " + interestedUsers);
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
	public void refuseOffer(String seller, String offer, String buyer) {
		SerializableRefuseOffer dropOffer= new SerializableRefuseOffer();
		
		dropOffer.userName = seller;
		dropOffer.serviceName = offer;
		dropOffer.commandInfo.add(buyer);
		
		netClient.sendData(dropOffer);
		
		System.out.println("[Network] " + buyer + " sent 'refuse offer' for " +
				"service " + offer + " to " + seller);
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

	
	/* Methods that handle server response */
	
	@Override
	public void recvLaunchOfferReq(String userName, String serviceName) {
		System.out.println("[Network] recv launch offer");
		med.recvLaunchOfferReq(userName, serviceName);
	}

	@Override
	public void recvRefuseOffer(String buyer, String serviceName) {
		System.out.println("[Network] => received refuse offer feedback");
		
		med.recvRefuseOffer(buyer, serviceName);
	}

	@Override
	public void recvAcceptOffer(String buyer, String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvMakeOffer(String seller, String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvDropAuction(String seller, String serviceName) {
		// TODO Auto-generated method stub
		
	}
	
}
