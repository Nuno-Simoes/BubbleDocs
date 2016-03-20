package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.*;
import pt.ulisboa.tecnico.bubbledocs.integration.CreateSpreadsheetIntegrator;

public class CreateSpreadsheetIntegratorTest extends BubbleDocsIntegratorTest {
	
	private String lars;
	private String logcav;
	
	private static final String USERNAME = "ars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String PASSWORD = "random";
    private static final String SHEET_NAME = "name";
    private static final String NO_PERMISSION_USERNAME = "sam";
    private static final String NO_PERMISSION_NAME = "Sam";
    private static final String NO_PERMISSION_EMAIL = "sam@sam.com";
    private static final String NOT_LOGGED_USERNAME = "cavaco";
    private static final String NOT_LOGGED_NAME = "Cavaco Silva";
    private static final String NOT_LOGGED_EMAIL = "cavaquinho@presidencia.pt";
	
    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
        ars.setPassword(PASSWORD);
        
    	User sam = createUser(NO_PERMISSION_USERNAME, NO_PERMISSION_NAME, 
    			NO_PERMISSION_EMAIL);
    	sam.setPassword(PASSWORD);
    	
    	User cavaquinho = createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME,
    			NOT_LOGGED_EMAIL);
    	
    	createSpreadSheet(sam, SHEET_NAME, 20, 20);
    	
    	lars = addUserToSession("ars");
    	logcav = cavaquinho.getToken();
    	addUserToSession("sam");
    }
	
    @Test
	public void success() {
		CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(lars, 
				SHEET_NAME, 20, 20);
		integrator.execute();
		Spreadsheet s = super.getSpreadSheet(SHEET_NAME);
		assertEquals("ars", s.getOwner());
		assertEquals(SHEET_NAME, s.getName());
		assertEquals(20, s.getLines());
		assertEquals(20, s.getColumns());
	}
    
    @Test
    public void sucess2() {
    	boolean found = false;
    	CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(lars, 
    			SHEET_NAME, 20, 20);
    	integrator.execute();
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
    	CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(lars,
    			SHEET_NAME, 20, 20);
    	integrator.execute();
    	Spreadsheet s = super.getSpreadSheet(SHEET_NAME);
    	for (Permission p : s.getPermissionsSet()) {
    		if (p.getWrite()) {
    			found = true;
    		}
    	}
    	assertTrue(found);
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(logcav,
    			SHEET_NAME, 20, 20);
    	integrator.execute();
    }
    
    @Test(expected = EmptySpreadsheetNameException.class)
    public void emptySpreadsheetName() {
    	CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(lars, "", 20, 20);
    	integrator.execute();
    }
    
    @Test(expected = InvalidSpreadsheetSizeException.class)
    public void invalidSpreadsheetLineSize() {
    	CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(lars, SHEET_NAME, -1, 20);
    	integrator.execute();
    }
    
    @Test(expected = InvalidSpreadsheetSizeException.class)
    public void invalidSpreadsheetColumnSize() {
    	CreateSpreadsheetIntegrator integrator = new CreateSpreadsheetIntegrator(lars, SHEET_NAME, 20, 0);
    	integrator.execute();
    }   
}