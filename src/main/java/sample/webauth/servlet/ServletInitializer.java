package sample.webauth.servlet;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.function.Consumer;
import javax.json.JsonObject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import sample.webauth.config.JsonConfiguration;
import sample.webauth.service.authentication.http.AuthenticationManager;
import sample.webauth.service.authentication.http.AuthenticationOptions;
import sample.webauth.service.authorization.IResourceAuthorizationService;
import sample.webauth.service.authorization.DefaultResourceAuthorizationService;
import sample.webauth.service.session.DefaultSessionStore;
import sample.webauth.service.security.StrongIdGenerator;
import sample.webauth.service.security.PasswordHasher;
import sample.webauth.service.user.DefaultUserStore;
import sample.webauth.service.user.UserService;

/**
 * Initialization of servlets dependencies
 *
 */
@WebListener
public class ServletInitializer implements ServletContextListener {

	private static final Logger LOGGER = Logger.getLogger(ServletInitializer.class.getName());	
	private static final String LOGIN_PATH_PARAM_NAME = "loginPath";
	private static final String SESSION_EXPIRATION_PARAM_NAME = "sessionExpirationInSeconds";
	private static final String SESSION_COOKIE_NAME_PARAM_NAME = "sessionCookieName";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {

		ServletContext context = sce.getServletContext();

		initializeInstances(context);
	}
	
	// initialize dependencies used by the servlets
	private void initializeInstances(ServletContext context)
	{
		LOGGER.info("Registering instances");

		try {
			UserService userService = new UserService(new DefaultUserStore(), new PasswordHasher());
			configureUsers(userService, context.getRealPath("WEB-INF/users.json"));

			IResourceAuthorizationService resourceAuthSvc = new DefaultResourceAuthorizationService();
			configureResources(resourceAuthSvc, context.getRealPath("WEB-INF/authorization.json"));
			
			ContextUtils.registerInstance(context, IResourceAuthorizationService.class, resourceAuthSvc);
			ContextUtils.registerInstance(context, AuthenticationManager.class,
				new AuthenticationManager(getAuthOptions(context), resourceAuthSvc,
					new DefaultSessionStore(), new StrongIdGenerator()));
			ContextUtils.registerInstance(context, UserService.class, userService);

		}
		catch (Exception ex) {
			LOGGER.info("Error registering instances: " + ex.getMessage());
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	// get authentication options from servlet configuration
	private AuthenticationOptions getAuthOptions(ServletContext context) {
		return new AuthenticationOptions(
			context.getInitParameter(LOGIN_PATH_PARAM_NAME),
			Integer.parseInt(context.getInitParameter(SESSION_EXPIRATION_PARAM_NAME)),
			context.getInitParameter(SESSION_COOKIE_NAME_PARAM_NAME)
		);
	}

	// add users from configuration
	private void configureUsers(UserService userService, String configFilePath) throws Exception {

		JsonConfiguration.consumeObjectArrayFromFile(configFilePath, (ThrowingConsumer<JsonObject>) x ->
			userService.addUser(
				x.getInt("id"),
				x.getString("username"),
				x.getString("password"),
				JsonConfiguration.toStringList(x.getJsonArray("roles")))
			);
	}
	
	// add resource authorizations from configuration
	private void configureResources(IResourceAuthorizationService resourceAuthSvc, String configFilePath) throws Exception {
		
		JsonConfiguration.consumeObjectArrayFromFile(configFilePath, x ->
			resourceAuthSvc.addResource(
				x.getString("path"),
				JsonConfiguration.toStringList(x.getJsonArray("roles")))
		);
	}

	// to adapt to Consumer a lambda expression that throws an exception
	@FunctionalInterface
	interface ThrowingConsumer<T> extends Consumer<T> {

		@Override
		default void accept(final T elem) {
			try {
				acceptThrows(elem);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}

		void acceptThrows(T elem) throws Exception;
	}	
}
