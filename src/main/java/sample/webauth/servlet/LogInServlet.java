package sample.webauth.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.webauth.service.authentication.AuthenticationTicket;
import sample.webauth.service.authentication.http.AuthenticationManager;
import sample.webauth.service.user.User;
import sample.webauth.service.user.UserService;

/**
 * Servlet that handles log in
 *
 */
public class LogInServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = Logger.getLogger(LogInServlet.class.getName());	

	private AuthenticationManager authenticationManager;
	private UserService userService;

	@Override
	public void init(ServletConfig config) throws ServletException {
        super.init(config);

		ServletContext context = config.getServletContext();
		// get dependencies
		this.authenticationManager = ContextUtils.getInstance(context, AuthenticationManager.class);
		this.userService = ContextUtils.getInstance(context, UserService.class);
    }	

	/*
		Handle get login request
	*/
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		request.getRequestDispatcher("/WEB-INF/login.jsp")
			.forward(request, response);
	}

	/*
		Handle post login request with user credentials
	*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		final String userName = request.getParameter("username");
        final String password = request.getParameter("password");

		User user = this.userService.checkCredentials(userName, password);
		if (user != null) {
			AuthenticationTicket ticket = new AuthenticationTicket(user.getUserName(), user.getRoles());
			this.authenticationManager.signIn(ticket, request, response);
		}
		else {
			// TODO: display invalid credentials error 
		}
	}
}	
