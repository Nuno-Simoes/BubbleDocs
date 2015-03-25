package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;

import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;

public class RemoveUserTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf";
    private static final String USERNAME = "ars";
    private static final String PASSWORD = "ars";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String SPREADSHEET_NAME = "spread";

    // the tokens for user root
    private String root;

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "António Rito Silva");
        User smf = createUser(USERNAME_TO_DELETE, "smf", "Sérgio Fernandes");
        createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
        RemoveUserService service = new RemoveUserService(root, USERNAME_TO_DELETE);
        service.execute();

        boolean deleted = getUserFromUsername(USERNAME_TO_DELETE) == null;

        assertTrue("user was not deleted", deleted);

        assertNull("Spreadsheet was not deleted",
                getSpreadSheet(SPREADSHEET_NAME));
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is not in session
     */
    @Test
    public void successToDeleteIsNotInSession() {
        success();
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is in session Test if user and session are both deleted
     */
    @Test
    public void successToDeleteIsInSession() {
        String token = addUserToSession(USERNAME_TO_DELETE);
        success();
	assertNull("Removed user but not removed from session", getUserFromSession(token));
    }

    @Test(expected = SpreadsheetDoesNotExistException.class)
    public void userToDeleteDoesNotExist() {
        new RemoveUserService(root, USERNAME_DOES_NOT_EXIST).execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void notRootUser() {
        String ars = addUserToSession(USERNAME);
        new RemoveUserService(ars, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotLoggedException.class)
    public void rootNotInSession() {
        removeUserFromSession(root);

        new RemoveUserService(root, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotLoggedException.class)
    public void notInSessionAndNotRoot() {
        String ars = addUserToSession(USERNAME);
        removeUserFromSession(ars);
        
        new RemoveUserService(ars, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotLoggedException.class)
    public void accessUserDoesNotExist() {
        new RemoveUserService(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE).execute();
    }
}