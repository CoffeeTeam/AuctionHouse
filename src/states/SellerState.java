package states;

import java.util.List;

import commands.Command;
import commands.DropAuction;
import commands.MakeOffer;

import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class SellerState extends State {

	Command makeOffer;
	Command dropAuction;
	public SellerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;
		this.makeOffer = new MakeOffer(medNetwork);
		this.dropAuction = new DropAuction(medNetwork);
	}

	@Override
	public List<String> getServiceList(String userName) {
		// TODO Auto-generated method stub
		return medWS.getServiceList(userName, UserTypes.seller);
	}

	@Override
	public void launchService(String serviceName, String userName) {
		// TODO Auto-generated method stub
		makeOffer.execute(serviceName, userName);
	}

	@Override
	public void dropService(String serviceName, String userName) {
		// TODO Auto-generated method stub
		dropAuction.execute(serviceName, userName);
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
