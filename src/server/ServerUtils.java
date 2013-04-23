package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SelectionKey;

import java.util.ArrayList;
import java.util.List;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableOfferExceeded;
import commands.serializableCommands.SerializableRefuseOffer;

import user.UserPacket;
import util.FileService;

public class ServerUtils {

	public static final String loggedUsersFile = "users";

	/**
	 * Writes into a file the information about the new user
	 * 
	 * @param user
	 *            object containing the information
	 * @param channel
	 *            TODO
	 */
	private static void addUserInfo(UserPacket user, SelectionKey channel) {
		FileWriter file = null;

		try {
			// write data to a database (a file)
			file = new FileWriter(loggedUsersFile, true);
			BufferedWriter outstream = new BufferedWriter(file);

			outstream.write(user.username + "\t" + user.password + "\t"
					+ user.userType + "\n");

			outstream.close();

			// write to local list
			Server.registeredUsersChannels.put(user.username, channel);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * When a user is logging out the entry containing that user is deleted from
	 * the file
	 * 
	 * @param user
	 *            the user that is leaving the system
	 */
	public static void deleteUserInfo(UserPacket user) {
		File file = new File(loggedUsersFile);
		File tmpFile = new File(loggedUsersFile + ".tmp");
		BufferedReader br = null;
		PrintWriter pw = null;
		String line;

		// verify if the file exists
		if (!file.isFile()) {
			System.out.println("File doesn't exist");
			return;
		}

		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e2) {
			System.err.println("File " + loggedUsersFile + "wasn't found");
			e2.printStackTrace();
		}
		try {
			pw = new PrintWriter(new FileWriter(tmpFile));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// create a temporary file
		try {
			if (!tmpFile.exists())
				tmpFile.createNewFile();

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
		 * read from the old file and copy it into the temporary file except the
		 * received user
		 */
		try {
			while (null != (line = br.readLine())) {
				String username = line.split("\\s+")[0];
				if (!username.equals(user.username)) {
					pw.write(line + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		pw.close();
		try {
			br.close();
		} catch (IOException e) {
			System.err.println("Error closing read buffer");
			e.printStackTrace();
		}

		// delete the old file
		if (!file.delete()) {
			System.err.println("Error deleting the old file");
		}

		// rename the temporary file to the original file
		if (tmpFile.renameTo(file)) {
			System.err.println("Could not rename file");
		}

	}

	public static void chooseAction(Object recvObject, SelectionKey key) {
		System.out.println(recvObject.getClass());

		/* Log in handler */
		if (recvObject instanceof UserPacket) {
			handleLogIn((UserPacket) recvObject, key);
			return;
		}

		/* Accept offer handler */
		if (recvObject instanceof SerializableAcceptOffer) {
			handleAcceptOffer((SerializableAcceptOffer) recvObject);
			return;
		}

		/* Refuse offer handler */
		if (recvObject instanceof SerializableRefuseOffer) {
			handleRefuseOffer((SerializableRefuseOffer) recvObject);
			return;
		}

		/* Drop Auction handler */
		if (recvObject instanceof SerializableDropAuction) {
			handleDropAuction((SerializableDropAuction) recvObject);
			return;
		}

		/* Make offer handler */
		if (recvObject instanceof SerializableMakeOffer) {
			handleMakeOffer((SerializableMakeOffer) recvObject);
			return;
		}

		/* Launch Offer Request handler */
		if (recvObject instanceof SerializableLaunchOfferReq) {
			handleLaunchOfferReq((SerializableLaunchOfferReq) recvObject);
			return;
		}

		/* Drop Offer Request handler */
		if (recvObject instanceof SerializableDropOfferReq) {
			handleDropOfferReq((SerializableDropOfferReq) recvObject);
			return;
		}
		
		/* File transfer handler */
		if (recvObject instanceof FileService) {
			handleFileTransferService((FileService)recvObject);
			return;
		}
		
		/* Send offer exceeded handler */
		if (recvObject instanceof SerializableOfferExceeded) {
			handleSendOfferExceeded((SerializableOfferExceeded)recvObject);
			return;
		}
	}

	private static void handleLogIn(UserPacket userPack, SelectionKey key) {
		System.out.println("[Server] A new user logged in");

		addUserInfo(userPack, key);
	}

	/**
	 * Send refuse offer package to multiple sellers
	 * 
	 * @param buyer
	 *            the buyer who sends the package
	 * @param usersToRefuse
	 *            the seller which will be refused
	 */
	private static void sendRefuseOfferPackages(String buyer, String service,
			List<String> usersToRefuse) {
		SerializableRefuseOffer refuseOffer;
		SelectionKey key;

		for (String refusedSeller : usersToRefuse) {
			refuseOffer = new SerializableRefuseOffer();
			refuseOffer.userName = buyer;
			refuseOffer.serviceName = service;

			key = Server.registeredUsersChannels.get(refusedSeller);

			if (null == key) {
				System.err.println("The user is no longer logged in");
			} else {
				Server.sendData(key, refuseOffer);
			}
		}
	}

	/**
	 * Performs actions required when the user accepts an offer
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleAcceptOffer(SerializableAcceptOffer pack) {
		// send to the seller for which the offer was accepted the confirmation
		System.out.println("[Server] Received accept offer packet");

		String seller;
		List<String> refusedSellers = new ArrayList<String>();
		SelectionKey key = Server.registeredUsersChannels.get(pack.commandInfo
				.get(0));

		if (null == key) {
			System.err.println("[Server]  Target user doesn't exist");
			return;
		}

		seller = pack.commandInfo.get(0);
		// extract the list of sellers to be refused
		if (1 != pack.commandInfo.size()) {
			refusedSellers = pack.commandInfo.subList(1,
					pack.commandInfo.size());
		}

		pack.commandInfo.clear();
		pack.commandInfo.add(seller);

		Server.sendData(key, pack);

		// to all the other sellers send refuse offer packages
		sendRefuseOfferPackages(pack.userName, pack.serviceName, refusedSellers);

	}

	/**
	 * Performs actions required when the user refuses an offer
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleRefuseOffer(SerializableRefuseOffer pack) {
		// Send refused info to the seller (in pack, the seller
		// is stored in userName)

		System.out.println("[Server] Received refuse offer packet\n\t sending"
				+ " anounce to seller");

		// get key
		SelectionKey key = Server.registeredUsersChannels.get(pack.userName);

		// set username in packet to buyer instead of seller
		pack.userName = pack.commandInfo.get(0);
		pack.commandInfo.clear();

		if (key == null) {
			System.out.println("[Server] The user " + pack.userName +
					" is no longer logged in => packet not sent");
		} else {
			Server.sendData(key, pack);
		}
	}

	/**
	 * Performs actions required when the user drops an auction
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleDropAuction(SerializableDropAuction pack) {
		String seller = pack.commandInfo.get(0);
		String buyer = pack.userName;
		SelectionKey key;

		// reset list in pack (no longer needed)
		pack.commandInfo = null;
		pack.userName = seller;
		
		// try to send "drop auction" packet to the buyer
		key = Server.registeredUsersChannels.get(buyer);
		
		if (key == null) {
			System.out.println("[Server] User " + seller + " is no longer" +
					" logged in => no 'drop offer' message sent to him");
			return;
		}
		
		System.out.println("[Server] SENDING drop auction to " + buyer + " for service " +
				pack.serviceName + " (from " + seller + ")");

		Server.sendData(key, pack);
	}

	/**
	 * Performs actions required when the user makes an offer
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleMakeOffer(SerializableMakeOffer pack) {
		System.out.println("[Server] Received a Make Offer packet");

		SelectionKey key;

		if (pack.commandInfo == null || pack.commandInfo.size() != 2) {
			System.err.println("Two aditional params required as command info: " +
					"buyer's name and price offered");
			return;
		}

		key = Server.registeredUsersChannels.get(pack.commandInfo.get(0));
		if (null == key) {
			System.err.println("The buyer is no longer logged in");
			return;
		}

		// rearrange information in packet (send to the buyer only relevant data)
		// remove buyer's name from packet
		pack.commandInfo.remove(0);
		
		Server.sendData(key, pack);
	}

	/**
	 * Performs actions required when the user launches an offer request
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleLaunchOfferReq(SerializableLaunchOfferReq pack) {
		List<String> interestedUsers = pack.commandInfo;
		SelectionKey key;

		for (String userName : interestedUsers) {
			if (null != (key = Server.registeredUsersChannels.get(userName))) {
				// send the info about the new service and user
				Server.sendData(key, pack);
				
				System.out.println("[Server][HANDLE LAUNCH OFFER REQ] sent update " +
						"packet to user " + userName);
			} else {
				System.err.println("[Server] the user " + userName
						+ " is no longer logged in");
			}
		}
	}

	/**
	 * Performs actions required when the user drops an offer request
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleDropOfferReq(SerializableDropOfferReq pack) {
		List<String> sellers = pack.commandInfo;
		SelectionKey key;

		// reset list in pack (no longer needed)
		pack.commandInfo = null;
		
		// send "drop offer" packet to each seller still logged in		
		for (String seller : sellers) {
			key = Server.registeredUsersChannels.get(seller);
			
			if (key == null) {
				System.out.println("[Server] User " + seller + " is no longer" +
						" logged in => no 'drop offer' message sent to him");
				continue;
			}

			System.out.println("[Server] sending drop offer announce to seller " + 
					seller + " (from buyer " + pack.userName + ", service " +
					pack.serviceName + ")");

			Server.sendData(key, pack);
		}
	}
	
	private static void handleFileTransferService(FileService recvObject) {
		System.out.println("[Server] Send file to buyer " + recvObject.buyer);
		
		String buyer = recvObject.buyer;
		SelectionKey key = Server.registeredUsersChannels.get(buyer);
		
		if (null == key) {
			System.err.println("The buyer logged out");
			return;
		}
		
		//send data to buyer
		Server.sendData(key, recvObject);
	}

	/**
	 * Performs actions required when a buyer receives an offer that tops other ones
	 * 
	 * @param pack		packet with request information
	 */
	private static void handleSendOfferExceeded(SerializableOfferExceeded pack) {
		List<String> sellers = pack.commandInfo;
		SelectionKey key;

		// reset list in pack (no longer needed)
		pack.commandInfo = null;
		
		// send "offer exceeded" packet to each seller still logged in
		for (String seller : sellers) {
			key = Server.registeredUsersChannels.get(seller);
			
			if (key == null) {
				System.out.println("[Server] User " + seller + " is no longer" +
						" logged in => no 'drop offer' message sent to him");
				continue;
			}

			Server.sendData(key, pack);
		}
	}
}
