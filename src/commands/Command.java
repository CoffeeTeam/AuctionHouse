package commands;


public interface Command {

	public abstract void execute(String serviceName, String user, String... auxUserInfo);
}
