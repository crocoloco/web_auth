package sample.webauth.service.authorization;

import java.util.List;

/**
 * Service to check authorization for resources
 *
 */
public interface IResourceAuthorizationService
{
	/*
		Check if a resource is authorized for the given roles
	*/
	public boolean isAuthorized (String resourcePath, List<String> roles);

	/*
		Get the list of authorized resources for the given roles
	*/
	public List<String> getAuthorizedResources(List<String> roles);

	/*
		Register a resource authorization
	*/
	public void addResource(String resourcePath, List<String> roles);
}	
