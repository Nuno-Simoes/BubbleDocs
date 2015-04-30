package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.CreateUserService;
import pt.ulisboa.tecnico.bubbledocs.service.RemoveUserService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RemoveUserIntegrator extends BubbleDocsIntegrator {
	
	private String userToken;
	private String username;
	private String email;
	private String name;

	RemoveUserIntegrator(String userToken, String username) {
		this.userToken = userToken;
		this.username = super.getUsername(userToken);
		this.email = super.getEmail(userToken);
		this.name = super.getName(userToken);
	}
	
	@Override
	protected void dispatch() throws Exception {
		
		RemoveUserService localService = new RemoveUserService(userToken, username);
		IDRemoteServices remoteService = new IDRemoteServices();
		
		try {
			localService.execute();
			remoteService.removeUser(username);
		} catch (Exception e) {
			CreateUserService compensationService =
					new CreateUserService(userToken, username, email, name);
			compensationService.execute();
		}
	}

}
