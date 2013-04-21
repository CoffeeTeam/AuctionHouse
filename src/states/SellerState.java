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
		return medWS.getServiceList(userName, UserTypes.seller);
	}

	@Override
	public void launchService(String serviceName, String userName,
			String... auxUserInfo) {
		if (auxUserInfo.length == 0) {
			System.err
					.println("[Seller state] This function should have an extra-parameter"
							+ " containing the name of the seller");
		}
		makeOffer.execute(serviceName, userName, auxUserInfo);
	}

	@Override
	public void dropService(String serviceName, String userName) {
		dropAuction.execute(serviceName, userName);
	}

	public void acceptOffer(String seller, String serviceName) {

	}

	public void refuseOffer(String seller, String serviceName, String buyer) {
		// does nothing for this state
		return;
	}

	@Override
	public List<String> getCurrentUsers(String serviceName) {
		return this.medWS.getUserList(serviceName, UserTypes.seller);
	}

}
