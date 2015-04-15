package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
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
		this.setId(0);
		this.setName("Super User");
		this.setUsername("root");
		this.setPassword("rootroot");
		this.setIsInSession(false);
		this.setSessionTime(0);
		this.setToken(null);
	}    

    public void addUser (String username, String name, String email) 
    		throws DuplicateUsernameException, InvalidUsernameException {
    	
    	if (username.equals("") || username.length() < 3 || username.length() > 8) {
 		   throw new InvalidUsernameException();
 	   }
    	
    	Portal portal = Portal.getInstance();
    	User user = new User(username, name, email);
    	portal.addUsers(user); 	
   }
    
    public void removeUser (String username) throws UserDoesNotExistException {
    	Portal portal = Portal.getInstance();
    	User u = portal.findUser(username);
    	u.delete();
    	portal.removeUsers(u);
    }

    public void removeUserPermissions (User u) {
    	for (Permission p : u.getPermissionsSet()) {
    		this.removePermissions(p);
    	}
    }
}
