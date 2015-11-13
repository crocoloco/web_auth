package sample.webauth.service.session;

import java.time.LocalDateTime;

/**
 * Session entity
 *
 */
public class Session
{
	private String id;
	private Object data;
	private LocalDateTime expiration;

	public Session(String id, Object data, LocalDateTime expiration) {
		this.id = id;
		this.data = data;
		this.expiration = expiration;
	}

	// get the session identifier
	public String getId() {
		return this.id;
	}

	// get data associated with the session
	public Object getData() {
		return this.data;
	}

	// get session expiration date
	public LocalDateTime getExpiration() {
		return this.expiration;
	}

	/*
		Get a refreshed copy of the session
	*/
	public Session getRefreshed(long expirationSeconds) {
		return new Session(this.id, this.data,
			LocalDateTime.now().plusSeconds(expirationSeconds));
	}
}	
