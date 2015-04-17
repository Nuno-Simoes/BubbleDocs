package store.ws.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ListDocsTest extends StoreServiceTest {
	
	String USER_EXISTS = "alice";
	String USER_DOES_NOT_EXIST = "francisco";
	String NEW_DOC = "grades";
	String NEW_DOC_2 = "project";
	
	// User that exists but hasnt created any documents
	@Test
	public void userWithoutRepository() throws UserDoesNotExist_Exception {
		List<String> userList = store.listDocs(USER_EXISTS);
		List<String> emptyList = new ArrayList<String>();
		assertEquals(userList, emptyList);
	}
	
	// User that exists and has documents
	@Test
	public void userWithRepository() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		DocUserPair docUserPair2 = new DocUserPair();
		docUserPair2.setUserId(USER_EXISTS);
		docUserPair2.setDocumentId(NEW_DOC_2);
		
		store.createDoc(docUserPair);
		store.createDoc(docUserPair2);
		
		List<String> userList = store.listDocs(USER_EXISTS);
		List<String> list = new ArrayList<String>();
		list.add(NEW_DOC);
		list.add(NEW_DOC_2);
		
		assertEquals(userList, list);
	}
	
	// User that does not exist
	@Test (expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		store.listDocs(USER_DOES_NOT_EXIST);
	}
}
