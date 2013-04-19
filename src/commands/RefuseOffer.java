package commands;

import commands.serializableCommands.SerializableRefuseOffer;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class RefuseOffer extends SerializableRefuseOffer implements Command {

	public RefuseOffer(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		
	}

}
