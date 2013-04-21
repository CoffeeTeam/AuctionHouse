package web_service;

import java.util.List;

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

}
