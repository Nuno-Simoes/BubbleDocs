package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

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
	
	public List<Spreadsheet> listSpreadsheets (String username) {
    	List<Spreadsheet> list = new ArrayList<Spreadsheet>();
    	
    	for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			u.listSpreadsheets(list);
    		}
    	} 
    	
    	return list;
    }

	public List<Spreadsheet> listSpreadsheets (User user, String str) {
    	List<Spreadsheet> list = new ArrayList<Spreadsheet>();
    	
    	for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(user.getUsername())) {
    			u.listSpreadsheets(list, str);
    		}
    	} 
    	
    	return list;
    }
    
    public void removeSpreadsheet (User u) {
    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getOwner().equals(u.getUsername())) {
    			s.delete();
    			this.removeSpreadsheets(s);
    		}
    	}
    }
    
    public void removeSpreadsheet (String username, String sheetName) {
    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getOwner().equals(username) && s.getName().equals(sheetName)) {
    			s.delete();
    			this.removeSpreadsheets(s);
    		}
    	}
    }

    public void removeSpreadsheet (int id) throws SpreadsheetDoesNotExistException {
    	Spreadsheet s = this.findSpreadsheet(id);
		s.delete();
    	this.removeSpreadsheets(s);
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
        
    public User findUser (String username) throws UserDoesNotExistException {
    	for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	throw new UserDoesNotExistException(username);
    }
    
    public boolean isOwner (User u, Spreadsheet s) {
    	if(u.getUsername().equals(s.getOwner())){
    		return true;
    	}
    	return false;
    }
    
    public void listUsers () {
    	for (User u : this.getUsersSet()) {
    		System.out.print("Username: " + u.getUsername());
    		System.out.print(", Name: " + u.getName());
    		System.out.println(", Email: " + u.getEmail());
    	}
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
    
    public Spreadsheet importFromXML(org.jdom2.Element element) {
    	Spreadsheet s = new Spreadsheet();
    	this.addSpreadsheets(s);
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
	
	public User findUserByUsername(String username) throws LoginBubbleDocsException {
		for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	throw new LoginBubbleDocsException();
	}

	public void renewPassword(String username, String password) {
		User u = this.findUser(username);
		u.setPassword(password);
	} 
}
