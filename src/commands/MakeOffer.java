package commands;

import commands.serializableCommands.SerializableMakeOffer;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class MakeOffer extends SerializableMakeOffer implements Command{

	public MakeOffer(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user, String... auxUserInfo) {
		if (auxUserInfo.length != 1) {
			System.err.println("[Make Offer][execute] Third parameter needed to store " +
					"buyer's name!!");
			return;
		}
		
		this.medNetwork.makeOfferNet(user, serviceName, auxUserInfo[0]);
	}

}
