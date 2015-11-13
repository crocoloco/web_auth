package sample.webauth.service.authentication;

import java.util.List;

/**
 * User authentication data
 *
 */
public class AuthenticationTicket
{
	private String userName;
	private List<String> roles;

	public AuthenticationTicket(String userName, List<String> roles) {
		this.userName = userName;
		this.roles = roles;
	}

	// get the name that identifies the user
	public String getUserName() {
		return this.userName;
	}

	// get the user roles
	public List<String> getRoles() {
		return this.roles;
	}
}	
