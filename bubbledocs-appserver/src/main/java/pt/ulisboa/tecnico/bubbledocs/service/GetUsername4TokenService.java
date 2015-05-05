package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class GetUsername4TokenService extends BubbleDocsService {
	
	private String token;
	private String username;
	
	public GetUsername4TokenService(String token){
			this.token = token;
	}

	@Override
	protected void dispatch() throws LoginBubbleDocsException {
		Session s = Session.getInstance();
		User u = s.getLoggedUser(token);
		this.username = u.getUsername();
	}
	
	public final String getResult() {
		return username;
	}
}
