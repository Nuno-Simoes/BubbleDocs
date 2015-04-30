package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserService extends PortalService {

	private String userToken;
	private String newUsername;
	private String email;
	private String name;


	public CreateUserService (String userToken, String newUsername, 
			String email, String name) {
		this.userToken = userToken;
		this.newUsername = newUsername;
		this.email = email;
		this.name = name;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
		LoginBubbleDocsException, InvalidUsernameException, DuplicateUsernameException {
			
		try {
			IDRemoteServices service = new IDRemoteServices();
			service.createUser(newUsername, email);
		} catch( RemoteInvocationException rie) {
			throw new UnavailableServiceException();
		}
			
		User u = super.getUser(userToken);
		if (u instanceof RootUser) {
			((RootUser) u).addUser(newUsername, name, 
					email);
		} else {
			throw new InvalidPermissionException(newUsername);
		}

	}
	
}
