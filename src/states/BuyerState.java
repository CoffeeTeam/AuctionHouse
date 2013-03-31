package states;

import java.util.List;

import commands.Command;
import commands.LaunchOfferReq;

import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class BuyerState extends State {

	Command launchOfferReq;
	public BuyerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;
		
		launchOfferReq = new LaunchOfferReq(medNetwork);

	}

	@Override
	public List<String> getServiceList(String userName) {
		// TODO Auto-generated method stub
		return this.medWS.getServiceList(userName, UserTypes.buyer);
	}

	@Override
	public void launchService(String serviceName) {
		// TODO Auto-generated method stub
		launchOfferReq.execute(serviceName, null);
	}

	@Override
	public void dropService(String serviceName) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getCurrentUsers(String serviceName) {
		// TODO Auto-generated method stub
		return this.medWS.getUserList(serviceName, UserTypes.buyer);
	}

	@Override
	public void acceptOffer(String seller, String serviceName) {
		// TODO Auto-generated method stub
		this.medNetwork.acceptOfferNet(seller, serviceName);
	}

	@Override
	public void refuseOffer(String seller, String serviceName) {
		// TODO Auto-generated method stub
		this.medNetwork.refuseOfferNet(seller, serviceName);
		
	}

}
