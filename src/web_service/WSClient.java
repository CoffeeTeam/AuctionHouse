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
	
	@Override
	public List<String> getCurrentUsers(String serviceName, String userType) {
		// TODO Auto-generated method stub
		return infoRetriever.getUserList(serviceName, userType);
	}

	@Override
	public List<String> getServiceList(String userName) {
		// TODO Auto-generated method stub
		return infoRetriever.userService(userName);
	}

}
