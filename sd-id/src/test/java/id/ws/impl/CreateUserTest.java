package id.ws.impl;

import static org.junit.Assert.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;

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
	String INVALID_USERNAME = "";

	@Test
	public void createNewUsernameSuccess() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_DOES_NOT_EXIST);

		User u = getUserFromUsername(USERNAME_DOES_NOT_EXIST);

		assertEquals(USERNAME_DOES_NOT_EXIST, u.getUserId());
		assertEquals(EMAIL_DOES_NOT_EXIST, u.getEmailAddress());
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest1() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL1);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest2() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL2);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest3() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL3);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest4() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL4);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest5() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL5);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmailTest6() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, INVALID_EMAIL6);
	}

	@Test(expected = EmailAlreadyExists_Exception.class)
	public void emailAlreadyExistsTest() throws Exception {
		id.createUser(USERNAME_DOES_NOT_EXIST, EMAIL_EXIST);
	}

	@Test(expected = UserAlreadyExists_Exception.class)
	public void userAlreadyExistsTest() throws Exception {
		id.createUser(USERNAME_EXIST, EMAIL_DOES_NOT_EXIST);
	}

	@Test(expected = InvalidUser_Exception.class)
	public void invalidUserTest1() throws Exception {
		id.createUser(INVALID_USERNAME, EMAIL_DOES_NOT_EXIST);
	}

	@Test(expected = InvalidUser_Exception.class)
	public void invalidUserTest2() throws Exception {
		id.createUser(null, EMAIL_DOES_NOT_EXIST);
	}
	
}