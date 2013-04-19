package user;

import java.io.Serializable;

public class UserPackage implements Serializable{

	private static final long serialVersionUID = -71823579446174302L;

	public String username;
	public String password;
	public String userType;
	public boolean toDelete;

}
