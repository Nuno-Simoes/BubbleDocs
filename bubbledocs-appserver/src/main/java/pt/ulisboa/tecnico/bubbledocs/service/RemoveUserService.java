package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;


public class RemoveUserService extends PortalService {
	private String userToken;
	private String username;
	
	public RemoveUserService (String userToken, String username) {
		this.userToken = userToken;
		this.username = username;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
	UserNotLoggedException, EmptyUsernameException, UserAlreadyExistsException {
		
		if(this.username.equals("")) {
			throw new EmptyUsernameException();
		}
		
		User u = getUser(userToken);
		
		if (u instanceof RootUser) {
			((RootUser) u).removeUser(this.username);
		} else {
			throw new InvalidPermissionException(this.username);
		}
	}
}