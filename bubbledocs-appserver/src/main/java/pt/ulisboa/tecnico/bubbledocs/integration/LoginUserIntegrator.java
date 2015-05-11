package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUserService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	private String username;
	private String password;
	private String result;
	
	public LoginUserIntegrator(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	protected void dispatch() {
		
		IDRemoteServices remoteService = new IDRemoteServices();
		LoginUserService localService;
		boolean failed = false;
		
		try {
			remoteService.loginUser(username, password);
		} catch (RemoteInvocationException rie) {
			failed = true;
		}
		
		localService = new LoginUserService(username, password, failed);
		localService.execute();
		this.result = localService.getResult();
	}
	
	public String getResult() {
		return this.result;
	}

}
