package network;

import gui.GUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import mediator.IMediatorNetwork;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableOfferExceeded;
import commands.serializableCommands.SerializableRefuseOffer;
import commands.serializableCommands.SerializableTransferFailed;
import constants.NetworkInfo;

import user.UserPacket;
import util.FileService;

public class Network extends INetwork {

	private NetworkClient netClient;
	static Logger loggerNetwork = Logger.getLogger(Network.class);

	public Network(IMediatorNetwork med) {
		PropertyConfigurator.configure("log4j.properties");
		loggerNetwork.addAppender(GUI.customAppender.getFileAppender());
		
		this.med = med;
		netClient = NetworkClient.getClientObject(this);
		netClient.execute();
	}

	/**
	 * The buyer launches a request for a certain service
	 */
	@Override
	public void launchOfferReq(String userName, String serviceName, List<String> interestedUsers) {
		loggerNetwork.info("[ " + userName + "  ] launched offer request for " +
						serviceName);
		
		SerializableLaunchOfferReq launchOffer = new SerializableLaunchOfferReq();

		launchOffer.userName = userName;
		launchOffer.serviceName = serviceName;
		launchOffer.commandInfo = interestedUsers;

		//send offer to the client
		netClient.sendData(launchOffer);
	}
	
	@Override
	public void dropOfferReq(String buyer, String serviceName, String seller) {
		loggerNetwork.info("[ " + buyer + "  ] dropped offer request for " +
				serviceName);


		SerializableDropOfferReq dropOffer = new SerializableDropOfferReq();
		
		dropOffer.userName = buyer;
		dropOffer.serviceName = serviceName;
		dropOffer.commandInfo.add(seller);
		
		netClient.sendData(dropOffer);
	}

	@Override
	public void acceptOffer(String buyer, String offer, String sellers) {
		loggerNetwork.info("[ " + buyer + "  ] accepted offer for " + offer);
		
		SerializableAcceptOffer acceptOffer = new SerializableAcceptOffer();
		
		acceptOffer.userName = buyer;
		acceptOffer.serviceName = offer;
		
		String[] sellerArray = sellers.split("\\s+");
		for (int i = 0; i < sellerArray.length; ++i) {
			acceptOffer.commandInfo.add(sellerArray[i]);
		}
		
		netClient.sendData(acceptOffer);
	}

	@Override
	public void refuseOffer(String seller, String offer, String buyer) {
		loggerNetwork.info("Buyer " + buyer + " refused offer for " + offer + 
					" from seller " + seller);
		
		SerializableRefuseOffer dropOffer= new SerializableRefuseOffer();
		
		dropOffer.userName = seller;
		dropOffer.serviceName = offer;
		dropOffer.commandInfo.add(buyer);
		
		netClient.sendData(dropOffer);
		
	}
	
	@Override
	public void makeOffer(String seller, String serviceName, String buyer, String price) {
		loggerNetwork.info("Seller " + seller + " made an offer to " + buyer +
						" for service " + serviceName);
		
		SerializableMakeOffer makeOffer = new SerializableMakeOffer();
		
		makeOffer.userName = seller;
		makeOffer.serviceName = serviceName;
		
		// in the auxiliary list add the buyer's name first and
		// the price second
		makeOffer.commandInfo.add(buyer);
		makeOffer.commandInfo.add(price);
		
		netClient.sendData(makeOffer);
	}

	@Override
	public void sendOfferExceeded(List<String> sellers, String serviceName, String buyer) {
		loggerNetwork.info("Seller send offer exceede to " + buyer + " for service " + 
						serviceName);
		
		SerializableOfferExceeded offerExceeded = new SerializableOfferExceeded();
		
		offerExceeded.userName = buyer;
		offerExceeded.serviceName = serviceName;
		offerExceeded.commandInfo = sellers;
		
		netClient.sendData(offerExceeded);
	}
	
	@Override
	public void dropAuction(String userName, String serviceName, String seller) {
		loggerNetwork.info("Seller " + seller + " drops auction for service " + 
				serviceName);
		
		SerializableDropAuction dropAuction = new SerializableDropAuction();
		
		dropAuction.userName = userName;
		dropAuction.serviceName = serviceName;
		dropAuction.commandInfo.add(seller);
		
		netClient.sendData(dropAuction);
	}

	@Override
	public void logInUser(String username, String password, String userType,
			List<String> serviceList) {

		UserPacket usrPack = new UserPacket();
		usrPack.username = username;
		usrPack.password = password;
		usrPack.userType = userType;
		usrPack.toDelete = 0;

		netClient.sendData(usrPack);
	}

	
	/* Methods that handle server response */
	
	@Override
	public void recvLaunchOfferReq(String userName, String serviceName) {
		loggerNetwork.info("received launch offer feedback");
		
		med.recvLaunchOfferReq(userName, serviceName);
	}

