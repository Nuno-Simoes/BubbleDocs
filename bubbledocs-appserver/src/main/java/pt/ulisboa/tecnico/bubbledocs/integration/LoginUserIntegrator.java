package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.LoginUserService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String username;
	private String password;
	
	public LoginUserIntegrator(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected void dispatch() throws Exception {
		
		IDRemoteServices remoteService = new IDRemoteServices();
		LoginUserService localService = new LoginUserService(username, password);
			
		remoteService.loginUser(username, password);
		localService.execute();
	}

}
