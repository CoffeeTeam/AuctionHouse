package commands;

import mediator.IMediatorNetwork;

public class DropOfferReq extends Command {

	public DropOfferReq(IMediatorNetwork med) {
		this.medNetwork = med;
	}
	
	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		this.medNetwork.dropOfferRequest(serviceName, user);
	}

}
