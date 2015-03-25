package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.WrongPasswordException;

// add needed import declarations

public class LoginUserTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jp";
    private static final String PASSWORD = "jp#";

    @Override
    public void populate4Test() {
        createUser(USERNAME, PASSWORD, "João Pereira");
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

    @Test(expected = UserDoesNotExistException.class)
    public void loginUnknownUser() {
        LoginUserService service = new LoginUserService("jp2", "jp");
        service.execute();
    }

    @Test(expected = WrongPasswordException.class)
    public void loginUserWithinWrongPassword() {
        LoginUserService service = new LoginUserService(USERNAME, "jp2");
        service.execute();
    }
}