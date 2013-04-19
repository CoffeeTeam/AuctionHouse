package commands;

import commands.serializableCommands.SerializableLaunchOfferReq;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class LaunchOfferReq extends SerializableLaunchOfferReq implements Command {

	public LaunchOfferReq(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		this.medNetwork.launchOfferRequest(serviceName, user);
	}
	

}
