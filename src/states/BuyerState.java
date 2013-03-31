package states;

import java.util.List;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public class BuyerState extends State {

	public BuyerState(IMediatorGUI medGUI, IMediatorWSClient medWS,
			IMediatorNetwork medNetwork) {
		this.medGUI = medGUI;
		this.medNetwork = medNetwork;
		this.medWS = medWS;

	}

	@Override
	public List<String> getServiceList(String userName) {
		// TODO Auto-generated method stub
		return this.medWS.getServiceList(userName);
	}

	@Override
	public void launchService(String serviceName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropService(String serviceName) {
		// TODO Auto-generated method stub

	}

}
