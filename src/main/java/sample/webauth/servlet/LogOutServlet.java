package sample.webauth.servlet;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.webauth.service.authentication.http.AuthenticationManager;

/**
 * Servlet that handles log out
 *
 */
public class LogOutServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = Logger.getLogger(LogOutServlet.class.getName());	

	private AuthenticationManager authenticationManager;

	@Override
	public void init(ServletConfig config) throws ServletException {
        super.init(config);

		ServletContext context = config.getServletContext();
		// get dependencies
		this.authenticationManager = ContextUtils.getInstance(context, AuthenticationManager.class);
    }	
	
	/*
		Handle post request
	*/
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		this.authenticationManager.signOut(request, response);
	}
}	
