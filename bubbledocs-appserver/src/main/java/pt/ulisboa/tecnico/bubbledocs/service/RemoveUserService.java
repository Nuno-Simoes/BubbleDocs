package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;

public class RemoveUserService extends BubbleDocsService {
	private String userToken;
	private String username;
	
	public RemoveUserService (String userToken, String username) {
		this.userToken = userToken;
		this.username = username;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException {
		
		User u = getUser(userToken);
		
		if (u instanceof RootUser) {
			((RootUser) u).removeUser(this.username);
		} else {
			throw new InvalidPermissionException(this.username);
		}
	}
}
