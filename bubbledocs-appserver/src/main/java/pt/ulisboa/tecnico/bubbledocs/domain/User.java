package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptySpreadsheetNameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSpreadsheetSizeException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class User extends User_Base {
	
	public User() {
		super();
	}

	public User(String username, String name, String email) {
		super();
		this.init(username, name, email);
	}

	protected void init(String username, String name, String email) {
		this.setUsername(username);
		this.setName(name);
		this.setEmail(email);
		this.setSessionTime(0);
		this.setToken(null);
	}

	public Spreadsheet createSpreadsheet(String name, int lines, int columns) 
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
		portal.setSheetId(id+1);

		p.setUser(this);
		p.setSpreadsheet(s);
		s.addPermissions(p);
		this.addPermissions(p);
		portal.addSpreadsheets(s);
		return s;
	}

	public void modifyPermissions (String username, int sheetId, boolean read, 
			boolean write) {
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

	public Permission findPermission (String username, int sheetId) 
			throws InvalidPermissionException {
		Portal portal = Portal.getInstance();
		User u = portal.findUser(username);
		Spreadsheet s = portal.findSpreadsheet(sheetId);


		for (Permission p : u.getPermissionsSet()) {
			if(p.getUser().equals(u) && p.getSpreadsheet().equals(s)) {
				return p;
			}
		}
		throw new InvalidPermissionException(username);
	}

	public void setContent (int sheetId, int line, int column, Content content) {
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

	@Override
	public void setUsername(String username) throws LoginBubbleDocsException {
		if (username.equals("") || username.length() < 3 || username.length() > 8) {
			throw new LoginBubbleDocsException();
		}
		super.setUsername(username);
	}

}