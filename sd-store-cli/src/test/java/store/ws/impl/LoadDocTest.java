package store.ws.impl;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;

public class LoadDocTest extends StoreServiceTest {
	
	String USER_EXISTS = "alice";
	String USER_EXISTS_2 = "bruno";
	String EMPTY_USER = "carla";
	String DOC_EXISTS = "grades";
	String DOC_EXISTS_2 = "project";
	String USER_DOES_NOT_EXIST = "gabriel";
	String NEW_DOC_EMPTY = "empty";
	
	// Read document
	@Test
	public void readSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(DOC_EXISTS);
		
//		port.createDoc(docUserPair);
		byte[] contents = "Any string".getBytes();
		//FrontEnd.getInstance().store(docUserPair, contents);
		FrontEnd.getInstance().load(docUserPair);
//		port.load(docUserPair);
	}
	
	/*
	// Read empty document
	@Test
	public void readEmptySuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS_2);
		docUserPair.setDocumentId(DOC_EXISTS_2);
		
//		port.createDoc(docUserPair);
		port.load(docUserPair);
	}
	
	// User that exists but does not have documents
	@Test(expected=DocDoesNotExist_Exception.class)
	public void emptyRepository() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(EMPTY_USER);
		docUserPair.setDocumentId(NEW_DOC_EMPTY);
		port.load(docUserPair);
	}
		
	// User that exists but does not have given document
	@Test(expected=DocDoesNotExist_Exception.class)
	public void docDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS_2);
		docUserPair.setDocumentId(DOC_EXISTS);
			
		port.load(docUserPair);
	}
	
	// User that does not exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(DOC_EXISTS);
		port.load(docUserPair);
	}
	*/
}
