package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class LoginUserService extends BubbleDocsService {

	private String userToken;
	private String username;
	private String password;
	private boolean status;
	
	public LoginUserService(String username, String password, boolean status) {
		this.username = username;
		this.password = password;
		this.status = status;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
		LoginBubbleDocsException {
		
		Portal p = Portal.getInstance();
		Session s = Session.getInstance();
		
		s.login(username, password, status);
		this.userToken = p.findUser(username).getToken();	
	}

	public final String getResult() {
		return userToken;
	}
}