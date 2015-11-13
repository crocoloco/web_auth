package sample.webauth.service.authorization;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Default implementation of a service to check authorization for resources
 *
 */
public class DefaultResourceAuthorizationService implements IResourceAuthorizationService
{
	private Map<String, List<String>> resourceRoles;

	public DefaultResourceAuthorizationService() {

		this.resourceRoles = new HashMap<String, List<String>>();
	}

	/*
		Check if a resource is authorized for the given roles
	*/
	public boolean isAuthorized (String resourcePath, List<String> roles) {

		List<String> authorizedRoles = this.resourceRoles.get(resourcePath);
		if (authorizedRoles == null) {
			return false;
		}

		return ! Collections.disjoint(authorizedRoles, roles);
	}

	/*
		Get the list of authorized resources for the given roles
	*/
	public List<String> getAuthorizedResources(List<String> roles) {

		return this.resourceRoles.entrySet().stream()
			.filter(e -> ! Collections.disjoint(e.getValue(), roles))
			.map(e -> e.getKey())
			.collect(Collectors.toList());
	}

	/*
		Register a resource authorization
	*/
	public void addResource(String resourcePath, List<String> roles) {
		this.resourceRoles.put(resourcePath, roles); 
	}
}	
