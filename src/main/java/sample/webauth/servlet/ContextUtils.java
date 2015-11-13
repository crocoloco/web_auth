package sample.webauth.servlet;

import javax.servlet.ServletContext;

/**
 * Utility class for servlets to register and get instances
 *
 */
public class ContextUtils {

	/*
		Register an instance of the specified type
	*/
	public static <T> void registerInstance(ServletContext context, Class<T> type, T instance) {
		context.setAttribute(type.getSimpleName(), instance);
	}

	/*
		Get a registered instance of the given type
	*/
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(ServletContext context, Class<T> type) {
		return (T) context.getAttribute(type.getSimpleName());
	}
}
