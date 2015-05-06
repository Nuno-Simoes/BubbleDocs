package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.service.GetUsername4TokenService;


public class GetUsername4TokenServiceTest extends BubbleDocsIntegratorTest {
	
	private String token;
	
	private static final String CORRECT_USERNAME = "alice";
	private static final String CORRECT_NAME = "alice";
	private static final String CORRECT_EMAIL = "alice@wonderland.com";
	
	private static final String WRONG_USERNAME = "potter";
	private static final String WRONG_NAME = "harry";
	private static final String WRONG_EMAIL = "potter@hogwarts.co.uk";
	
    private static final String NOT_LOGGED_USERNAME = "amalhoa";
    private static final String NOT_LOGGED_NAME = "Ana Malhoa";
    private static final String NOT_LOGGED_EMAIL = "amalhoa@turbinada.pt";
	
	@Override
    public void populate4Test() {
		createUser(CORRECT_USERNAME, CORRECT_NAME, CORRECT_EMAIL);
		token = addUserToSession(CORRECT_USERNAME);
		
		createUser(WRONG_USERNAME, WRONG_NAME, WRONG_EMAIL);
				
		createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
    }
	
	  @Test
	    public void success() {
		  GetUsername4TokenService service = new GetUsername4TokenService(token);	  
		  service.execute();
		  String username = service.getResult();
		  
		  assertEquals(CORRECT_USERNAME, username); 
		  }
	  
	  @Test(expected = InvalidUsernameException.class)
	  public void UserDoesNotExist(){
		  GetUsername4TokenService service = new GetUsername4TokenService(WRONG_USERNAME);
		  service.execute();
	  }
	  
	  @Test(expected = LoginBubbleDocsException.class)
	  public void invalidLogin(){
		  GetUsername4TokenService service = new GetUsername4TokenService(NOT_LOGGED_USERNAME);
		  service.execute();
	  }
}