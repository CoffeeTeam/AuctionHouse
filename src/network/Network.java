package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import mediator.IMediatorNetwork;

import commands.serializableCommands.SerializableAcceptOffer;
import commands.serializableCommands.SerializableDropAuction;
import commands.serializableCommands.SerializableDropOfferReq;
import commands.serializableCommands.SerializableLaunchOfferReq;
import commands.serializableCommands.SerializableMakeOffer;
import commands.serializableCommands.SerializableRefuseOffer;
import constants.Sizes;

import user.UserPacket;
import util.FileService;

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
	public void dropOfferReq(String buyer, String serviceName, List<String> sellers) {
		SerializableDropOfferReq dropOffer = new SerializableDropOfferReq();
		
		dropOffer.userName = buyer;
		dropOffer.serviceName = serviceName;
		dropOffer.commandInfo = sellers;
		
		netClient.sendData(dropOffer);
	}

	@Override
	public void acceptOffer(String buyer, String offer, String sellers) {
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
		SerializableRefuseOffer dropOffer= new SerializableRefuseOffer();
		
		dropOffer.userName = seller;
		dropOffer.serviceName = offer;
		dropOffer.commandInfo.add(buyer);
		
		netClient.sendData(dropOffer);
		
		System.out.println("[Network] " + buyer + " sent 'refuse offer' for " +
				"service " + offer + " to " + seller);
	}
	
	@Override
	public void makeOffer(String seller, String serviceName, String buyer) {
		SerializableMakeOffer makeOffer = new SerializableMakeOffer();
		
		makeOffer.userName = seller;
		makeOffer.serviceName = serviceName;
		// in the auxiliary list add the buyer's name first and
		// the price second
		makeOffer.commandInfo.add(buyer);
		makeOffer.commandInfo.add(new Integer(
				new Random().nextInt(Sizes.maxPrice)).toString());
		
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
		System.out.println("Network => received launch offer feedback");
		
		med.recvLaunchOfferReq(userName, serviceName);
	}

	@Override
	public void recvDropOfferReq(String buyer, String serviceName) {
		med.recvDropOfferReq(buyer, serviceName);
	}

	@Override
	public void recvRefuseOffer(String buyer, String serviceName) {
		System.out.println("[Network] => received refuse offer feedback");
		
		med.recvRefuseOffer(buyer, serviceName);
	}

	@Override
	public void recvAcceptOffer(String buyer, String serviceName, String seller) {
		System.out.println("[Network] => received accept offer feedback");
		
		med.recvAcceptOffer(buyer, serviceName, seller);
	}

	@Override
	public void recvMakeOffer(String seller, String serviceName, String price) {
		System.out.println("Network => received make offer feedback");
		
		med.recvMakeOffer(serviceName, seller, price);
	}

	@Override
	public void recvDropAuction(String seller, String serviceName) {
		
	}

	@Override
	public void sendFileToBuyer(String buyer, String serviceName, String seller) {
		System.out.println("Network => send file to buyer");
		
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
			System.err.println("File is too large");
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
		
		System.out.println("[ClientUtils] Send file to the buyer");
		
		//put the byte into an object and send it
		FileService fileObj = new FileService();
		fileObj.fileContent = fileBytes;
		fileObj.fileName = filename;
		fileObj.buyer = buyer;
		fileObj.seller = seller;
		fileObj.serviceName = serviceName;
		
		netClient.sendData(fileObj);
		
	}

	@Override
	public void recvFileTransfer(String seller, String serviceName, byte[] fileContent) {
		String filename = seller + "_" + serviceName;
		int toWrite;
		int chunkSize = fileContent.length/Sizes.noChunks;
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
	
}
