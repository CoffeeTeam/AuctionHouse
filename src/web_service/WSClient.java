package web_service;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.rmi.RemoteException;


import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import util.FileInfoRetriever;

import mediator.IMediatorWSClient;

public class WSClient extends IWSClient{
	
	private final String dbName = "ClientList";

	@Deprecated // was used instead of database in project's early stages
	private FileInfoRetriever infoRetriever;
	
	private static final String SERVICES_URL =
			"http://localhost:8080/axis/services/WS_DBConnect?wsdl";
	private static final String LOG_IN 					= "logIn";
	private static final String USER_SERVICES 			= "userServices";
	private static final String GET_USERS_FOR_SERVICE	= "getUsersForService";
	private static final String LOG_OUT					= "logOut";
	private static final String CHANGE_OFFER_STATUS		= "changeOfferStatus";
	private static final String GET_PRICE				= "getPrice";
	private static final Integer MAX_SIZE				= 50;
	
	private QName string = new QName("http://echo.demo.oracle/", "string");

	public WSClient(IMediatorWSClient med){
		this.med = med;
		infoRetriever = new FileInfoRetriever(dbName);
	}

	@Override
	public boolean callLogIn(String username, String password, String type) {
		Boolean result = false;
		
		try {
			Service	service = new Service();
			Call call = (Call)service.createCall();
			Object[] inParams = {username, password, type};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			call.setOperationName(new QName(LOG_IN));
			
			call.addParameter("username", string, String.class, ParameterMode.IN);
			call.addParameter("password", string, String.class, ParameterMode.IN);
			call.addParameter("type", string, String.class, ParameterMode.IN);
			call.setReturnClass(Boolean.class);
			
			result = (Boolean) call.invoke(inParams);
			
			System.out.println("[WSClient][callLogIn] Received result is " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public void callLogOut(String username) {
		Service service = new Service();
		Call call;
		try {
			call = (Call)service.createCall();
			Object[] inParams = new Object[]{username};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			
			call.setTargetEndpointAddress(SERVICES_URL);
			call.setOperationName(new QName(LOG_OUT));
			call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, "");
			
			call.addParameter("username", string, String.class, ParameterMode.IN);
			
			call.setReturnClass(void.class);
			
			call.invoke(inParams);
			
		} catch (ServiceException e) {
			System.out.println("Error calling logout");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> callUserServices(String username) {
		List<String> services = null;

		System.out.println("[WSClient][callUserServices] Username is "+username);
		
		try {
			Service	service = new Service();
			Call call = (Call)service.createCall();
			QName string = new QName("http://echo.demo.oracle/", "string");
			Object[] inParams = {username};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			call.setOperationName(new QName(USER_SERVICES));
			
			call.addParameter("username", string, String.class, ParameterMode.IN);
			call.setReturnClass(String[].class);
			
			System.out.println("Call object is "+call+", in params are "+inParams);
			
			Object result = call.invoke(inParams);
			
			if (result == null)
				return null;
			
			services = Arrays.asList((String[]) result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return services;
	}

	private List<String> convertToList(String[] users) {
		List<String> usersList = new ArrayList<String>();
		
		for (int i = 0; i < users.length; ++i)
			usersList.add(users[i]);
		
		return usersList;
	}
	
	@Override
	public List<String> callGetUsersForService(String serviceName) {
		String[] sellerUsers = new String[MAX_SIZE];
		try {
			Service	service = new Service();
			Call call = (Call)service.createCall();
			Object[] inParams = new Object[]{serviceName};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			call.setOperationName(new QName(GET_USERS_FOR_SERVICE));
			
			call.addParameter("servicename", string, String.class, ParameterMode.IN);
			call.setReturnClass(String[].class);
			
			sellerUsers = (String[]) call.invoke(inParams);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return convertToList(sellerUsers);
	}

	@Override
	public void callChangeOfferStatus(String username, String status,
			String servicename) {
		
		Service service = new Service();
		Call call;
		try {
			call = (Call)service.createCall();
			Object[] inParams = new Object[]{username, status, servicename};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			
			call.setTargetEndpointAddress(SERVICES_URL);
			call.setOperationName(new QName(CHANGE_OFFER_STATUS));
			call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, "");
			
			call.addParameter("username", string, String.class, ParameterMode.IN);
			call.addParameter("status", string, String.class, ParameterMode.IN);
			call.addParameter("servicename", string, String.class, ParameterMode.IN);
			
			call.setReturnClass(void.class);
			
			call.invoke(inParams);
			
		} catch (ServiceException e) {
			System.out.println("Error calling change offer request");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getUserServicePrice(String seller, String serviceName) {
		String priceValue = "0";
		
		Service service = new Service();
		Call call;
		try {
			call = (Call)service.createCall();
			Object[] inParams = new Object[]{seller, serviceName};
			
			call.setTargetEndpointAddress(new URL(SERVICES_URL));
			
			call.setTargetEndpointAddress(SERVICES_URL);
			call.setOperationName(new QName(GET_PRICE));
			call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY, "");
			
			call.addParameter("seller", string, String.class, ParameterMode.IN);
			call.addParameter("serviceName", string, String.class, ParameterMode.IN);
			
			call.setReturnClass(String.class);
			
			priceValue = (String)call.invoke(inParams);
			
		} catch (ServiceException e) {
			System.out.println("Error calling user service price");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return priceValue;
	}

}
