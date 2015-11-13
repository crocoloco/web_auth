package sample.webauth.service.authentication.http;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.webauth.config.JsonConfiguration;
import sample.webauth.service.authentication.AuthenticationTicket;
import sample.webauth.service.authorization.IResourceAuthorizationService;
import sample.webauth.service.session.ISessionStore;
import sample.webauth.service.session.Session;
import sample.webauth.service.security.StrongIdGenerator;

/**
 * Authentication service that handles sessions and cookies
 *
 */
public class AuthenticationManager
{
	private static final Logger LOGGER = Logger.getLogger(AuthenticationManager.class.getName());	
	private AuthenticationOptions options; 
	private IResourceAuthorizationService resourceAuthSvc;
	private ISessionStore sessionStore;
	private StrongIdGenerator idGenerator;
	
	/*
		Constructor with dependencies
	*/
	public AuthenticationManager(AuthenticationOptions options, IResourceAuthorizationService resourceAuthSvc,
		ISessionStore sessionStore, StrongIdGenerator idGenerator) {

		this.options = options;
		this.resourceAuthSvc = resourceAuthSvc;
		this.sessionStore = sessionStore;
		this.idGenerator = idGenerator;
	}
	
	/*
		Sign in a user setting up an authentication session and sending a session cookie
	*/
	public void signIn(AuthenticationTicket ticket, HttpServletRequest request, HttpServletResponse response) throws IOException {

		// invalidate existing session to mitigate session fixation attack
		Session session = getRequestSession(request);
		if (session != null) {
			this.sessionStore.remove(session.getId());
		}

		createSession(ticket, response);
			
		// TODO: redirect to original page?
		// redirect to first authorized page
		List<String> resources = this.resourceAuthSvc.getAuthorizedResources(ticket.getRoles());
		if (! resources.isEmpty()) {
			response.sendRedirect(request.getContextPath() + resources.get(0));
		}

		LOGGER.info("User " + ticket.getUserName() + " signed in");
	}

	/*
		Sign out of the current session invalidating the session and the corresponding cookie
	*/
	public void signOut(HttpServletRequest request, HttpServletResponse response) throws IOException {

		invalidateSession(request, response);
		response.sendRedirect(request.getContextPath() + this.options.getLoginPath());
	}
	
	/*
		Return the user credentials related with the current session, if any
	*/
	public AuthenticationTicket authenticate(HttpServletRequest request, HttpServletResponse response) {

		Session session = getRequestSession(request);
		// no current session
		if (session == null) {
			return null;
		}

		if (invalidateExpiredSession(session, response)) {
			return null;
		}
		
		AuthenticationTicket ticket = (AuthenticationTicket)session.getData();
		if (ticket == null) {
			return null;
		}

		refreshSession(session, response);

		return ticket;
	}
	
	/*
		Send the user to the authentication page
	*/
	public void challengeUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// TODO: keep track of original requested url?
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.sendRedirect(request.getContextPath() + this.options.getLoginPath());
		LOGGER.info("User not authenticated. Redirecting to login page");
	}

	/*
		Obtain the current session, if any
	*/
	private Session getRequestSession(HttpServletRequest request) {

		Cookie cookie = getCookie(this.options.getSessionCookieName(), request);
		if (cookie == null) {
			return null;
		}
		String sessionId = cookie.getValue();

		return this.sessionStore.get(sessionId);
	}

	/*
		Create a new session with the given user credentials
	*/
	private Session createSession(AuthenticationTicket ticket, HttpServletResponse response) {

		// get a non existing session id
		String sessionId = null;
		while (sessionId == null) {
			sessionId = this.idGenerator.getId();
			if (this.sessionStore.get(sessionId) != null) {
				sessionId = null;
			}
		}
		
		Session newSession = new Session(sessionId, ticket,
			LocalDateTime.now().plusSeconds(this.options.getSessionExpirationInSeconds()));
		this.sessionStore.add(newSession);
		addCookie(this.options.getSessionCookieName(), newSession.getId(), 
			this.options.getSessionExpirationInSeconds(), response);

		return newSession;
	}
	
	/*
		Remove a given session and the corresponding cookie
	*/
	private void invalidateSession(Session session, HttpServletResponse response) {

		this.sessionStore.remove(session.getId());
		killCookie(this.options.getSessionCookieName(), response);
		
	}

	/*
		Invalidate the current session, removing only the session
	*/
	private void invalidateSession(HttpServletRequest request, HttpServletResponse response) {

		Cookie cookie = getCookie(this.options.getSessionCookieName(), request);
		if (cookie != null) {
			String sessionId = cookie.getValue();
			this.sessionStore.remove(sessionId);
		}
	}

	/*
		Invalidate the session if it has expired
	*/
	private boolean invalidateExpiredSession(Session session, HttpServletResponse response) {

		LocalDateTime now = LocalDateTime.now();
		if (now.compareTo(session.getExpiration()) > 0) {
			this.sessionStore.remove(session.getId());
			killCookie(this.options.getSessionCookieName(), response);
			return true;
		}
		return false;
	}

	/*
		Refresh a given session resetting the expiration time
	*/
	private void refreshSession(Session session, HttpServletResponse response) {

		this.sessionStore.update(session.getRefreshed(this.options.getSessionExpirationInSeconds()));
		addCookie(this.options.getSessionCookieName(), session.getId(), this.options.getSessionExpirationInSeconds(), response);
	}

	/*
		Find by name a cookie sent by the browser
	*/
	private Cookie getCookie(String cookieName, HttpServletRequest request) {

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				return cookie;
			}
		}
		return null;
	}

	/*
		Set a cookie in the response sent to the browser
	*/
	private void addCookie(String cookieName, String value, int expirationSeconds, HttpServletResponse response) {

		Cookie cookie = new Cookie(cookieName, value);
		cookie.setMaxAge(expirationSeconds); // persistent cookie
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		// cookie.setSecure(true); // needs https
		response.addCookie(cookie);
	}

	/* 
		Instruct to delete a cookie from the browser
	*/
	private void killCookie(String cookieName, HttpServletResponse response) {

		Cookie cookie = new Cookie(cookieName, "-");
		cookie.setMaxAge(0); // persistent cookie
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		// cookie.setSecure(true); // needs https
		response.addCookie(cookie);
	
	}
}
