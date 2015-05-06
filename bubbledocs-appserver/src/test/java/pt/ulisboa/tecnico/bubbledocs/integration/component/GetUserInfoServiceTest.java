package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.dto.UserDto;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.service.GetUserInfoService;

public class GetUserInfoServiceTest extends BubbleDocsIntegratorTest {
	
	private static final String CORRECT_USERNAME = "alice";
	private static final String CORRECT_NAME = "alice";
	private static final String CORRECT_EMAIL = "alice@wonderland.com";
	
	private static final String WRONG_USERNAME = "potter";
	private static final String WRONG_NAME = "harry";
	private static final String WRONG_EMAIL = "potter@hogwarts.co.uk";
	
    @Override
    public void populate4Test() {
    	createUser(CORRECT_USERNAME, CORRECT_NAME, CORRECT_EMAIL);
    }
	
    @Test
    public void success() {
    	GetUserInfoService service = new GetUserInfoService(CORRECT_USERNAME);
    	service.execute();
    	UserDto dto = service.getResult();
    	
    	assertEquals(CORRECT_NAME, dto.getName());
    	assertEquals(CORRECT_EMAIL, dto.getEmail());
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void invalidUsername() {
    	GetUserInfoService service = new GetUserInfoService(WRONG_USERNAME);
    	service.execute();
    }
    
    @Test
    public void invalidName() {
    	GetUserInfoService service = new GetUserInfoService(CORRECT_USERNAME);
    	service.execute();
    	UserDto dto = service.getResult();
    	
    	assertEquals(CORRECT_EMAIL, dto.getEmail());
    	assertNotEquals(WRONG_NAME, dto.getName());
    }
    
    @Test
    public void invalidEmail() {
    	GetUserInfoService service = new GetUserInfoService(CORRECT_USERNAME);
    	service.execute();
    	UserDto dto = service.getResult();
    	
    	assertEquals(CORRECT_NAME, dto.getName());
    	assertNotEquals(WRONG_EMAIL, dto.getEmail());
    }
    
    @Test
    public void invalidId() {
    	GetUserInfoService service = new GetUserInfoService(CORRECT_USERNAME);
    	service.execute();
    	UserDto dto = service.getResult();
    	
    	assertEquals(CORRECT_NAME, dto.getName());
    	assertEquals(CORRECT_EMAIL, dto.getEmail());
    }
    
    @Test
    public void invalidToken() {
    	GetUserInfoService service = new GetUserInfoService(CORRECT_USERNAME);
    	service.execute();
    	UserDto dto = service.getResult();
    	
    	assertEquals(CORRECT_NAME, dto.getName());
    	assertEquals(CORRECT_EMAIL, dto.getEmail());
    }
    
    @Test
    public void invalidSessionTime() {
    	GetUserInfoService service = new GetUserInfoService(CORRECT_USERNAME);
    	service.execute();
    	UserDto dto = service.getResult();
    	
    	assertEquals(CORRECT_NAME, dto.getName());
    	assertEquals(CORRECT_EMAIL, dto.getEmail());
    }
           
}