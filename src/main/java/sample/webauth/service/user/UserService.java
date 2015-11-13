package sample.webauth.service.user;

import java.util.List;

import sample.webauth.service.security.PasswordHasher;

/**
 * Service that checks user credentials
 *
 */
public class UserService
{
	private IUserStore userStore;
	private PasswordHasher passwordHasher;

	public UserService(IUserStore userStore, PasswordHasher passwordHasher) {
		this.userStore = userStore;
		this.passwordHasher = passwordHasher;
	}

	/*
		Get a user matching the given credentials
	*/
	public User checkCredentials(String userName, String password)
	{
		String hashedPassword = password;

		User user = this.userStore.getByName(userName);
		if (user == null) {
			return null;
		}

		try {
			if (this.passwordHasher.check(password, user.getHashedPassword())) {
				return user;
			}
		}
		catch (Exception ex) {
			// TODO: propagate exception
		}

		return null;
	}

	/*
		Register a user
	*/
	public User addUser(long id, String userName, String password, List<String> roles) throws Exception {

		String hashedPassword = this.passwordHasher.hash(password);
		User user = new User(id, userName, hashedPassword, roles);
		this.userStore.add(user);

		return user;

	}
}	
