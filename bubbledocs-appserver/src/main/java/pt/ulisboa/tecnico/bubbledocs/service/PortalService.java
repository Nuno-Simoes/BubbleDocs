package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.PortalException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public abstract class PortalService {
	
	@Atomic
	public final void execute() throws PortalException {
		dispatch();
	}
	
	static Portal getPortal() {
		return FenixFramework.getDomainRoot().getPortal();
	}
	
	static User getUser(String username) throws UserDoesNotExistException {
		User u = getPortal().findUser(username);
		
		if (u == null) {
			throw new UserDoesNotExistException(username);
		}
		
		return u;
	}
	
	protected abstract void dispatch() throws PortalException;
}