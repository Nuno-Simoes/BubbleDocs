package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserService extends PortalService {

	private String userToken;
	private String username;
	private String password;
	
	public LoginUserService(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
		LoginBubbleDocsException, RemoteInvocationException,
		UnavailableServiceException {
		Portal p = Portal.getInstance();
		
		try {
			IDRemoteServices service = new IDRemoteServices();
			service.loginUser(username, password);
			p.login(username, password);
			p.renewPassword(username, password);
			this.userToken = p.findUserByUsername(username).getToken();
		} catch (RemoteInvocationException rie) {
			p.localLogin(username, password);
			this.userToken = p.findUserByUsername(username).getToken();
		} 		
	}

	public final String getUserToken() {
		return userToken;
	}
}