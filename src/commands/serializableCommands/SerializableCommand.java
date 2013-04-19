package commands.serializableCommands;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import mediator.IMediatorNetwork;

public abstract class SerializableCommand implements Serializable{

	private static final long serialVersionUID = 612696483138661950L;

	public transient IMediatorNetwork medNetwork;
	
	public SerializableCommand() {
		commandInfo = new LinkedList<String>();
	}

	public String userName;
	public String serviceName;
	public List<String> commandInfo;
}
