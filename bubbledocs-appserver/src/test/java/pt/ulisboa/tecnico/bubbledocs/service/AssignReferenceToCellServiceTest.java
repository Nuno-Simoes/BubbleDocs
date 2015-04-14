package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;

public class AssignReferenceToCellServiceTest extends BubbleDocsServiceTest {

    // the tokens
	private String logars;
	private String logsam;
	private int id;

	private static final String USERNAME = "ars";
	private static final String NAME = "António Rito Silva";
	private static final String EMAIL = "ars@ars.com";
	private static final String NO_PERMISSION_USERNAME = "sam";
	private static final String NO_PERMISSION_NAME = "Sam";
	private static final String NO_PERMISSION_EMAIL = "sam@sam.com";
	private static final String PASSWORD = "randomm";
    private static final String SPREADNAME = "teste";
    private static final int ID_DOES_NOT_EXIST = 100;
    
    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
        ars.setPassword(PASSWORD);
        
        User sam = createUser(NO_PERMISSION_USERNAME, NO_PERMISSION_NAME, 
        		NO_PERMISSION_EMAIL);
        sam.setPassword(PASSWORD);
        
        Spreadsheet s = createSpreadSheet(ars, SPREADNAME, 10, 10);
        s.getCell(7, 3).setIsProtected(true);
        id = s.getId();
        logars = addUserToSession(USERNAME);
        logsam = addUserToSession(NO_PERMISSION_USERNAME);
    }

    @Test
    public void success() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logars,
    			id, "1;1","3;3");
        service.execute();
        Spreadsheet sheet = super.getSpreadSheet(SPREADNAME);
        Reference ref = (Reference) sheet.getCell(1, 1).getContent();
        assertEquals(3, ref.getReferencedCell().getLine(), 0);
        assertEquals(3, ref.getReferencedCell().getColumn(), 0);
    }

    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void spreadsheetDoesNotExist() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logars,
    			ID_DOES_NOT_EXIST, "1;1","3;3");
        service.execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void invalidUser() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logsam,
    			id, "1;1","3;3");
        service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logars,
    			id, "30;5","3;2");
        service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds2() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logars,
    			id, "3;2","30;5");
        service.execute();
    }

    @Test(expected = InvalidContentException.class)
    public void notAReference() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logars,
    			id, "1;1","failed");
        service.execute();
    }
    
    @Test(expected = InvalidPermissionException.class)
    public void protectedCell() {
    	AssignReferenceCellService service = new AssignReferenceCellService(logars,
    			id, "7;3","3;7");
        service.execute();
    }

}
