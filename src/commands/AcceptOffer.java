package commands;

import commands.serializableCommands.SerializableAcceptOffer;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class AcceptOffer extends SerializableAcceptOffer implements Command {

	public AcceptOffer(IMediatorNetwork med){
		this.medNetwork = med;
	}
	
	@Override
	public void execute(String serviceName, String user, String... auxUserInfo) {
		if (0 == auxUserInfo.length) {
			System.err.println("[Accept Offer] the last argument should contain " 
					+ " info about the accepted and refused sellers");
			return;
		}
		
		this.medNetwork.acceptOfferNet(serviceName, user, auxUserInfo[0]);
	}

}
