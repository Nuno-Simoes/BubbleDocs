package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUserException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class RootUser extends RootUser_Base {
    
    private static RootUser instance = null;

    protected RootUser() {
        super();
        this.setName("Super User");
        this.setUsername("root");
        this.setPassword("rootroot");
    }
        
    public static RootUser getInstance() {
    	if (instance==null) {
    		instance = new RootUser();
    	}
    	
    	return instance;
    }
    
    public void add (String username, String name, String password) throws InvalidUserException {
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
    
    public User returnUser (String username) throws UserDoesNotExistException {
    	
    	for (User u : this.getPortal().getUsersSet()) {
    		if (username.equals(u.getUsername())) {
    				return u;
    		}
    	}
    	
    	throw new UserDoesNotExistException(username);
    	
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