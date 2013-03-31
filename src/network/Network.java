package network;

import java.util.HashMap;

import constants.StatusMessages;

public class Network extends INetwork{

	public Network() {
		pairServiceStatus = new HashMap<String,String>();
	}

	@Override
	public void launchOfferReq(String serviceName) {
		// TODO Auto-generated method stub
		pairServiceStatus.put(serviceName, StatusMessages.noOffer);
	}

	@Override
	public void acceptOffer(String seller, String offer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refuseOffer(String seller, String offer) {
		// TODO Auto-generated method stub
		
	}
}
