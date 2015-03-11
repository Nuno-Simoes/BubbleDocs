package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUserException;

public class RootUser extends RootUser_Base {
    
	public static RootUser getInstance() {
		RootUser user = FenixFramework.getDomainRoot().getRootUser();
		if (user == null)
		    user = new RootUser();

		return user;
	}

	private RootUser() {
		FenixFramework.getDomainRoot().setRootUser(this);
	}
    
    public void add (String username, String name, String password) 
    		throws InvalidUserException {
    	for (User u : this.getPortal().getUsersSet()) {
    		if (username.equals(u.getUsername())) {
    			throw new InvalidUserException(username);
    		}
    	}
    	
    	User user = new User(username, name, password);
    	user.setId(this.getId());
    	this.getPortal().addUsers(new User(username, name, password));
    	this.setId(this.getId()+1);
    }
    

    
    public void removeUser (String username) throws InvalidUserException {
    	if (this.getUsername().equals(username)) {
    		throw new InvalidUserException(username);
    	}
    	for (User u : this.getPortal().getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			removeUserPermissions(u);
    			this.getPortal().removeSpreadsheet(u);
    			this.getPortal().removeUsers(u);
    		}
    	}
    }

    public void removeUserPermissions (User u) {
    	for (Permission p : u.getPermissionsSet()) {
    		this.removePermissions(p);;
    	}
    }
    
}