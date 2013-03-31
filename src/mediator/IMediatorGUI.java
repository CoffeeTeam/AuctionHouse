package mediator;

import java.util.List;

public interface IMediatorGUI {

	/**
	 * Given the name and the password of the buyer the mediator returns the
	 * services that he is looking for
	 * @param name buyer's id
	 * @param passwd buyer's password
	 * @return list of services that he is interested in
	 */
	public List<String> logInBuyer(String name, String passwd);
	
	public List<String> logInSeller(String name, String passwd);
	
	public List<String> getUsers(String serviceName);
	
	public void launchService(String serviceName);
	public void dropService(String serviceName);
	public void acceptOfferGui(String seller, String offer);
	public void refuseOfferGui(String seller, String offer);
}
