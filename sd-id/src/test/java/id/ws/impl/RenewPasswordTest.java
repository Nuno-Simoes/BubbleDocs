package id.ws.impl;

import static org.junit.Assert.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RenewPasswordTest extends IdTest {

	String USERNAME_EXIST = "alice";
	String USERNAME_DOES_NOT_EXIST = "manuel";
	String INVALID_USERNAME = "";
	String SUCCESS = "Success";

	@Test
	public void renewPasswordTestSuccess() throws Exception {
		boolean returned = false;
		User alice = getUserFromUsername(USERNAME_EXIST);
		String firstPassword = alice.getPassword();
		id.renewPassword(USERNAME_EXIST);
		if (false == firstPassword.equals(alice.getPassword())) {
			returned = true;
		}
		assertTrue(SUCCESS, returned);

	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest1() throws Exception {
		id.renewPassword(USERNAME_DOES_NOT_EXIST);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest2() throws Exception {
		id.renewPassword(INVALID_USERNAME);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest3() throws Exception {
		id.renewPassword(null);
	}

}