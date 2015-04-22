package id.ws.impl;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;

public class CreateUserTest extends IdTest {

	String USERNAME_EXIST = "alice";
	String EMAIL_EXIST = "alice@tecnico.pt";
	String USERNAME_DOES_NOT_EXIST = "manuel";
	String EMAIL_DOES_NOT_EXIST = "manuel@tecnico.pt";
	String INVALID_EMAIL1 = "a";
	String INVALID_EMAIL2 = "a@";
	String INVALID_EMAIL3 = "@a";
	String INVALID_EMAIL4 = "@";
	String INVALID_EMAIL5 = "@@";
	String INVALID_EMAIL6 = "a@a@a";
	String INVALID_EMAIL7 = "";
	String INVALID_USERNAME = "";

	@Test
	public void createNewUsernameSuccess() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_DOES_NOT_EXIST);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest1() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL1);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest2() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL2);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest3() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL3);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest4() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL4);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest5() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL5);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest6() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL6);
	}
	
	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest7() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL7);
	}

	@Test(expected = EmailAlreadyExists_Exception.class)
	public void emailAlreadyExistsTest() throws Exception {
		port.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_EXIST);
	}

	@Test(expected = UserAlreadyExists_Exception.class)
	public void userAlreadyExistsTest() throws Exception {
		port.createUser(USERNAME_EXIST, EMAIL_DOES_NOT_EXIST);
	}

	@Test(expected = InvalidUser_Exception.class)
	public void invalidUserTest1() throws Exception {
		port.createUser(INVALID_USERNAME, EMAIL_DOES_NOT_EXIST);
	}

	@Test(expected = InvalidUser_Exception.class)
	public void invalidUserTest2() throws Exception {
		port.createUser(null, EMAIL_DOES_NOT_EXIST);
	}
	
}
