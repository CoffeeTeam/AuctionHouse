package commands;

import mediator.IMediatorNetwork;

public class AcceptOffer extends Command {

	public AcceptOffer(IMediatorNetwork med){
		this.medNetwork = med;
	}
	
	@Override
	public void execute(String serviceName, String user) {
		// TODO Auto-generated method stub
		
	}

}
