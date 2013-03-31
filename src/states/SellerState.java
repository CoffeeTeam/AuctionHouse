package states;

import java.util.List;

import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class SellerState extends State {

	public SellerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;
	}

	@Override
	public List<String> getServiceList(String userName) {
		// TODO Auto-generated method stub
		return medWS.getServiceList(userName, UserTypes.seller);
	}

	@Override
	public void launchService(String serviceName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropService(String serviceName) {
		// TODO Auto-generated method stub

	}

	public void acceptOffer(String seller, String serviceName) {
		
	}

	public void refuseOffer(String seller, String serviceName) {

	}

	@Override
	public List<String> getCurrentUsers(String serviceName) {
		// TODO Auto-generated method stub
		return this.medWS.getUserList(serviceName, UserTypes.seller);
	}

	
}
