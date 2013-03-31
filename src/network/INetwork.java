package network;

import java.util.HashMap;
import java.util.List;

import mediator.IMediatorNetwork;

public abstract class INetwork {

	public IMediatorNetwork med;
	public HashMap<String, String> pairServiceStatus;
	
	public abstract void launchOfferReq(String serviceName);
	public abstract void acceptOffer(String seller, String offer);
	public abstract void refuseOffer(String seller, String offer);

}
