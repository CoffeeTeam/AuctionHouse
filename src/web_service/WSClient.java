package web_service;

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import util.DataGenerator;
import util.FileInfoRetriever;

import mediator.IMediatorWSClient;

public class WSClient extends IWSClient{
	
	private final String dbName = "ClientList";
	private FileInfoRetriever infoRetriever;
	
	private static final String SERVICES_URL =
			"http://localhost:8080/axis/services/WS_DBConnect?wsdl";
	private static final String LOG_IN 					= "logIn";
	private static final String USER_SERVICES 			= "userServices";
	private static final String GET_USERS_FOR_SERVICE	= "getUsersForService";
	private static final String LOG_OUT					= "logOut";
	private static final String CHANGE_OFFER_STATUS		= "changeOfferStatus";
	private static final String GET_PRICE				= "getPrice";
	
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
		Boolean result = false;
		
		try {
			Service	service = new Service();
			Call call = (Call)service.createCall();
			QName string = new QName("http://echo.demo.oracle/", "string");
			Object[] inParams = new Object[]{username, password, type};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			call.setOperationName(new QName(LOG_IN));
			
			call.addParameter("username", string, String.class, ParameterMode.IN);
			call.addParameter("password", string, String.class, ParameterMode.IN);
			call.addParameter("type", string, String.class, ParameterMode.IN);
			call.setReturnClass(Boolean.class);
			
			result = (Boolean) call.invoke(inParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
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
