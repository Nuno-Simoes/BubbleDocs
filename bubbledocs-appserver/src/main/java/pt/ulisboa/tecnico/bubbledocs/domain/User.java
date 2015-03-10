package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.List;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUserException;

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
   
   public void createSpreadsheet(String name, int lines, int columns) {
	   Spreadsheet s = new Spreadsheet(name, lines, columns);
	   Permission p = new Permission (true, true);
	   int id = this.getPortal().getSheetId();
	   s.setId(id);
	   s.setOwner(this.getUsername());
	   this.getPortal().addSpreadsheets(s);
	   this.getPortal().setSheetId(id+1);
	   this.addPermissions(p);
	   s.addPermissions(p);
   }
   
   //Não tem permissoes suficientes exception?
   public void modifyPermissions (String username, int sheetId, boolean read, boolean write) 
		   throws InvalidUserException {
	   Permission p = this.findPermission(username, sheetId);
	   User u = this.getPortal().findUser(username);
	   Spreadsheet s = this.getPortal().findSpreadsheet(sheetId);

	   if(this.getPortal().isOwner(u, s) || p.getWrite()) {
		   p.setRead(read);
		   p.setWrite(write);
	   }   
	   throw new InvalidUserException(username); 
   }
   
   
   public Permission findPermission (String username, int sheetId) throws InvalidUserException {
	   User u = this.getPortal().findUser(username);
	   Spreadsheet s = this.getPortal().findSpreadsheet(sheetId);
	   
	   for (Permission p : this.getPermissionsSet()) {
		   if(p.getUser().equals(u) && p.getSpreadsheet().equals(s)) {
			   return p;
		   }
	   }
	   throw new InvalidUserException(username);
   }
   
}
