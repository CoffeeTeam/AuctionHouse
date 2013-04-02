package commands;

import mediator.IMediatorNetwork;

public class MakeOffer extends Command{

	public MakeOffer(IMediatorNetwork med) {
		this.medNetwork = med;
	}

	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		this.medNetwork.makeOffer(serviceName, user);
	}

}
