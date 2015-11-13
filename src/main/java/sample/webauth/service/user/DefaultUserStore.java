package sample.webauth.service.user;

import java.util.List;
import java.util.LinkedList;

/**
 * Simple in memory implementation of a user store
 *
 */
public class DefaultUserStore implements IUserStore
{
	private List<User> entities;

	public DefaultUserStore() {
		this.entities = new LinkedList<User>();
	}

	/*
		Get a user by her name
	*/
	public User getByName(String userName)
	{
		return this.entities.stream()
			.filter(x -> x.getUserName().equals(userName))
			.findFirst().orElse(null);
	}

	/*
		Add a user
	*/
	public void add (User user) {
		this.entities.add(user);
	}
}	
