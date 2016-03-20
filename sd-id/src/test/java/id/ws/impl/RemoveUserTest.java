package id.ws.impl;

import static org.junit.Assert.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RemoveUserTest extends IdTest {

	String USERNAME_EXIST = "alice";
	String USERNAME_DOES_NOT_EXIST = "manuel";
	String INVALID_USERNAME = "";
	String SUCCESS = "Success";

	@Test
	public void removeUserTestSuccess() throws Exception {
		boolean returned = false;
		id.removeUser(USERNAME_EXIST);
		if (getUserFromUsername(USERNAME_EXIST) == null) {
			returned = true;
		}
		assertTrue(SUCCESS, returned);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest1() throws Exception {
		id.removeUser(USERNAME_DOES_NOT_EXIST);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest2() throws Exception {
		id.removeUser(INVALID_USERNAME);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest3() throws Exception {
		id.removeUser(null);
	}
	
}