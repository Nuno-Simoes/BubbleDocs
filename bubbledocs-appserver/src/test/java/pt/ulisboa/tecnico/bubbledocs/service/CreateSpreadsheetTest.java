package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.*;

public class CreateSpreadsheetTest extends BubbleDocsServiceTest {
	
	private String ars;
	
    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String SHEET_NAME = "name";
    private static final String NO_PERMISSION_USER = "sam";
	
    @Override
    public void populate4Test() {
    	User u = createUser(USERNAME, PASSWORD, "António Rito Silva");
    	createUser(NO_PERMISSION_USER, PASSWORD, "Sam");
    	createSpreadSheet(u, SHEET_NAME, 20, 20);
    	ars = addUserToSession("ars");
    	addUserToSession("sam");
    }
	
    @Test
	public void success() {
		CreateSpreadsheetService service = new CreateSpreadsheetService(ars, SHEET_NAME, 20, 20);
		service.execute();
		Spreadsheet s = super.getSpreadSheet(SHEET_NAME);
		assertEquals("ars", s.getOwner());
		assertEquals(SHEET_NAME, s.getName());
		assertEquals(20, s.getLines());
		assertEquals(20, s.getColumns());
	}
    
    @Test
    public void sucess2() {
    	boolean found = false;
    	CreateSpreadsheetService service = new CreateSpreadsheetService(ars, SHEET_NAME, 20, 20);
    	service.execute();
    	Spreadsheet s = super.getSpreadSheet(SHEET_NAME);
    	for (Permission p : s.getPermissionsSet()) {
    		if (p.getUser().getUsername().equals("sam")) {
    			found = true;
    		}
    	}
    	assertFalse(found);
    }
    
    @Test
    public void sucess3() {
    	boolean found = false;
    	CreateSpreadsheetService service = new CreateSpreadsheetService(ars, SHEET_NAME, 20, 20);
    	service.execute();
    	Spreadsheet s = super.getSpreadSheet(SHEET_NAME);
    	for (Permission p : s.getPermissionsSet()) {
    		if (p.getWrite()) {
    			found = true;
    		}
    	}
    	assertTrue(found);
    }
    
    @Test(expected = EmptySpreadsheetNameException.class)
    public void emptySpreadsheetName() {
    	CreateSpreadsheetService service = new CreateSpreadsheetService(ars, "", 20, 20);
    	service.execute();
    }
    
    @Test(expected = InvalidSpreadsheetSizeException.class)
    public void invalidSpreadsheetLineSize() {
    	CreateSpreadsheetService service = new CreateSpreadsheetService(ars, SHEET_NAME, -1, 20);
    	service.execute();
    }
    
    @Test(expected = InvalidSpreadsheetSizeException.class)
    public void invalidSpreadsheetColumnSize() {
    	CreateSpreadsheetService service = new CreateSpreadsheetService(ars, SHEET_NAME, 20, 0);
    	service.execute();
    }
    
}