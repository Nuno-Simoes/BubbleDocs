package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;

public class AssignLiteralToCellServiceTest extends BubbleDocsServiceTest {

    // the tokens
    private String ars;
    private String sam;
    private int id;

    private static final String USERNAME = "ars";
    private static final String NO_PERMISSION_USER = "sam";
    private static final String PASSWORD = "ars";
    private static final String NAME = "whatever";
    private static final int ID_DOES_NOT_EXIST = 100;
    
    @Override
    public void populate4Test() {
        User u = createUser(USERNAME, PASSWORD, "António Rito Silva");
        createUser(NO_PERMISSION_USER, PASSWORD, "Sam");
        Spreadsheet s = createSpreadSheet(u, NAME, 10, 10);
        s.getCell(7, 3).setIsProtected(true);
        id = s.getId();
        ars = addUserToSession("ars");
        sam = addUserToSession("sam");
    }

    @Test
    public void success() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(ars,
    			id, "1;5","3");
        service.execute();
        Spreadsheet sheet = super.getSpreadSheet(NAME);
        assertEquals(3, sheet.getCell(1, 5).getContent().getResult());
    }

    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void spreadsheetDoesNotExist() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(ars,
    			ID_DOES_NOT_EXIST, "1;5","3");
        service.execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void invalidUser() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(sam,
    			id, "1;5","3");
        service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(ars,
    			id, "19;5","3");
        service.execute();
    }

    @Test(expected = InvalidContentException.class)
    public void notALiteral() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(ars,
    			id, "1;5","ola");
        service.execute();
    }
    
    @Test(expected = InvalidPermissionException.class)
    public void protectedCell() {
    	AssignLiteralToCellService service = new AssignLiteralToCellService(ars,
    			id, "7;3","3");
        service.execute();
    }

}
