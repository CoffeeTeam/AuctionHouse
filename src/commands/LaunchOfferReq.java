package commands;

import mediator.IMediatorNetwork;

public class LaunchOfferReq extends Command {

	public LaunchOfferReq(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		this.medNetwork.launchOfferRequest(serviceName, user);
	}
	

}
