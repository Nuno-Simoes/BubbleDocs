package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.service.RenewPasswordService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordServiceTest extends BubbleDocsServiceTest {
	
	private String logars;
	private String logcbranco;
	
    private static final String USERNAME = "ars";
    private static final String NAME = "António Rito Silva";
    private static final String EMAIL = "ars@ars.com";
    private static final String PASSWORD = "random";
    private static final String NOT_LOGGED_USERNAME = "cbranco";
    private static final String NOT_LOGGED_NAME = "José Castelo Branco";
    private static final String NOT_LOGGED_EMAIL = "cbranco@vip.pt";
    

    @Override
    public void populate4Test() {
    	User ars = createUser(USERNAME, NAME, EMAIL);
    	ars.setPassword(PASSWORD);
    	
    	User cbranco = createUser(NOT_LOGGED_USERNAME, 
    			NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
    	
    	logcbranco = cbranco.getToken();
    	logars = addUserToSession(USERNAME);
    }
	
    @Test
	public void success(){
    	boolean pass = false;
    	RenewPasswordService service = new RenewPasswordService(logars);
    	service.execute();
    	User user = getUserFromSession(logars);
    	
    	if(user.getPassword() == null) {
    		pass = true;
    	}
    		
    	assertTrue("Local copy was not set invalid" , pass);
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void userNotLogged() {
    	RenewPasswordService service = new RenewPasswordService(logcbranco);
    	service.execute();
    }
	
	@Mocked IDRemoteServices remote;
    @Test(expected = UnavailableServiceException.class)
    public void unavailableService() {
    	
    	new Expectations() {{
    		remote.renewPassword(anyString); 
    		result = new RemoteInvocationException();
    	}};
    	
        new RenewPasswordService(logars).execute();
    }

}
