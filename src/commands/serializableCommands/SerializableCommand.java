package commands.serializableCommands;

import java.io.Serializable;

import mediator.IMediatorNetwork;

public abstract class SerializableCommand implements Serializable{

	private static final long serialVersionUID = 612696483138661950L;

	public IMediatorNetwork medNetwork;
}
