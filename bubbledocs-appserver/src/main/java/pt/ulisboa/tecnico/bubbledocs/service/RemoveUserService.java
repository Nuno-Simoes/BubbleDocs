package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RemoveUserService extends PortalService {
	private String userToken;
	private String username;
	
	public RemoveUserService (String userToken, String username) {
		this.userToken = userToken;
		this.username = username;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
		LoginBubbleDocsException, DuplicateUsernameException,
		UnavailableServiceException {
		
		if(this.username.equals("")) {
			throw new InvalidUsernameException();
		}
		
		User u = getUser(userToken);
		
		if (u instanceof RootUser) {
			try {
				IDRemoteServices remove = new IDRemoteServices();
				remove.removeUser(username);
				((RootUser) u).removeUser(this.username);
			} catch (RemoteInvocationException rie) {
				throw new UnavailableServiceException();
			}
		} else {
			throw new InvalidPermissionException(this.username);
		}
	}
}
