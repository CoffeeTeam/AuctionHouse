package states;

import java.util.List;

import mediator.IMediatorGUI;
import mediator.IMediatorNetwork;
import mediator.IMediatorWSClient;

public abstract class State {

	IMediatorGUI medGUI;
	IMediatorWSClient medWS;
	IMediatorNetwork medNetwork;

	/**
	 * Get the list of services offered/received by an entity
	 * 
	 * @return the list of services
	 */
	public abstract List<String> getServiceList(String userName);

	/**
	 * Invoked when the buyer wants to take an offer or when a seller makes an
	 * offer to a buyer
	 * 
	 * @param serviceName
	 *            the name of the offered service
	 */
	public abstract void launchService(String serviceName);

	/**
	 * Invoked when the buyer wants to drop an offer that he received or when a
	 * seller wants to drop an auction
	 * 
	 * @param serviceName
	 *            the name of the service for which the actions are made
	 */
	public abstract void dropService(String serviceName);

	/**
	 * Used by the buyer to accept an offer
	 * 
	 * @param serviceName
	 *            the name of the service
	 * @param seller
	 *            the name of the person who provides the service
	 */
	public void acceptOffer(String seller, String serviceName) {

	}

	/**
	 * The buyer drops the offer of a seller
	 * 
	 * @param serviceName
	 *            name of the service to be dropped
	 * @param seller
	 *            the person which provides the service
	 */
	public void refuseOffer(String seller, String serviceName) {

	}
}
