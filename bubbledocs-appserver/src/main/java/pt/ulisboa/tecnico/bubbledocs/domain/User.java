package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.List;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

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
   
   public void listSpreadsheets (List<Spreadsheet> list) {
	   for (Spreadsheet s : this.getPortal().getSpreadsheetsSet()) {
		   if (this.getUsername().equals(s.getOwner())) {
			   list.add(s);
		   }
	   }
   }
   
   public Spreadsheet createSpreadsheet(String name, int lines, int columns) {
	   Portal portal = Portal.getInstance();
	   Spreadsheet s = new Spreadsheet(name, lines, columns);
	   Permission p = new Permission (true, true);
	   int id = portal.getSheetId();
	   
	   s.setId(id);
	   s.setOwner(this.getUsername());
	   portal.addSpreadsheets(s);
	   portal.setSheetId(id+1);
	   
	   this.addPermissions(p);
	   p.setUser(this);
	   p.setSpreadsheet(s);
	   s.addPermissions(p);
	   
	   return s;
   }
   
   public void modifyPermissions (String username, int sheetId, boolean read, 
		   boolean write) throws UserDoesNotExistException, 
		   SpreadsheetDoesNotExistException, InvalidPermissionException {
	   Portal portal = Portal.getInstance();
	   Permission p = findPermission(username, sheetId);
	   User u = portal.findUser(username);
	   Spreadsheet s = portal.findSpreadsheet(sheetId);

	   if (portal.isOwner(u, s) || p.getWrite()) {
		   p.setRead(read);
		   p.setWrite(write);
	   } else throw new InvalidPermissionException(username);	   
   }
   
   public void modifyProtection (int sheetId, int line, 
		   int column, boolean protection) throws UserDoesNotExistException, 
		   SpreadsheetDoesNotExistException, InvalidPermissionException, 
   		   OutOfBoundsException {
	   Portal portal = Portal.getInstance();
	   Permission p = findPermission(this.getUsername(), sheetId);
	   Spreadsheet s = portal.findSpreadsheet(sheetId);

	   if (portal.isOwner(this, s) || p.getWrite()) {
		   Cell c = s.getCell(line, column);
		   c.setIsProtected(protection);
	   } else throw new InvalidPermissionException(this.getUsername());	   
   }
   
   
   public Permission findPermission (String username, int sheetId) 
		   throws UserDoesNotExistException, SpreadsheetDoesNotExistException, 
		   InvalidPermissionException {
	   Portal portal = Portal.getInstance();
	   User u = portal.findUser(username);
	   Spreadsheet s = portal.findSpreadsheet(sheetId);
	   
	   for (Permission p : this.getPermissionsSet()) {
		   if(p.getUser().equals(u) && p.getSpreadsheet().equals(s)) {
			   return p;
		   }
	   }
	   throw new InvalidPermissionException(username);
   }
   
   public void setContent (int sheetId, int line, int column, Content content)
	   		throws UserDoesNotExistException, SpreadsheetDoesNotExistException, 
	   		InvalidPermissionException, OutOfBoundsException {
	   Portal portal = Portal.getInstance();
	   Permission p = findPermission(this.getUsername(), sheetId);
	   Spreadsheet s = portal.findSpreadsheet(sheetId);
	   
	   if (portal.isOwner(this, s) || p.getWrite()) {
		   s.setContent(line, column, content);
	   }
   }
      
}
