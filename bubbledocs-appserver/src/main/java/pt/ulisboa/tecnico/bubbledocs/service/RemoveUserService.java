package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;


public class RemoveUserService extends PortalService {
	private String userToken;
	private String Username;
	
	public RemoveUserService (String userToken, String Username) {
		this.userToken = userToken;
		this.Username = Username;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
	UserNotLoggedException, EmptyUsernameException, UserAlreadyExistsException {
		
		if(this.Username.equals("")) {
			throw new EmptyUsernameException();
		}
		
		User u = getUser(userToken);
		
		if (u instanceof RootUser) {
			((RootUser) u).removeUser(this.Username);
		} else {
			throw new InvalidPermissionException(this.Username);
		}
	}
}