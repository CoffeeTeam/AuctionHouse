package commands;

import commands.serializableCommands.SerializableDropAuction;

import mediator.IMediatorNetwork;

@SuppressWarnings("serial")
public class DropAuction extends SerializableDropAuction implements Command {

	public DropAuction(IMediatorNetwork med) {
		this.medNetwork = med;
	}
	
	@Override
	public void execute(String serviceName, String user, String... auxUserInfo) {
		// TODO Auto-generated method stub
		
	}

}
