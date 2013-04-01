package commands;

import mediator.IMediatorNetwork;

public class DropAuction extends Command {

	public DropAuction(IMediatorNetwork med) {
		this.medNetwork = med;
	}
	
	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		medNetwork.dropAuction(serviceName, user);
	}

}
