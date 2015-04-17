package store.ws.impl;

import static org.junit.Assert.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;

public class CreateDocTest extends StoreServiceTest {
	
	String USER_EXISTS = "alice";
	String USER_DOES_NOT_EXIST = "francisco";
	String NEW_DOC = "grades";
	String NEW_DOC_2 = "project";
	
	// User that does not exist creates new document
	@Test
	public void createNewUserSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(NEW_DOC);
		store.createDoc(docUserPair);
		
		assertTrue(super.userExists(USER_DOES_NOT_EXIST));
		assertTrue(super.docExists(USER_DOES_NOT_EXIST, NEW_DOC));
	}
	
	// Verify behaviour of tearDown
	@Test
	public void tearDownSuccess() {
		assertFalse(super.userExists(USER_DOES_NOT_EXIST));
	}
	
	// User that already exists creates new document
	@Test
	public void createNewDocumentSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		assertTrue(super.userExists(USER_EXISTS));
		assertFalse(super.docExists(USER_EXISTS, NEW_DOC));
		
		store.createDoc(docUserPair);
		
		assertTrue(super.docExists(USER_EXISTS, NEW_DOC));
	}
	
	// User adds two new documents
	@Test
	public void createSeveralDocumentsSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		store.createDoc(docUserPair);
		
		DocUserPair docUserPair2 = new DocUserPair();
		docUserPair2.setUserId(USER_EXISTS);
		docUserPair2.setDocumentId(NEW_DOC_2);
		
		store.createDoc(docUserPair2);
		
		assertTrue(super.docExists(USER_EXISTS, NEW_DOC));
		assertTrue(super.docExists(USER_EXISTS, NEW_DOC_2));
	}
	
	// User already has document with given name on repository
	@Test (expected=DocAlreadyExists_Exception.class)
	public void docAlreadyExists() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		store.createDoc(docUserPair);
		assertTrue(super.docExists(USER_EXISTS, NEW_DOC));
		store.createDoc(docUserPair);
	}

}
