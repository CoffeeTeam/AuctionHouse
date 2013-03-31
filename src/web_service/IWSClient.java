package web_service;

import java.util.List;

import mediator.IMediatorWSClient;

public abstract class IWSClient {

	public IMediatorWSClient med;
	
	public abstract List<String> getCurrentUsers(String serviceName, String userType);
	public abstract List<String> getServiceList(String userName);
}
