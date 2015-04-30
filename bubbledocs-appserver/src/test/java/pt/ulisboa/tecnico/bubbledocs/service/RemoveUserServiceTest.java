package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RemoveUserServiceTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf";
    private static final String NAME_TO_DELETE = "Sérgio Fernandes";
    private static final String EMAIL_TO_DELETE = "smf@smf.com";
    private static final String USERNAME = "ars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String PASSWORD = "random";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String SPREADSHEET_NAME = "spread";
    private static final String NOT_LOGGED_USERNAME = "zecabra";
    private static final String NOT_LOGGED_NAME = "José Cabra";
    private static final String NOT_LOGGED_EMAIL = "zecabra@deixeitudoporela.pt";
    
    private String root;
    private String logzecabra;
    
    @Override
    public void populate4Test() {
        User ars = createUser(USERNAME, NAME, EMAIL);
        ars.setPassword(PASSWORD);
        
        User smf = createUser(USERNAME_TO_DELETE,
        		NAME_TO_DELETE, EMAIL_TO_DELETE);
        smf.setPassword(PASSWORD);
        
        User zecabra = createUser(NOT_LOGGED_USERNAME,
        		NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
        
        createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);
        
        logzecabra = zecabra.getToken();
        root = addUserToSession(ROOT_USERNAME);
    };
    
    @Test
    public void success() {
    	boolean deletedUser = false;
    	boolean deletedSpreadsheet = false;
    	
        RemoveUserService service = new RemoveUserService(root, USERNAME_TO_DELETE);
        service.execute();
                
        try {
        	getUserFromUsername(USERNAME_TO_DELETE);
        } catch (LoginBubbleDocsException une) {
        	deletedUser = true;
        }
        assertTrue("user was not deleted", deletedUser);
        
        try {
            getSpreadSheet(SPREADSHEET_NAME);
        } catch (SpreadsheetDoesNotExistException sne) {
        	deletedSpreadsheet = true;
        }
        assertTrue("User spreadsheets were not deleted", deletedSpreadsheet);        
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is in session Test if user and session are both deleted
     */
    @Test
    public void successToDeleteIsInSession() {
    	boolean deleted = false;
        String token = addUserToSession(USERNAME_TO_DELETE);
        
        RemoveUserService service = new RemoveUserService(root, USERNAME_TO_DELETE);
        service.execute();
        
        try { 
        	getUserFromSession(token); 
        } catch (LoginBubbleDocsException le) {
        	deleted = true;
        }
        assertTrue("Removed user but not removed from session", deleted);
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	RemoveUserService service = new RemoveUserService(logzecabra,
    			USERNAME_DOES_NOT_EXIST);;
    	service.execute();
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void userToDeleteDoesNotExist() {
        new RemoveUserService(root, USERNAME_DOES_NOT_EXIST).execute();
    }

    @Test(expected = InvalidPermissionException.class)
    public void notRootUser() {
        String ars = addUserToSession(USERNAME);
        new RemoveUserService(ars, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void rootNotInSession() {
        removeUserFromSession(root);

        new RemoveUserService(root, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void notInSessionAndNotRoot() {
        String ars = addUserToSession(USERNAME);
        removeUserFromSession(ars);
        
        new RemoveUserService(ars, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void accessUserDoesNotExist() {
        new RemoveUserService(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE).execute();
    }
    
    /* Corrigir 
    @Mocked IDRemoteServices remote;
    @Test(expected = UnavailableServiceException.class)
    public void unavailableService() {
    	
    	new Expectations() {{
    		remote.removeUser(anyString); 
    		result = new RemoteInvocationException();
    	}};
    	
        new RemoveUserService(root, USERNAME_TO_DELETE).execute();
    } */
}