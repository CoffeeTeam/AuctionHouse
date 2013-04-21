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
		// TODO Auto-generated method stub
		
	}

}
