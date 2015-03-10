package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUserException;

public class Portal extends Portal_Base {
    
	private static Portal instance = null;
	
    protected Portal() {
        super();
        this.setSheetId(0);
        this.setUserId(0);
    }
    
    public static Portal getInstance() {
    	if (instance==null) {
    		instance = new Portal();
    	}
    	
    	return instance;
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

    public void removeSpreadsheet (int id) throws InvalidSpreadsheetException {
    	Spreadsheet s = this.findSpreadsheet(id);
    	this.removeSpreadsheets(s);
    }
    
    public Spreadsheet findSpreadsheet (int id) throws InvalidSpreadsheetException {
    	for (Spreadsheet s : this.getSpreadsheetsSet()) {
    		if (s.getId() == id) {
    			return s;
    		}
    	}
    	throw new InvalidSpreadsheetException(Integer.toString(id)); 
    }
    
    public User findUser (String username) throws InvalidUserException {
    	for (User u : this.getUsersSet()) {
    		if (u.getUsername().equals(username)) {
    			return u;
    		}
    	}
    	throw new InvalidUserException(username);
    }
    
    public boolean isOwner (User u, Spreadsheet s) {
    	if(u.getUsername().equals(s.getOwner())){
    		return true;
    	}
    	return false;
    }
    
}