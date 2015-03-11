package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class Portal extends Portal_Base {
    
	public static Portal getInstance() {
		Portal portal = FenixFramework.getDomainRoot().getPortal();
		if (portal == null)
		    portal = new Portal();

		return portal;
	}

	private Portal() {
		FenixFramework.getDomainRoot().setPortal(this);
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
    			this.removeSpreadsheets(s);
    		}
    	}
    }

    public void removeSpreadsheet (int id) throws SpreadsheetDoesNotExistException {
    	Spreadsheet s = this.findSpreadsheet(id);
    	this.removeSpreadsheets(s);
    }
    
    public Spreadsheet findSpreadsheet (int id) throws SpreadsheetDoesNotExistException {
    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getId() == id) {
    			return s;
    		}
    	}
    	throw new SpreadsheetDoesNotExistException(Integer.toString(id)); 
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
    
}
