package sample.webauth.service.user;

/**
 * Interface to users store
 *
 */
public interface IUserStore 
{
	/*
		Get a user by her name
	*/
	public User getByName(String userName);

	/*
		Add a user
	*/
	public void add(User user);
}	
