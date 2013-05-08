package web_service;

import java.util.List;

import util.DataGenerator;
import util.FileInfoRetriever;

import mediator.IMediatorWSClient;

public class WSClient extends IWSClient{
	
	private final String dbName = "ClientList";
	private FileInfoRetriever infoRetriever;

	public WSClient(IMediatorWSClient med){
		this.med = med;
		infoRetriever = new FileInfoRetriever(dbName);
	}
	
	/**
	 * Retrieves the users that offer/request a certain service
	 */
	@Override
	public List<String> getCurrentUsers(String serviceName, String userType) {
		return infoRetriever.getUserList(serviceName, userType);
	}

	@Override
	public List<String> getServiceList(String userName, String userType) {
		return infoRetriever.userService(userName, userType);
	}

	@Override
	public boolean callLogIn(String username, String password, String type) {
	/*	
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		Call call = (Call)service.createCall();
		
	*/	// TODO => communicate with server
		return false;
	}

	@Override
	public void callLogOut(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> callUserServices(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> callGetUsersForService(String serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void callChangeOfferStatus(String username, String status,
			String servicename) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserServicePrice(String seller, String serviceName) {
		// TODO => get price from database, not from dummy generator
		return DataGenerator.getPrice(serviceName, seller).toString();
	}

}
