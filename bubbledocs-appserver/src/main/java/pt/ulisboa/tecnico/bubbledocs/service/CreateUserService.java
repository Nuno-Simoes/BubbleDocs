package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;
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
	UserNotLoggedException, EmptyUsernameException, UserAlreadyExistsException {

		try {
			IDRemoteServices service = new IDRemoteServices();
			service.createUser(newUsername, email);

			if(this.newUsername.equals("")) {
				throw new EmptyUsernameException();
			}

			User u = super.getUser(userToken);

			if (u instanceof RootUser) {
				((RootUser) u).addUser(newUsername, name, 
						email);
			} else {
				throw new InvalidPermissionException(newUsername);
			}
		} catch (RemoteInvocationException) {
		} finally {
			throw new UnavailableServiceException();
		}
	}
}