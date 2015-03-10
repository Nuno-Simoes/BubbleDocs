package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.ArrayList;
import java.util.List;

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
    
}