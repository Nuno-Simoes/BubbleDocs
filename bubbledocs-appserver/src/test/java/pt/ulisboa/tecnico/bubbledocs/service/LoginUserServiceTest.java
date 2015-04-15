package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

// add needed import declarations

public class LoginUserServiceTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "ars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String PASSWORD = "random";
    private static final String WRONG_PASSWORD = "wrong_random";
    private static final String NEW_PASSWORD = "new_password";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";

    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
    	ars.setPassword(PASSWORD);
    }

    // returns the time of the last access for the user with token userToken.
    // It must get this data from the session object of the application
    private float getLastAccessTimeInSession(String userToken) {
    	Session s = Session.getInstance();
    	User u = s.findUser(userToken);
    	return u.getSessionTime();
    }
    
    @Test
    public void success() {
        LoginUserService service = new LoginUserService(USERNAME, PASSWORD);
        service.execute();
        
        String token = service.getUserToken();

        User user = getUserFromSession(service.getUserToken());
        assertEquals(USERNAME, user.getUsername());
        	
        float difference = (getLastAccessTimeInSession(token) - (float) (System.currentTimeMillis()/3600000));

        assertTrue("Access time in session not correctly set", difference >= 0);
        assertTrue("diference in seconds greater than expected", difference < 2);
    }
    
    @Test
    public void successLoginTwice() {
    	Session s = Session.getInstance();
    	
        LoginUserService service = new LoginUserService(USERNAME, PASSWORD);

        service.execute();
        String token1 = service.getUserToken();

        service.execute();
        String token2 = service.getUserToken();
        
        
        assertFalse(s.isTokenActive(token1));
        User user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
    }
    
    @Test
    public void changedPassword() {
    	Portal p = Portal.getInstance();
    	User ars = p.findUser(USERNAME);
    	LoginUserService service = new LoginUserService(USERNAME, NEW_PASSWORD);
    	service.execute();
    	
    	assertEquals(ars.getPassword(), NEW_PASSWORD);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void loginUnknownUser() {
    	LoginUserService service = new LoginUserService(USERNAME_DOES_NOT_EXIST, PASSWORD);
        service.execute();
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void loginUserWithinWrongPassword() {
        LoginUserService service = new LoginUserService(USERNAME, WRONG_PASSWORD);
        service.execute();
    }
        
    @Mocked
    IDRemoteServices remote;
    
    @Test (expected = UnavailableServiceException.class)
    public void unavailableService() {
    	
    	new Expectations() {{
    		remote.loginUser(anyString, anyString);
    		result = new RemoteInvocationException();
    	}};
    	
    	new LoginUserService(USERNAME, WRONG_PASSWORD);
    	
    }
    
    @Test
    public void success2() {
    	
    	new Expectations() {{
    		remote.loginUser(anyString, anyString);
    		result = new RemoteInvocationException();
    	}};
    	
        LoginUserService service = new LoginUserService(USERNAME, PASSWORD);
        service.execute();
        
        String token = service.getUserToken();

        User user = getUserFromSession(service.getUserToken());
        assertEquals(USERNAME, user.getUsername());
        	
        float difference = (getLastAccessTimeInSession(token) - (float) (System.currentTimeMillis()/3600000));

        assertTrue("Access time in session not correctly set", difference >= 0);
        assertTrue("diference in seconds greater than expected", difference < 2);
    }
    
}