package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;

public class GetUsername4TokenService extends BubbleDocsService {
	
	private String token;
	private String username;
	
	public GetUsername4TokenService(String token){
			this.token = token;
	}

	@Override
	protected void dispatch() {
		Session s = Session.getInstance();
		User u = s.getLoggedUser(token);
		this.username = u.getUsername();
	}
	
	public final String getResult() {
		return this.username;
	}
}
