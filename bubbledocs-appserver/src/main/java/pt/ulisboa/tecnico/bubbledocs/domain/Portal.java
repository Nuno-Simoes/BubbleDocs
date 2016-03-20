package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;

public class Portal extends Portal_Base {
    	
	private Portal() {
		FenixFramework.getDomainRoot().setPortal(this);
		this.setUserId(0);
		this.setSheetId(0);
	}
	
	public static Portal getInstance() {
		Portal portal = FenixFramework.getDomainRoot().getPortal();
		if (portal == null) {
		    portal = new Portal();
		}
		
		RootUser root = RootUser.getInstance();
		boolean flag = true;
		
		for (User u : portal.getUsersSet()) {
			if (u.getUsername().equals(root.getUsername())) {
				flag = false;
				break;
			}
		}
		
		if (flag) {
			portal.addUsers(root);
		}
		
		return portal;
	}
   
    public Spreadsheet findSpreadsheet (int id) 
    		throws SpreadsheetDoesNotExistException {
    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getId() == id) {
    			return s;
    		}
    	}
    	throw new SpreadsheetDoesNotExistException(Integer.toString(id)); 
    }
    
    public Spreadsheet findSpreadsheet (User u, String name) 
    		throws SpreadsheetDoesNotExistException {

    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getName().equals(name) && s.getOwner().equals(u.getUsername())) {
    			return s;
    		}
    	}
    	
    	throw new SpreadsheetDoesNotExistException(name); 
    }
    
    public Spreadsheet findSpreadsheet (String name) 
    		throws SpreadsheetDoesNotExistException {
    	
    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getName().equals(name)) {
    			return s;
    		}
    	}
    	throw new SpreadsheetDoesNotExistException(name); 	
    }
        
    public boolean isOwner (User u, Spreadsheet s) {
    	if(u.getUsername().equals(s.getOwner())){
    		return true;
    	}
    	return false;
    }
    
    @Override
    public void addUsers(User user) throws DuplicateUsernameException {
    	
    	for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(user.getUsername())) {
    			throw new DuplicateUsernameException(user.getUsername());
    		}
    	}
    	
    	int id = this.getUserId();
    	user.setId(id);
    	this.setUserId(id+1);
    	
    	this.getUsersSet().add(user);	
    }
    
    public Spreadsheet importFromXML(org.jdom2.Element element, String owner) {
    	Spreadsheet s = new Spreadsheet();
    	int id = this.getSheetId();
    	this.setSheetId(id+1);
    	this.addSpreadsheets(s);
    	s.setOwner(owner);
    	s.setId(this.getSheetId());
    	s.importFromXML(element);
    	return s;
    }
    
    public User findUserByToken(String token) throws LoginBubbleDocsException {
    	for (User u : this.getUsersSet()) {
    		if ((!(u.getToken() == null)) && (u.getToken().equals(token))) {
    			return u;
    		}
    	}
    	throw new LoginBubbleDocsException();
    }
	
	public User findUser(String username) throws LoginBubbleDocsException {
		for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	throw new LoginBubbleDocsException();
	}

}
