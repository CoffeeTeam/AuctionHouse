package states;

import java.util.List;

import commands.Command;
import commands.DropOfferReq;
import commands.LaunchOfferReq;
import commands.RefuseOffer;

import constants.UserTypes;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class BuyerState extends State {

	Command launchOfferReq;
	Command dropOfferReq;
	Command refuseOffer;

	public BuyerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;
		
		launchOfferReq = new LaunchOfferReq(medNetwork);
		dropOfferReq = new DropOfferReq(medNetwork);
		refuseOffer = new RefuseOffer(medNetwork);
	}

	@Override
	public List<String> getServiceList(String userName) {
		return this.medWS.getServiceList(userName, UserTypes.buyer);
	}

	@Override
	public void launchService(String serviceName, String userName, String... auxUserInfo) {
		launchOfferReq.execute(serviceName, userName);
	}

	@Override
	public void dropService(String serviceName, String userName) {
		dropOfferReq.execute(serviceName, userName);
	}

	@Override
	public List<String> getCurrentUsers(String serviceName) {
		return this.medWS.getUserList(serviceName, UserTypes.buyer);
	}

	@Override
	public void acceptOffer(String seller, String serviceName) {
		this.medNetwork.acceptOfferNet(seller, serviceName);
	}

	@Override
	public void refuseOffer(String seller, String serviceName, String buyer) {
		refuseOffer.execute(serviceName, seller, buyer);
	}

}
