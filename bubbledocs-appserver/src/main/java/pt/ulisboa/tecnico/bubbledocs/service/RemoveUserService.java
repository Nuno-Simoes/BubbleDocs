package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;


public class RemoveUserService extends PortalService {
	
	private String userToken;
	private String newUsername;
	
	public RemoveUserService (String userToken, String newUsername, 
			String password, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
	}

	@Override
	protected void dispatch() throws UserDoesNotExistException, 
		InvalidPermissionException {
		User u = getUser(userToken);
		
		if (u instanceof RootUser) {
			((RootUser) u).removeUser(this.newUsername);
		} else {
			throw new InvalidPermissionException(this.newUsername);
		}
	}
	
	
}