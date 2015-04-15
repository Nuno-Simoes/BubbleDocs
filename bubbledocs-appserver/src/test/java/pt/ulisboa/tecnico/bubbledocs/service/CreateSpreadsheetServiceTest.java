package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.*;

public class CreateSpreadsheetServiceTest extends BubbleDocsServiceTest {
	
	private String lars;
	
	private static final String USERNAME = "ars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String PASSWORD = "random";
    private static final String SHEET_NAME = "name";
    private static final String NO_PERMISSION_USERNAME = "sam";
    private static final String NO_PERMISSION_NAME = "Sam";
    private static final String NO_PERMISSION_EMAIL = "sam@sam.com";
	
    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
        ars.setPassword(PASSWORD);
        
    	User sam = createUser(NO_PERMISSION_USERNAME, NO_PERMISSION_NAME, 
    			NO_PERMISSION_EMAIL);
    	sam.setPassword(PASSWORD);
    	createSpreadSheet(sam, SHEET_NAME, 20, 20);
    	
    	lars = addUserToSession("ars");
    	addUserToSession("sam");
    }
	
    @Test
	public void success() {
		CreateSpreadsheetService service = new CreateSpreadsheetService(lars, SHEET_NAME, 20, 20);
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
    	CreateSpreadsheetService service = new CreateSpreadsheetService(lars, SHEET_NAME, 20, 20);
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
    	CreateSpreadsheetService service = new CreateSpreadsheetService(lars, SHEET_NAME, 20, 20);
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
    	CreateSpreadsheetService service = new CreateSpreadsheetService(lars, "", 20, 20);
    	service.execute();
    }
    
    @Test(expected = InvalidSpreadsheetSizeException.class)
    public void invalidSpreadsheetLineSize() {
    	CreateSpreadsheetService service = new CreateSpreadsheetService(lars, SHEET_NAME, -1, 20);
    	service.execute();
    }
    
    @Test(expected = InvalidSpreadsheetSizeException.class)
    public void invalidSpreadsheetColumnSize() {
    	CreateSpreadsheetService service = new CreateSpreadsheetService(lars, SHEET_NAME, 20, 0);
    	service.execute();
    }
    
}