package store.ws.impl;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ListDocsTest extends StoreServiceTest {
	
	String USER_EXISTS = "bruno";
	String USER_EXISTS_EMPTY = "carla";
	String USER_DOES_NOT_EXIST = "gabriel";
	String NEW_DOC = "grades";
	String NEW_DOC_2 = "project";
	
	// User that exists but hasnt created any documents
	@Test
	public void success() throws UserDoesNotExist_Exception {
		client.listDocs(USER_EXISTS_EMPTY);
	}
	
	@Test
	public void success2() throws Exception {
		client.listDocs(USER_EXISTS);
	}
	
	@Test (expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		client.listDocs(USER_DOES_NOT_EXIST);
	}
}