package pt.ulisboa.tecnico.bubbledocs.domain;

import java.util.List;

import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptySpreadsheetNameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetSizeException;
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
   
   public void createSpreadsheet(String name, int lines, int columns) 
		   throws EmptySpreadsheetNameException, InvalidSpreadsheetSizeException {
	   Portal portal = Portal.getInstance();
	   
	   if (name.equals("")) {
		   throw new EmptySpreadsheetNameException();
	   }
	   
	   if (lines <= 0 || columns <= 0) {
		   throw new InvalidSpreadsheetSizeException();
	   }
	   
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
   }
   
   public void modifyPermissions (String username, int sheetId, boolean read, 
		   boolean write) throws UserDoesNotExistException, 
		   SpreadsheetDoesNotExistException {
	   Portal portal = Portal.getInstance();
	   Permission p = findPermission(this.getUsername(), sheetId);
	   User u = portal.findUser(username);
	   Spreadsheet s = portal.findSpreadsheet(sheetId);

	   if (portal.isOwner(u, s) || p.getWrite()) {
		   Permission pr;
		   try {
			   pr = this.findPermission(username, sheetId); 
		   } catch (InvalidPermissionException e) {
			   pr = new Permission(read,write);
			   u.addPermissions(pr);
			   pr.setUser(u);
			   pr.setSpreadsheet(s);
			   s.addPermissions(pr);
		   }
		   pr.setRead(read);
		   pr.setWrite(write);
	   }	   
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
   
   public void delete() {
	   for (Permission p : this.getPermissionsSet()) {
		   p.delete();
		   this.removePermissions(p);
	   }
   }
      
}
