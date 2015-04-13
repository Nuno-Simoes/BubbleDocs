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
    private String ars;
    private String smf;
    private int id;

    private static final String USERNAME = "ars";
    private static final String NO_PERMISSION_USER = "smf";
    private static final String PASSWORD = "ars";
    private static final String NAME = "teste";
    private static final int ID_DOES_NOT_EXIST = 100;
    
    @Override
    public void populate4Test() {
        User u = createUser(USERNAME, PASSWORD, "António Rito Silva");
        createUser(NO_PERMISSION_USER, PASSWORD, "Smf");
        Spreadsheet s = createSpreadSheet(u, NAME, 10, 10);
        s.getCell(7, 3).setIsProtected(true);
        id = s.getId();
        ars = addUserToSession("ars");
        smf = addUserToSession("smf");
    }

    @Test
    public void success() {
    	AssignReferenceCellService service = new AssignReferenceCellService(ars,
    			id, "1;1","3;3");
        service.execute();
        Spreadsheet sheet = super.getSpreadSheet(NAME);
        Reference ref = (Reference) sheet.getCell(1, 1).getContent();
        assertEquals(3, ref.getReferencedCell().getLine(), 0);
        assertEquals(3, ref.getReferencedCell().getColumn(), 0);
    }

    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void spreadsheetDoesNotExist() {
    	AssignReferenceCellService service = new AssignReferenceCellService(ars,
    			ID_DOES_NOT_EXIST, "1;1","3;3");
        service.execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void invalidUser() {
    	AssignReferenceCellService service = new AssignReferenceCellService(smf,
    			id, "1;1","3;3");
        service.execute();
    }

    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds() {
    	AssignReferenceCellService service = new AssignReferenceCellService(ars,
    			id, "30;5","3;2");
        service.execute();
    }
    
    @Test(expected = OutOfBoundsException.class)
    public void outOfBounds2() {
    	AssignReferenceCellService service = new AssignReferenceCellService(ars,
    			id, "3;2","30;5");
        service.execute();
    }

    @Test(expected = InvalidContentException.class)
    public void notAReference() {
    	AssignReferenceCellService service = new AssignReferenceCellService(ars,
    			id, "1;1","failed");
        service.execute();
    }
    
    @Test(expected = InvalidPermissionException.class)
    public void protectedCell() {
    	AssignReferenceCellService service = new AssignReferenceCellService(ars,
    			id, "7;3","3;7");
        service.execute();
    }

}
