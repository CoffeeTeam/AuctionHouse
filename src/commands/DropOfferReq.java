package commands;

import commands.serializableCommands.SerializableDropOfferReq;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class DropOfferReq extends SerializableDropOfferReq implements Command {

	public DropOfferReq(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user, String... auxUserInfo) {
		this.medNetwork.dropOfferRequestNet(serviceName, user);
	}

}
