package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ProtectedCellException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignReferenceToCellIntegrator;

public class AssignReferenceToCellIntegratorTest extends BubbleDocsIntegratorTest {

    // the tokens
	private String logars;
	private String logsam;
	private String logjoaoj;
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
    private static final String NOT_LOGGED_USERNAME = "joaoj";
    private static final String NOT_LOGGED_NAME = "João Jardim";
    private static final String NOT_LOGGED_EMAIL = "joaoj@madeira.pt";
    
    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
        ars.setPassword(PASSWORD);
        
        User sam = createUser(NO_PERMISSION_USERNAME, NO_PERMISSION_NAME, 
        		NO_PERMISSION_EMAIL);
        sam.setPassword(PASSWORD);
        
        User joaoj = createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME,
        		NOT_LOGGED_EMAIL);
        
        Spreadsheet s = createSpreadSheet(ars, SPREADNAME, 10, 10);
        s.getCell(7, 3).setIsProtected(true);
        id = s.getId();
        logjoaoj = joaoj.getToken();
        logars = addUserToSession(USERNAME);
        logsam = addUserToSession(NO_PERMISSION_USERNAME);
    }

    @Test
    public void success() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logars,
    			id, "1;1","3;3");
        integrator.execute();
        Spreadsheet sheet = super.getSpreadSheet(SPREADNAME);
        Reference ref = (Reference) sheet.getCell(1, 1).getContent();
        assertEquals(3, ref.getReferencedCell().getLine(), 0);
        assertEquals(3, ref.getReferencedCell().getColumn(), 0);
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logjoaoj,
    				id, "1;1", "3;3");
    	integrator.execute();
    }

    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void spreadsheetDoesNotExist() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logars,
    			ID_DOES_NOT_EXIST, "1;1","3;3");
        integrator.execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void invalidUser() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logsam,
    			id, "1;1","3;3");
        integrator.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logars,
    			id, "30;5","3;2");
        integrator.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds2() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logars,
    			id, "3;2","30;5");
        integrator.execute();
    }

    @Test(expected = InvalidContentException.class)
    public void notAReference() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logars,
    			id, "1;1","failed");
        integrator.execute();
    }
    
    @Test(expected = ProtectedCellException.class)
    public void protectedCell() {
    	AssignReferenceToCellIntegrator integrator = new AssignReferenceToCellIntegrator(logars,
    			id, "7;3","3;7");
        integrator.execute();
    }

}
