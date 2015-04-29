package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

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
		LoginBubbleDocsException {
		
		Portal p = Portal.getInstance();
		p.localLogin(username, password);
		this.userToken = p.findUserByUsername(username).getToken();	
		
	}

	public final String getUserToken() {
		return userToken;
	}
}