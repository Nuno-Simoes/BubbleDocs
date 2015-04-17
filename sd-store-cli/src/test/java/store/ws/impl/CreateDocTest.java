package store.ws.impl;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;

public class CreateDocTest extends StoreServiceTest {
	
	String USER_EXISTS = "alice";
	String USER_EXISTS_2 = "bruno"; 
	String USER_DOES_NOT_EXIST = "francisco";
	String NEW_DOC = "grades";
	String NEW_DOC_2 = "project";
	String NEW_DOC_3 = "pages";
	
	// User that does not exist creates new document
	@Test
	public void createNewUserSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(NEW_DOC);
		port.createDoc(docUserPair);	
	}
	
	// User that already exists creates new document
	@Test
	public void createNewDocumentSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);	
		port.createDoc(docUserPair);
	}
	
	// User adds two new documents
	@Test
	public void createSeveralDocumentsSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS_2);
		docUserPair.setDocumentId(NEW_DOC_2);
		
		port.createDoc(docUserPair);
		
		DocUserPair docUserPair2 = new DocUserPair();
		docUserPair2.setUserId(USER_EXISTS_2);
		docUserPair2.setDocumentId(NEW_DOC_3);
		
		port.createDoc(docUserPair2);
	}
	
	// User already has document with given name on repository
	@Test (expected=DocAlreadyExists_Exception.class)
	public void docAlreadyExists() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		port.createDoc(docUserPair);
	}
}
