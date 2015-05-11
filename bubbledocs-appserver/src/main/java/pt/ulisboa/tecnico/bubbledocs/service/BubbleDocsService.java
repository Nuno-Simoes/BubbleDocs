package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;

public abstract class BubbleDocsService {
	
	@Atomic
	public final void execute() {
		dispatch();
	}
	
	static Portal getPortal() {
		return FenixFramework.getDomainRoot().getPortal();
	}
	
	static User getUser(String token) {
		
		Portal p = Portal.getInstance();
		User u = p.findUserByToken(token);
		
		return u;
	}
	
	protected abstract void dispatch();
}