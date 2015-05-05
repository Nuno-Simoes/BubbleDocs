package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUserService;
import pt.ulisboa.tecnico.bubbledocs.service.RemoveUserService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String newUsername;
	private String email;
	private String name;

	public CreateUserIntegrator (String userToken, String newUsername, 
			String email, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
	}

	@Override
	protected void dispatch() throws UnavailableServiceException {
		CreateUserService localService = new CreateUserService(userToken, newUsername, email, name);
		IDRemoteServices remoteService = new IDRemoteServices();
		try {
			localService.execute();
			remoteService.createUser(newUsername, email);
		} catch (RemoteInvocationException rie) {
			RemoveUserService compensationService = new RemoveUserService(userToken, newUsername);
			compensationService.execute();
			throw new UnavailableServiceException();
		}
	}

}