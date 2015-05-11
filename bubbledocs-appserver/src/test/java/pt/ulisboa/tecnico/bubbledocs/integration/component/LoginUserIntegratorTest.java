package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUserService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

// add needed import declarations

public class LoginUserIntegratorTest extends BubbleDocsIntegratorTest {

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
    
    @Test
    public void success() {
        LoginUserIntegrator integrator = 
        		new LoginUserIntegrator(USERNAME, PASSWORD);
        integrator.execute();
        
        User u = super.getUserFromUsername(USERNAME);
        String token = u.getToken();
        
        assertEquals(u, super.getUserFromSession(token));
        
        float difference = (getLastAccessTimeInSession(token) - 
        		(float) (System.currentTimeMillis()/3600000));

        assertTrue("Access time in session not correctly set", difference >= 0);
        assertTrue("diference in seconds greater than expected", difference < 2);
    }
        
    @Test
    public void successLoginTwice() {    	
        LoginUserIntegrator integrator = 
        		new LoginUserIntegrator(USERNAME, PASSWORD);
        
        integrator.execute();
        User u1 = super.getUserFromUsername(USERNAME);
        String token1 = u1.getToken();
        super.getUserFromSession(token1);
        
        integrator.execute();
        User u2 = super.getUserFromUsername(USERNAME);
        String token2 = u2.getToken();
        super.getUserFromSession(token2);
        
        assertEquals(USERNAME, u1.getUsername());
        assertEquals(USERNAME, u2.getUsername());
    }
    
    @Test
    public void changedPassword() {
    	Portal p = Portal.getInstance();
    	User ars = p.findUser(USERNAME);
    	LoginUserService service = 
    			new LoginUserService(USERNAME, NEW_PASSWORD, false);
    	service.execute();

    	assertEquals(NEW_PASSWORD, ars.getPassword());
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void loginUnknownUser() throws Exception {
    	LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME_DOES_NOT_EXIST, PASSWORD);
        integrator.execute();
    } 
    
    @Test(expected = LoginBubbleDocsException.class)
    public void loginUserWithWrongPasswordRemote() throws Exception {
    
        LoginUserIntegrator integrator = new LoginUserIntegrator(USERNAME, WRONG_PASSWORD);
        integrator.execute();
    }
    
    @Test (expected = UnavailableServiceException.class)
    public void unavailableService(@Mocked final IDRemoteServices remote) throws Exception {
    	
    	new Expectations() {{
    		remote.loginUser(anyString, anyString);
    		result = new RemoteInvocationException();
    	}};
    	
    	new LoginUserIntegrator(USERNAME, WRONG_PASSWORD).execute();
    }
}
