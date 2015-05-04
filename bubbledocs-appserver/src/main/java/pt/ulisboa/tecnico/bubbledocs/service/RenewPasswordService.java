package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordService extends BubbleDocsService {
	private String userToken;
	
	public RenewPasswordService (String userToken) {
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
		LoginBubbleDocsException, DuplicateUsernameException,
		UnavailableServiceException {
		
		User u = getUser(userToken);
		
		try {
			IDRemoteServices renew = new IDRemoteServices();
			renew.renewPassword(u.getToken());
			u.setPassword(null);
		} catch (RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
	}
}