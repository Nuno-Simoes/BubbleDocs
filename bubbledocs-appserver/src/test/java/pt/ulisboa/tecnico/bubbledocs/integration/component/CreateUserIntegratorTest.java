package pt.ulisboa.tecnico.bubbledocs.integration.component;

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
import pt.ulisboa.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegratorTest extends BubbleDocsIntegratorTest {

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
    	CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
        integrator.execute();

        User user = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

        assertEquals(USERNAME_DOES_NOT_EXIST, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(NAME, user.getName());
        assertEquals(null, user.getPassword());
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	CreateUserIntegrator integrator = new CreateUserIntegrator(logquim, 
    			USERNAME, EMAIL, NAME);
    	integrator.execute();
    }
    
    @Test(expected = DuplicateUsernameException.class)
    public void usernameExists() {
        CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME, EMAIL, NAME);
        integrator.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooLong() {
    	CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME_TOO_LONG, EMAIL, NAME);
    	integrator.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void usernameTooShort() {
    	CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME_TOO_SHORT, EMAIL, NAME);
    	integrator.execute();
    }
    
    @Test(expected = InvalidUsernameException.class)
    public void emptyUsername() {
        CreateUserIntegrator integrator = new CreateUserIntegrator(root, "", EMAIL, NAME);
        integrator.execute();
    }
	
    @Test(expected = InvalidPermissionException.class)
    public void unauthorizedUserCreation() {
        CreateUserIntegrator integrator = new CreateUserIntegrator(lsmf, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
        integrator.execute();
    }
	
    @Test(expected = LoginBubbleDocsException.class)
    public void accessUsernameNotExist() {
        removeUserFromSession(root);
        CreateUserIntegrator integrator = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
        integrator.execute();
    }
    
    @Mocked
    IDRemoteServices remote;
    
    @Test(expected = UnavailableServiceException.class)
    public void unavailableService() {
    	
    	new Expectations() {{
    		remote.createUser(anyString, anyString);
    		result = new RemoteInvocationException();
    	}};
    	
		new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME).execute();
    }
    
    
    @Test(expected = LoginBubbleDocsException.class) 
    public void compensationDone() {
    	
    	new Expectations() {{
    		remote.createUser(anyString, anyString);
    		result = new RemoteInvocationException();
    	}};
    	
    	try {
    		new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME).execute();
    	}catch(UnavailableServiceException use) {
    		super.getUserFromUsername(USERNAME_DOES_NOT_EXIST);
    	}
    }  
}