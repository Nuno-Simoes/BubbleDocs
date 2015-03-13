package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class RootUser extends RootUser_Base {
    
	public static RootUser getInstance() {
		RootUser user = FenixFramework.getDomainRoot().getRootUser();
		if (user == null)
		    user = new RootUser();

		return user;
	}

	private RootUser() {
		FenixFramework.getDomainRoot().setRootUser(this);
		FenixFramework.getDomainRoot().getRootUser().setId(0);
		FenixFramework.getDomainRoot().getRootUser().setName("Super User");
		FenixFramework.getDomainRoot().getRootUser().setUsername("root");
	}
    
    public void addUser (String username, String name, String password) 
    		throws UserAlreadyExistsException {
    	Portal portal = Portal.getInstance();
    	
    	for (User u : portal.getUsersSet()) {
    		if (username.equals(u.getUsername())) {
    			throw new UserAlreadyExistsException(username);
    		}
    	}
    	
    	User user = new User(username, name, password);
    	int id = portal.getUserId();
    	user.setId(id);
    	portal.setUserId(id+1);
    	portal.addUsers(user);
    }
    
    public void removeUser (String username) throws UserDoesNotExistException {
    	Portal portal = Portal.getInstance();
    	boolean found = false;
    	
    	if (this.getUsername().equals(username)) {
    		throw new UserDoesNotExistException(username);
    	}
    	
    	for (User u : portal.getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			removeUserPermissions(u);
    			portal.removeSpreadsheet(u);
    			portal.removeUsers(u);
    			found = true;
    		}
    	}
    	
    	if (!found) {
    		throw new UserDoesNotExistException(username);
    	}
    }

    public void removeUserPermissions (User u) {
    	for (Permission p : u.getPermissionsSet()) {
    		this.removePermissions(p);
    	}
    }
    
}