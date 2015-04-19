package id.ws.impl;


import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class RenewPasswordTest extends IdTest {

	String USERNAME_EXIST = "alice";
	String USERNAME_DOES_NOT_EXIST = "zeca";
	String INVALID_USERNAME = "";
	String SUCCESS = "Success";

	@Test
	public void renewPasswordTestSuccess() throws Exception {
		port.renewPassword(USERNAME_EXIST);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest1() throws Exception {
		port.renewPassword(USERNAME_DOES_NOT_EXIST);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest2() throws Exception {
		port.renewPassword(INVALID_USERNAME);
	}

	@Test(expected = UserDoesNotExist_Exception.class)
	public void userDoesNotExistTest3() throws Exception {
		port.renewPassword(null);
	}

}