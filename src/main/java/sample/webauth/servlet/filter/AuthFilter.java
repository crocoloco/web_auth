package sample.webauth.servlet.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.webauth.service.authentication.AuthenticationTicket;
import sample.webauth.service.authentication.http.AuthenticationManager;
import sample.webauth.service.authorization.IResourceAuthorizationService;
import sample.webauth.servlet.ContextUtils;

/**
 * Servlet filter for authentication & authorization
 *
 */
public class AuthFilter implements Filter
{
	private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());	

	private AuthenticationManager authenticationManager;
	private IResourceAuthorizationService resourceAuthService;

	public void init(FilterConfig config) throws ServletException {

		final ServletContext context = config.getServletContext();
		// get dependencies
		this.authenticationManager = ContextUtils.getInstance(context, AuthenticationManager.class);
		this.resourceAuthService = ContextUtils.getInstance(context, IResourceAuthorizationService.class);

		LOGGER.info("AuthFilter initialized");
	}

	/*
		Request handling
	*/
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
		final String uri = req.getRequestURI();
		LOGGER.info("Filtering " + uri);

		// authenticate request
		AuthenticationTicket ticket = this.authenticationManager.authenticate(req, res);

		// if not authenticated ask for user credentials
		if (ticket == null) {
			this.authenticationManager.challengeUser(req, res);
		}
		else {
			// identify the request with the username
			req.setAttribute("username", ticket.getUserName());

			// check authorization
			String path = req.getRequestURI().substring(req.getContextPath().length());
			if (this.resourceAuthService.isAuthorized(path, ticket.getRoles())) {
				LOGGER.info("Authorized access to " + uri);
				chain.doFilter(request, response);
			}
			else {
				LOGGER.info("Unauthorized access to " + uri);
				res.setStatus(HttpServletResponse.SC_FORBIDDEN);
				// TODO: get from configuration
				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/forbidden.jsp");
				dispatcher.include(req, res);
			}
		}
    }

	public void destroy() {
		// free resources
		LOGGER.info("AuthFilter destroyed");
	}
}	
