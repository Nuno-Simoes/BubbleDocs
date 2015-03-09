package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUserException;

public class RootUser extends RootUser_Base {
    
    public RootUser() {
        super();
    }
    
    public void add (String username, String name, String password) throws InvalidUserException {
    	for (User u : this.getPortal().getUsers()) {
    		if (username.equals(u.getUsername())) {
    			throw new InvalidUserException(username);
    		}
    	}
    	
    	this.getPortal().addUsers(new User(username, name, password));
    }
    
    
    //acabar
 /*	public void remove (String username) throws InvalidUserException {
    	if (this.getUsername().equals(username)) {
    		throw new InvalidUserException(username);
    	}
    	
    	for (User u : this.getPortal().getUsers()) {
    		if (u.getUsername().equals(username)) {
    			this.removeUsers(this.getPortal().)
    		}
    	}
    	
    }*/
    
}