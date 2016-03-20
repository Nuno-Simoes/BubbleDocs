package id.ws.impl;


import static org.junit.Assert.assertEquals;


import org.junit.Test;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

public class RequestAuthenticationTest extends IdTest {

	String USERNAME_EXIST = "bruno";
	String USERNAME_DOES_NOT_EXIST = "zequinha";
	String PASSWORD_EXIST = "Bbb2";
	String PASSWORD_DOES_NOT_EXIST = "batata";
	String EMPTY_PASSWORD = "";
	String EMPTY_USERNAME = "";
	
	byte[] ALLOWED_PASS = PASSWORD_EXIST.getBytes();
	byte[] FAIL_PASS = PASSWORD_DOES_NOT_EXIST.getBytes();
	byte[] EMPTY_PASS = EMPTY_PASSWORD.getBytes();

	@Test
	public void requestAuthenticationTestSuccess() throws Exception {
		port.requestAuthentication(USERNAME_EXIST, ALLOWED_PASS);		
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void userDoesNotExist1() throws Exception {
		port.requestAuthentication(USERNAME_DOES_NOT_EXIST, ALLOWED_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void userDoesNotExist2() throws Exception {
		port.requestAuthentication(EMPTY_USERNAME, ALLOWED_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void userDoesNotExist3() throws Exception {
		port.requestAuthentication(null, ALLOWED_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void failedPassword1() throws Exception {
		port.requestAuthentication(USERNAME_EXIST, FAIL_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void failedPassword2() throws Exception {
		port.requestAuthentication(USERNAME_EXIST, null);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void failedPassword3() throws Exception {
		port.requestAuthentication(USERNAME_EXIST, EMPTY_PASS);
	}

}