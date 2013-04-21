package states;

import java.util.List;

import commands.Command;
import commands.DropOfferReq;
import commands.LaunchOfferReq;

import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class BuyerState extends State {

	Command launchOfferReq;
	Command dropOfferReq;

	public BuyerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;
		
		launchOfferReq = new LaunchOfferReq(medNetwork);
		dropOfferReq = new DropOfferReq(medNetwork);
	}

	@Override
	public List<String> getServiceList(String userName) {
		// TODO Auto-generated method stub
		return this.medWS.getServiceList(userName, UserTypes.buyer);
	}

	@Override
	public void launchService(String serviceName, String userName) {
		// TODO Auto-generated method stub
		launchOfferReq.execute(serviceName, userName);
	}

	@Override
	public void dropService(String serviceName, String userName) {
		// TODO Auto-generated method stub
		dropOfferReq.execute(serviceName, userName);
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
	public void refuseOffer(String seller, String serviceName, String buyer) {
		this.medNetwork.refuseOfferNet(seller, serviceName, buyer);
	}

}
