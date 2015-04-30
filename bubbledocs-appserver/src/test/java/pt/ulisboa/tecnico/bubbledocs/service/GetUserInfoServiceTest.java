package pt.ulisboa.tecnico.bubbledocs.service;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class GetUserInfoServiceTest extends BubbleDocsServiceTest {
	
	private static final String CORRECT_USERNAME = "alice";
	private static final String CORRECT_NAME = "alice";
	private static final String CORRECT_EMAIL = "alice@wonderland.com";
	
	private static final String WRONG_USERNAME = "hpotter";
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