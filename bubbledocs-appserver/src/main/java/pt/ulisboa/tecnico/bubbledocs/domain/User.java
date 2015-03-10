package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.List;

public class User extends User_Base {
    
    public User() {
        super();
    }
   
   public User(String username, String name, String password) {
	   super();
	   this.init(username, name, password);
   }
   
   protected void init(String username, String name, String password) {
	   this.setUsername(username);
	   this.setName(name);
	   this.setPassword(password);
   }
   
   public void listSpreadsheets (List<Spreadsheet> list, String str) {
	   for (Spreadsheet s : this.getPortal().getSpreadsheetsSet()) {
		   if (this.getUsername().equals(s.getOwner())) {
			   list.add(s);
		   }
	   }
   }
    
}
