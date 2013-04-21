package commands;

import commands.serializableCommands.SerializableRefuseOffer;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class RefuseOffer extends SerializableRefuseOffer implements Command {

	public RefuseOffer(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user, String... auxUserInfo) {
		if (auxUserInfo.length != 1) {
			System.err.println("[Refuse Offer][execute] Third parameter needed to store " +
					"buyer's name!!");
			return;
		}

		this.medNetwork.refuseOfferNet(user, serviceName, auxUserInfo[0]);
	}

}
