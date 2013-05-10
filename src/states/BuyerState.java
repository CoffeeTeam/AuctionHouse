package states;

import java.util.List;

import commands.AcceptOffer;
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
	Command acceptOffer;

	public BuyerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;
		
		launchOfferReq = new LaunchOfferReq(medNetwork);
		dropOfferReq = new DropOfferReq(medNetwork);
		refuseOffer = new RefuseOffer(medNetwork);
		acceptOffer = new AcceptOffer(medNetwork);
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
	public void dropService(String serviceName, String userName, String... auxInfo) {
		dropOfferReq.execute(serviceName, userName, auxInfo[0]);
	}

	@Override
	public List<String> getCurrentUsers(String serviceName) {
		return this.medWS.getUserList(serviceName, UserTypes.buyer);
	}

	@Override
	public void acceptOffer(String buyer, String serviceName, String seller, List<String> otherSellers) {
		/* on the first position will be the name of the seller who's offer was accepted
		 * and after will be the name of the sellers which which will be refused
		 */
		String allSellers  = new String(seller);
		for (String refusedSeller : otherSellers) {
			allSellers += " " + refusedSeller;
		}
		
		acceptOffer.execute(buyer, serviceName, allSellers);
	}

	@Override
	public void refuseOffer(String seller, String serviceName, String buyer) {
		refuseOffer.execute(serviceName, seller, buyer);
	}

}
