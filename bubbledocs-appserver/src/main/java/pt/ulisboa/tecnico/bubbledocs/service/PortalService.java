package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.PortalException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;

public abstract class PortalService {
	
	@Atomic
	public final void execute() throws PortalException {
		dispatch();
	}
	
	static Portal getPortal() {
		return FenixFramework.getDomainRoot().getPortal();
	}
	
	static User getUser(String token) throws UserNotLoggedException {
		Session s = Session.getInstance();
		User u = s.findUser(token);
		
		return u;
	}
	
	protected abstract void dispatch() throws PortalException;
}