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
	 * Get the list of users which provide/or wish a certain service
	 * @param serviceName 
	 * @return list of users
	 */
	public abstract List<String> getCurrentUsers(String serviceName);

	/**
	 * Invoked when the buyer wants to take an offer or when a seller makes an
	 * offer to a buyer
	 * 
	 * @param serviceName
	 *            the name of the offered service
	 */
	public abstract void launchService(String serviceName, String userName, String... auxUserInfo);

	/**
	 * Invoked when the buyer wants to drop an offer that he received or when a
	 * seller wants to drop an auction
	 * 
	 * @param serviceName
	 *            the name of the service for which the actions are made
	 * @param auxInfo TODO
	 */
	public abstract void dropService(String serviceName, String userName, String... auxInfo);

	/**
	 * Used by the buyer to accept an offer
	 * 
	 * @param serviceName
	 *            the name of the service
	 * @param seller
	 *            the name of the person who provides the service
	 */
	public abstract void acceptOffer(String buyer, String serviceName, String seller, List<String> otherSellers);

	/**
	 * The buyer drops the offer of a seller
	 * @param seller
	 *            the person which provides the service
	 * @param serviceName
	 *            name of the service to be dropped
	 * @param buyer TODO
	 */
	public abstract void refuseOffer(String seller, String serviceName, String buyer);
}
