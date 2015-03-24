package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;


public class CreateUserService extends PortalService {
	
	private String userToken;
	private String newUsername;
	private String password;
	private String name;
	
	
	public CreateUserService (String userToken, String newUsername, 
			String password, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
		this.password = password;
		this.name = name;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
		UserNotLoggedException, EmptyUsernameException {
		
		if(this.newUsername.equals("")) {
			throw new EmptyUsernameException();
		}
		
		User u = super.getUser(userToken);
				
		if (u instanceof RootUser) {
			((RootUser) u).addUser(this.newUsername, this.name, 
					this.password);
		} else {
			throw new InvalidPermissionException(this.newUsername);
		}
	}	
}