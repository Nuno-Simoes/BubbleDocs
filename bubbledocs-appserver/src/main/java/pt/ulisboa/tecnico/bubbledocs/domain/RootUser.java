package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;

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
    	try {
    		s.removeUser(u.getToken());
    	} catch (UserNotLoggedException unle) {
    		//ignore
    	}
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