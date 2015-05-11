package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSessionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordService extends BubbleDocsService {
	private String userToken;

	public RenewPasswordService(String userToken) {
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws UnavailableServiceException, 
	InvalidSessionException {

		Session s = Session.getInstance();
		User u = getUser(userToken);
		boolean logged = s.isInSession(u);
		if (logged == false) {
			throw new InvalidSessionException();
		}

		try {
			IDRemoteServices renew = new IDRemoteServices();
			renew.renewPassword(u.getToken());
			u.setPassword(null);
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
	}
}