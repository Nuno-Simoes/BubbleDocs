package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;

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
		
		User u = getUser(userToken);
		
		if (u instanceof RootUser) {
			((RootUser) u).removeUser(this.username);
		} else {
			throw new InvalidPermissionException(this.username);
		}
	}
}
