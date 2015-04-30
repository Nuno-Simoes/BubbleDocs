package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public abstract class BubbleDocsIntegrator {

	public final void execute() throws Exception {
		dispatch();
	}
	
	static String getUsername(String token) throws LoginBubbleDocsException {
		
		Portal p = Portal.getInstance();
		User u = p.findUserByToken(token);
		
		return u.getUsername();
	}
	
	static String getEmail(String token) throws LoginBubbleDocsException {
		
		Portal p = Portal.getInstance();
		User u = p.findUserByToken(token);
		
		return u.getEmail();
	}
	
	static String getName(String token) throws LoginBubbleDocsException {
	
	Portal p = Portal.getInstance();
	User u = p.findUserByToken(token);
	
	return u.getName();
	}
	
	protected abstract void dispatch() throws Exception;
}
