package id.ws.impl;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RemoveUserTest extends IdTest {

	String USERNAME_EXIST = "manuel";
	String USERNAME_DOES_NOT_EXIST = "maria";
	String INVALID_USERNAME = "";
	String SUCCESS = "Success";

	@Test
	public void removeUserTestSuccess() throws Exception {
		port.removeUser(USERNAME_EXIST);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest1() throws Exception {
		port.removeUser(USERNAME_DOES_NOT_EXIST);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest2() throws Exception {
		port.removeUser(INVALID_USERNAME);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest3() throws Exception {
		port.removeUser(null);
	}
	
}