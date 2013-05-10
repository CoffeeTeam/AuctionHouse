package network;

import util.FileService;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableLogOut;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableOfferExceeded;
import commands.serializableCommands.SerializableRefuseOffer;
import commands.serializableCommands.SerializableTransferFailed;

public class ClientUtils {

	public static void chooseAction(Object recvObject) {
		NetworkClient.loggerNetworkClient.info(recvObject.getClass());

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

		if (recvObject instanceof FileService) {
			handleFileTransferService((FileService) recvObject);
			return;
		}

		/*
		 * Handle a response from server to a client's update and a seller's
		 * offer exceed
		 */
		if (recvObject instanceof SerializableOfferExceeded) {
			handleOfferExceeded((SerializableOfferExceeded) recvObject);
			return;
		}

		/*
		 * Handle a response from server to client informing that the transfer
		 * didn't complete successful
		 */
		if (recvObject instanceof SerializableTransferFailed) {
			handleTransferFailed((SerializableTransferFailed) recvObject);
			return;
		}
		
		if (recvObject instanceof SerializableLogOut) {
			handleLogOut((SerializableLogOut) recvObject);
		}
		
	}

	/**
	 * Handle packet from server containing info about a seller registering into
	 * the system
	 * 
	 * @param launchedOffer
	 *            packet from server
	 */
	private static void handleLaunchOfferRequest(
			SerializableLaunchOfferReq launchedOffer) {
		NetworkClient.loggerNetworkClient.info("[Client] => received launch offer request");
		NetworkClient.network.recvLaunchOfferReq(launchedOffer.userName,
				launchedOffer.serviceName);
	}

	/**
	 * Handles a packet from server that is result of a "drop offer request"
	 * command
	 * 
	 * @param pack
	 *            packet from server
	 */
	private static void handleDropOfferRequest(SerializableDropOfferReq pack) {
		NetworkClient.network.recvDropOfferReq(pack.userName, pack.serviceName);
	}

	/**
	 * Handles a packet from server that is result of a "accept offer" command
	 * 
	 * @param pack
	 *            packet from server
	 */
	private static void handleAcceptOffer(SerializableAcceptOffer pack) {
		NetworkClient.loggerNetworkClient.info("[ClientUtils] Received accept offer packet");

		NetworkClient.network.recvAcceptOffer(pack.userName, pack.serviceName,
				pack.commandInfo.get(0));

	}

	/**
	 * Handles a packet from server that is result of a "refuse offer" command
	 * 
	 * @param pack
	 *            packet from server
	 */
	private static void handleRefuseOffer(SerializableRefuseOffer pack) {
		System.out
				.println("[ClientUtils] Received refuse offer packet from server");

		NetworkClient.network.recvRefuseOffer(pack.userName, pack.serviceName);
	}

	/**
	 * Handles a packet from server that is result of a "make offer" command
	 * 
	 * @param pack
	 *            packet from server
	 */
	private static void handleMakeOffer(SerializableMakeOffer pack) {
		if (pack.commandInfo.size() != 1) {
			System.err
					.println("Make offer packet should contain the price (as sole"
							+ " auxiliary parameter)");
		}

		NetworkClient.network.recvMakeOffer(pack.userName, pack.serviceName,
				pack.commandInfo.get(0));
	}

	/**
	 * Handles a packet from server that is result of a "drop auction" command
	 * 
	 * @param pack
	 *            packet from server
	 */
	private static void handleDropAuction(SerializableDropAuction pack) {
		NetworkClient.network.recvDropAuction(pack.userName, pack.serviceName);
	}

	private static void handleFileTransferService(FileService recvObject) {
		NetworkClient.loggerNetworkClient.info("[Client Utils] Receive file handler");

		NetworkClient.network.recvFileTransfer(recvObject.seller,
				recvObject.serviceName, recvObject.fileContent);
	}

	/**
	 * Handles a response from server to a client's update and a seller's offer
	 * exceed
	 */
	private static void handleOfferExceeded(SerializableOfferExceeded pack) {
		NetworkClient.network
				.recvOfferExceeded(pack.userName, pack.serviceName);
	}

	/**
	 * Handle packet from server informing the seller that the transfer of a
	 * service failed
	 * 
	 * @param pack
	 *            the packet from server
	 */
	public static void handleTransferFailed(SerializableTransferFailed pack) {
		NetworkClient.network.recvTransferFailed(pack.userName, pack.serviceName);
	}
	
	public static void handleLogOut(SerializableLogOut pack) {
		NetworkClient.network.recvLogOutUser(pack.userName);
	}
}
