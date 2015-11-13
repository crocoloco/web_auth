package sample.webauth.service.authentication.http;

/**
 * Authentication manager options
 *
 */
public class AuthenticationOptions
{
	private int sessionExpirationInSeconds;
	private String sessionCookieName;
	private String loginPath;

	public AuthenticationOptions(String loginPath, int sessionExpirationInSeconds,
		String sessionCookieName) {

		this.loginPath = loginPath;
		this.sessionExpirationInSeconds = sessionExpirationInSeconds;
		this.sessionCookieName = sessionCookieName;
	}

	// get the path of the login page
	public String getLoginPath() {
		return this.loginPath;
	}

	// get the maximum time of session inactivity in seconds for the session to expire
	public int getSessionExpirationInSeconds() {
		return this.sessionExpirationInSeconds;
	}
	
	// get the name of the cookie session
	public String getSessionCookieName() {
		return this.sessionCookieName;
	}
}	
