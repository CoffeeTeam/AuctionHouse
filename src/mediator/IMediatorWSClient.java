package mediator;

import java.util.List;

public interface IMediatorWSClient {

	public List<String> getServiceList(String userName);
	public List<String> getUserList(String serviceName, String userType);
}
