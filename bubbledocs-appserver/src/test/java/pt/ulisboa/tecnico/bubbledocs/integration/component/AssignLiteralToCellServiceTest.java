package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralToCellService;


public class AssignLiteralToCellServiceTest extends BubbleDocsServiceTest {

    // the tokens
    private String logars;
    private String logsam;
    private String logp44;
    private int id;
    private int p44id;

    private static final String USERNAME = "ars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String NO_PERMISSION_USERNAME = "sam";
    private static final String NO_PERMISSION_NAME = "Sam";
    private static final String NO_PERMISSION_EMAIL = "sam@sam.com";
    private static final String PASSWORD = "randomm";
    private static final String SPREADNAME = "whatever";
    private static final int ID_DOES_NOT_EXIST = 100;
    private static final String NOT_LOGGED_USERNAME = "preso44";
    private static final String NOT_LOGGED_NAME = "José Sócrates";
    private static final String NOT_LOGGED_EMAIL = "preso44@evora.pt";
    
    @Override
    public void populate4Test() {
        User ars = createUser(USERNAME, NAME, EMAIL);
        ars.setPassword(PASSWORD);
        
        User sam = createUser(NO_PERMISSION_USERNAME, NO_PERMISSION_NAME, 
        		NO_PERMISSION_EMAIL);
        sam.setPassword(PASSWORD);
        
        User preso44 = createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME,
        		NOT_LOGGED_EMAIL);
        
        Spreadsheet ps = createSpreadSheet(preso44, "Contas Suiça", 10, 100);
        Spreadsheet s = createSpreadSheet(ars, SPREADNAME, 10, 10);
        s.getCell(7, 3).setIsProtected(true);
        p44id = ps.getId();
        id = s.getId();
        logp44 = preso44.getToken();
        logars = addUserToSession(USERNAME);
        logsam = addUserToSession(NO_PERMISSION_USERNAME);
    }

    @Test
    public void success() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logars,
    			id, "1;5","3");
        service.execute();
        Spreadsheet sheet = super.getSpreadSheet(SPREADNAME);
        assertEquals(3, sheet.getCell(1, 5).getContent().getResult(), 0);
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logp44,
    			p44id, "1;5", "3");
    	service.execute();
    }

    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void spreadsheetDoesNotExist() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logars,
    			ID_DOES_NOT_EXIST, "1;5","3");
        service.execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void invalidUser() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logsam,
    			id, "1;5","3");
        service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logars,
    			id, "19;5","3");
        service.execute();
    }

    @Test(expected = InvalidContentException.class)
    public void notALiteral() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logars,
    			id, "1;5","ola");
        service.execute();
    }
    
    @Test(expected = InvalidPermissionException.class)
    public void protectedCell() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(logars,
    			id, "7;3","3");
        service.execute();
    }

}