	@Override
	public void recvDropOfferReq(String buyer, String serviceName) {
		med.recvDropOfferReq(buyer, serviceName);
	}

	@Override
	public void recvRefuseOffer(String buyer, String serviceName) {
		loggerNetwork.info("received refuse offer feedback");
		
		med.recvRefuseOffer(buyer, serviceName);
	}

	@Override
	public void recvAcceptOffer(String buyer, String serviceName, String seller) {
		loggerNetwork.info("received accept offer feedback");
		
		med.recvAcceptOffer(buyer, serviceName, seller);
	}

	@Override
	public void recvMakeOffer(String seller, String serviceName, String price) {
		loggerNetwork.info("received make offer feedback");
		
		// update the seller's status
		med.recvMakeOffer(serviceName, seller, price);
	}

	@Override
	public void recvDropAuction(String seller, String serviceName) {
		loggerNetwork.info("received drop auction feedback");
		med.recvDropAuction(seller, serviceName);
	}

	@Override
	public void sendFileToBuyer(String buyer, String serviceName, String seller) {
		loggerNetwork.info("send file to buyer");
		
		long length;
		int offset, numRead;
		
		byte[] fileBytes;
		
		String filename = serviceName;
		File verifyFile = new File(filename); 
		InputStream serviceFile = null;
		
		try {
			serviceFile = new FileInputStream(filename);
		} catch (FileNotFoundException e1) {
			System.err.println("Error opening file's inputStream");
			e1.printStackTrace();
		}
		
		if (!verifyFile.exists()) {
			System.err.println("File doesn't exist");
			return;
		}
		
		//verify length of the file
		length = verifyFile.length();
		if(length > Integer.MAX_VALUE) {
			loggerNetwork.error("File is too large");
		}
		
		fileBytes = new byte[(int)length];
		offset = 0;
		
		//read the file into a byte array
		try {
			while (offset < fileBytes.length && 
					(numRead = serviceFile.read(fileBytes, offset, fileBytes.length - offset) ) >= 0) {
				offset += numRead;
			}
		} catch (IOException e) {
			System.err.println("Error reading input file");
			e.printStackTrace();
		}
		
		
		//put the byte into an object and send it
		FileService fileObj = new FileService();
		fileObj.fileContent = fileBytes;
		fileObj.fileName = filename;
		fileObj.buyer = buyer;
		fileObj.seller = seller;
		fileObj.serviceName = serviceName;
		
		med.startFileTransfer(serviceName, buyer);

		netClient.sendData(fileObj);

	}

	@Override
	public void recvFileTransfer(String seller, String serviceName, byte[] fileContent) {
		loggerNetwork.info("Receive file from seller " + seller + 
				" from service " + serviceName);
		
		String filename = seller + "_" + serviceName;
		int toWrite;
		int noChunks = fileContent.length/NetworkInfo.BUF_SIZE;
		int chunkSize = fileContent.length/noChunks;
		int offset = 0;
		
		try {
			File outFile = new File(filename);
			if (outFile.exists()) {
				outFile.createNewFile();
			}
			
			// write the byte stream into memory
			FileOutputStream receivedFile = new FileOutputStream(filename);

			for (; offset !=  fileContent.length; ){
				if ( fileContent.length - offset < chunkSize) 
					toWrite = fileContent.length - offset;
				else
					toWrite = chunkSize;
				receivedFile.write(fileContent, offset, toWrite);
				
				offset += toWrite;
				
				//announce the gui about the progress of writing the file
				med.acceptFileTransfer(serviceName, 100 * offset / fileContent.length);
			}
			
			receivedFile.close();

		} catch (FileNotFoundException e) {
			System.err.println("Error opening outputstream");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error writing file on disk");
			e.printStackTrace();
		}
	}

	@Override
	public void recvOfferExceeded(String userName, String serviceName) {
		loggerNetwork.info("Received offer exceeded feedback from " + userName + 
				" for service " + serviceName);
		med.recvOfferExceeded(userName, serviceName);
	}

	@Override
	public void transferFailed(String seller, String serviceName, String buyer) {
		loggerNetwork.info("Transfer of service " + serviceName +
				"from seller " + seller + " to buyer " + buyer );
		
		SerializableTransferFailed transferFailed = new SerializableTransferFailed();
		transferFailed.userName = buyer;
		transferFailed.serviceName = serviceName;
		transferFailed.commandInfo.add(seller);
		
		netClient.sendData(transferFailed);
	}

	@Override
	public void recvTransferFailed(String buyer, String serviceName) {
		loggerNetwork.info("Transfer of service " + serviceName +
				"from seller to buyer " + buyer + " failed");
		
		med.recvTransferFailed(buyer, serviceName);
	}
	
}
