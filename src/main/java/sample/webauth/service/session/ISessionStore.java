package sample.webauth.service.session;

/**
 * Session store
 *
 */
public interface ISessionStore
{
	/*
		Get a session by identifier 
	*/
	public Session get(String sessionId);

	/*
		Add a session
		Session is updated if already exists
	*/
	public void add(Session session);

	/*
		Update a session
	*/
	public void update(Session session);

	/*
		Remove a session
	*/
	public void remove(String sessionId);
}	
