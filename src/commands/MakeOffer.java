package commands;

import commands.serializableCommands.SerializableMakeOffer;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class MakeOffer extends SerializableMakeOffer implements Command{

	public MakeOffer(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		this.medNetwork.makeOffer(serviceName, user);
	}

}
