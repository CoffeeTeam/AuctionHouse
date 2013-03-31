package commands;

import mediator.IMediatorNetwork;

public abstract class Command {
	
	public IMediatorNetwork medNetwork;

	public abstract void execute(String serviceName, String user);
}
