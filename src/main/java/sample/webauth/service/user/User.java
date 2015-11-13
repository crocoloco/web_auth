package sample.webauth.service.user;

import java.util.List;

/**
 * User entity
 *
 */
public class User
{
	private long id;
	private String userName;
	private String hashedPassword;
	private List<String> roles;

	public User(long id, String userName, String hashedPassword, List<String> roles) {
		this.id = id;
		this.userName = userName;
		this.hashedPassword = hashedPassword;
		this.roles = roles;
	}

	// get user identifier
	public long getId() {
		return this.id;
	}

	// get the name that identifies the user
	public String getUserName() {
		return this.userName;
	}

	// get the user hashed password
	public String getHashedPassword() {
		return this.hashedPassword;
	}

	public List<String> getRoles() {
		return this.roles;
	}
}	
