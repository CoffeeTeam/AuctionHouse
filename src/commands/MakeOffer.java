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
		this.medNetwork.makeOfferNet(serviceName, user);
	}

}
