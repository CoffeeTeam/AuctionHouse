package network;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableRefuseOffer;

public class ClientUtils {

	public static void chooseAction(Object recvObject) {
		System.out.println(recvObject.getClass());

		if (recvObject instanceof SerializableLaunchOfferReq) {
			handleLaunchOfferRequest((SerializableLaunchOfferReq) recvObject);
			return;
		}
		
		/* Handle a response from server to a client's "drop offer" command */
		if (recvObject instanceof SerializableDropOfferReq) {
			handleDropOfferRequest((SerializableDropOfferReq) recvObject);
			return;
		}

		/* Handle a response from server to a client's "refuse offer" command */
		if (recvObject instanceof SerializableRefuseOffer) {
			handleRefuseOffer((SerializableRefuseOffer) recvObject);
			return;
		}
		
		/* Handle a response from server to a client's "accept offer" command */
		if (recvObject instanceof SerializableAcceptOffer) {
			handleAcceptOffer((SerializableAcceptOffer) recvObject);
			return;
		}
		
		/* Handle a response from server to a client's "make offer" command */
		if (recvObject instanceof SerializableMakeOffer) {
			handleMakeOffer((SerializableMakeOffer) recvObject);
			return;
		}
		
		/* Handle a response from server to a client's "drop auction" command */
		if (recvObject instanceof SerializableDropAuction) {
			handleDropAuction((SerializableDropAuction) recvObject);
			return;
		}
	}

	private static void handleLaunchOfferRequest(
			SerializableLaunchOfferReq launchedOffer) {
		NetworkClient.network.recvLaunchOfferReq(launchedOffer.userName, launchedOffer.serviceName);
	}
	
	/**
	 * Handles a packet from server that is result of a "drop offer request" command
	 * 
	 * @param pack		packet from server
	 */
	private static void handleDropOfferRequest(SerializableDropOfferReq pack) {
		// TODO
	}
	
	/**
	 * Handles a packet from server that is result of a "accept offer" command
	 * 
	 * @param pack		packet from server
	 */
	private static void handleAcceptOffer(SerializableAcceptOffer pack) {
		// TODO
	}
	
	/**
	 * Handles a packet from server that is result of a "refuse offer" command
	 * 
	 * @param pack		packet from server
	 */
	private static void handleRefuseOffer(SerializableRefuseOffer pack) {
		System.out.println("[ClientUtils] Received refuse offer packet from server");
		
		NetworkClient.network.recvRefuseOffer(pack.userName, pack.serviceName);
	}
	
	/**
	 * Handles a packet from server that is result of a "make offer" command
	 * 
	 * @param pack		packet from server
	 */
	private static void handleMakeOffer(SerializableMakeOffer pack) {
		NetworkClient.network.recvMakeOffer(pack.userName, pack.serviceName);
	}
	
	/**
	 * Handles a packet from server that is result of a "drop auction" command
	 * 
	 * @param pack		packet from server
	 */
	private static void handleDropAuction(SerializableDropAuction pack) {
		// TODO
	}
}
