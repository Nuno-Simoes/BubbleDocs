package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class RootUser extends RootUser_Base {
    
	public static RootUser getInstance() {
		RootUser user = FenixFramework.getDomainRoot().getRootUser();
		Portal portal = Portal.getInstance();
		if (user == null) {
		    user = new RootUser();
		    portal.addUsers(user);
		}

		return user;
	}

	private RootUser() {
		FenixFramework.getDomainRoot().setRootUser(this);
		FenixFramework.getDomainRoot().getRootUser().setId(0);
		FenixFramework.getDomainRoot().getRootUser().setName("Super User");
		FenixFramework.getDomainRoot().getRootUser().setUsername("root");
		FenixFramework.getDomainRoot().getRootUser().setPassword("rootroot");
	}
    
    public void addUser (String username, String name, String password) 
    		throws UserAlreadyExistsException {
    	
    	Portal portal = Portal.getInstance();
    	User user = new User(username, name, password);
    	portal.addUsers(user); 	
   }
    
    public void removeUser (String username) throws UserDoesNotExistException {
    	Portal portal = Portal.getInstance();
    	Session s = Session.getInstance();
    	User u = portal.findUser(username);
    	s.removeUser(u.getToken());
    	u.delete();
//    	this.removeUserPermissions(u);
//    	portal.removeSpreadsheet(u);
    	portal.removeUsers(u);
    }

    public void removeUserPermissions (User u) {
    	for (Permission p : u.getPermissionsSet()) {
    		this.removePermissions(p);
    	}
    }
}