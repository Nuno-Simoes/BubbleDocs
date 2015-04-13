package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
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
		LoginBubbleDocsException, RemoteInvocationException {
		//Session s = Session.getInstance();
		//s.login(username, password);
		//this.userToken = s.findUserByUsername(username).getToken();
		try {
			IDRemoteServices service = new IDRemoteServices();
			service.loginUser(username, password);
		} catch (RemoteInvocationException rie) {
			Session s = Session.getInstance();
			s.login(username, password);
			this.userToken = s.findUserByUsername(username).getToken();	
		} finally {
			throw new UnavailableServiceException();
		}
	}

	public final String getUserToken() {
		return userToken;
	}
}

