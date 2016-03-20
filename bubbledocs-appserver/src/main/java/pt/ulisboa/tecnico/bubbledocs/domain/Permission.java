package pt.ulisboa.tecnico.bubbledocs.domain;

public class Permission extends Permission_Base {
    
    public Permission() {
        super();
    }
    
    public Permission (boolean read, boolean write) {
    	super();
    	init(read, write);
    }
    
    protected void init (boolean read, boolean write) {
    	this.setRead(read);
    	this.setWrite(write);
    }
    
    public void delete() {
    	Spreadsheet s = this.getSpreadsheet();
    	
    	if (this.getUser().getUsername().equals(this.getSpreadsheet().getOwner())) {
    		s.delete();
    	}
    		
    	this.setSpreadsheet(null);
    	this.setUser(null);
    }
    
}
