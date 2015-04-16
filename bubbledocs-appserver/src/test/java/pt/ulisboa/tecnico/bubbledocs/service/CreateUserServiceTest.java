package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserServiceTest extends BubbleDocsServiceTest {

    private String root;
    private String lsmf;
    private String logquim;

    private static final String USERNAME = "ars";
    private static final String USERNAME_TOO_SHORT = "ar";
    private static final String USERNAME_TOO_LONG = "arsarsars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String PASSWORD = "random";
    private static final String USERNAME_WITHOUT_PERMISSION = "smf";
    private static final String NAME_WITHOUT_PERMISSION = "Sérgio Fernandes";
    private static final String EMAIL_WITHOUT_PERMISSION = "smf@smf.com";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String ROOT_NAME = "root";
    private static final String NOT_LOGGED_USERNAME = "Quim";
    private static final String NOT_LOGGED_NAME = "Quim Barreiros";
    private static final String NOT_LOGGED_EMAIL = "qbarreiros@garagemdavizinha.pt";

    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
    	ars.setPassword(PASSWORD);
    	
    	User smf = createUser(USERNAME_WITHOUT_PERMISSION, NAME_WITHOUT_PERMISSION, EMAIL_WITHOUT_PERMISSION);
    	smf.setPassword(PASSWORD);
    	
    	User quim = createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
        
    	logquim =quim.getToken();
        root = addUserToSession(ROOT_NAME);
        lsmf = addUserToSession(USERNAME_WITHOUT_PERMISSION);
    }
    
    @Test
    public void success() {
    	CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
        service.execute();

        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
        assertEquals(null, user.getPassword());
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	CreateUserService service = new CreateUserService(logquim, 
    			USERNAME, EMAIL, NAME);
    	service.execute();
    }
    
    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUserService service = new CreateUserService(root, USERNAME, EMAIL, NAME);
        service.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooLong() {
    	CreateUserService service = new CreateUserService(root, USERNAME_TOO_LONG, EMAIL, NAME);
    	service.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooShort() {
    	CreateUserService service = new CreateUserService(root, USERNAME_TOO_SHORT, EMAIL, NAME);
    	service.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
        CreateUserService service = new CreateUserService(root, "", EMAIL, NAME);
        service.execute();
    }
	
    @Test(expected = InvalidPermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUserService service = new CreateUserService(lsmf, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
        service.execute();
    }
	
    @Test(expected = LoginBubbleDocsException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        CreateUserService service = new CreateUserService(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
        service.execute();
    }
    
    @Mocked
    IDRemoteServices remote;
    
    @Test (expected = UnavailableServiceException.class)
    public void unavailableService() {
    	
    	new Expectations() {{
    		remote.createUser(anyString, anyString);
    		result = new RemoteInvocationException();
    	}};
    	
    	new CreateUserService(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME).execute();
    	
    }
    
}