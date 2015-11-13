package sample.webauth.service.session;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in memory implementation of a session store
 *
 */
public class DefaultSessionStore implements ISessionStore
{
	private static ConcurrentHashMap<String, Session> sessions;

	public DefaultSessionStore() {
		sessions = new ConcurrentHashMap<String, Session>();
	}

	/*
		Get a session by identifier 
	*/
	public Session get(String sessionId) {
		return sessions.get(sessionId);
	}

	/*
		Add a session
		Session is updated if already exists
	*/
	public void add(Session session) {
		sessions.put(session.getId(), session);
	}

	/*
		Update a session
	*/
	public void update(Session session) {
		add(session);
	}

	/*
		Remove a session
	*/
	public void remove(String sessionId) {
		sessions.remove(sessionId);
	}
}	
