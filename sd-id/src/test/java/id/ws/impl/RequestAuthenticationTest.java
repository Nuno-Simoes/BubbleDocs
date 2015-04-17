package id.ws.impl;

import static org.junit.Assert.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;

public class RequestAuthenticationTest extends IdTest {

	String USERNAME_EXIST = "alice";
	String USERNAME_DOES_NOT_EXIST = "manuel";
	String PASSWORD_EXIST = "Aaa1";
	String PASSWORD_DOES_NOT_EXIST = "batata";
	byte[] ALLOWED_PASS = PASSWORD_EXIST.getBytes();
	byte[] FAIL_PASS = PASSWORD_DOES_NOT_EXIST.getBytes();
	String EMPTY = "";
	byte[] EMPTY_PASS = EMPTY.getBytes();

	@Test
	public void requestAuthenticationTesteSuccess() throws Exception {
		id.requestAuthentication(USERNAME_EXIST, ALLOWED_PASS);
		User u = getUserFromUsername(USERNAME_EXIST);
		assertEquals(u.getUserId(), USERNAME_EXIST);
		assertEquals(u.getPassword(), PASSWORD_EXIST);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void userDoesNotExist1() throws Exception {
		id.requestAuthentication(USERNAME_DOES_NOT_EXIST, ALLOWED_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void userDoesNotExist2() throws Exception {
		id.requestAuthentication(EMPTY, ALLOWED_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void userDoesNotExist3() throws Exception {
		id.requestAuthentication(null, ALLOWED_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void failedPassword1() throws Exception {
		id.requestAuthentication(USERNAME_EXIST, FAIL_PASS);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void failedPassword2() throws Exception {
		id.requestAuthentication(USERNAME_EXIST, null);
	}

	@Test(expected = AuthReqFailed_Exception.class)
	public void failedPassword3() throws Exception {
		id.requestAuthentication(USERNAME_EXIST, EMPTY_PASS);
	}
	
}