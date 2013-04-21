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

import java.util.List;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableRefuseOffer;

import user.UserPacket;

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
	}

	private static void handleLogIn(UserPacket userPack, SelectionKey key) {
		System.out.println("[SERVER] it is a user package");

		if (userPack.toDelete == 1)
			deleteUserInfo(userPack);
		else {
			System.out.println("[SERVER] Add a new user");
			addUserInfo(userPack, key);
		}
	}

	/**
	 * Performs actions required when the user accepts an offer
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleAcceptOffer(SerializableAcceptOffer pack) {
		// TODO
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
		
		System.out.println("[Server] Received refuse offer packet\n\t sending" +
				" anounce to seller");
		
		// get key
		SelectionKey key = Server.registeredUsersChannels.get(pack.userName);
		
		// set username in packet to buyer instead of seller
		pack.userName = pack.commandInfo.get(0);
		pack.commandInfo.clear();
		
		if (key == null) {
			System.err.println("The user " + pack.userName +
					" is no longer logged in");
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
		// TODO
	}

	/**
	 * Performs actions required when the user makes an offer
	 * 
	 * @param pack
	 *            packet with request information
	 */
	private static void handleMakeOffer(SerializableMakeOffer pack) {
		SelectionKey key;
		
		if (pack.commandInfo.isEmpty()) {
			System.err.println("No buyer specified to make offer");
			return;
		}
		
		key = Server.registeredUsersChannels.get(pack.commandInfo.get(0));
		if (null == key) {
			System.err.println("The buyer is no longer logged in");
			return;
		}
		
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
			if (null != Server.registeredUsersChannels.get(userName)) {
				// send the info about the new service and user
				key = Server.registeredUsersChannels.get(userName);
				
				if (null == key)
					System.err.println("[Server] the user " + userName +
							" is no longer logged in");
				else
					Server.sendData(key, pack);
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
		//TODO

	}

}
