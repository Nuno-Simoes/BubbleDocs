package store.ws.impl;

import static org.junit.Assert.*;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class LoadTest extends StoreServiceTest {
	
	String USER_EXISTS = "alice";
	String DOC_EXISTS = "grades";
	String USER_DOES_NOT_EXIST = "francisco";
	String NEW_DOC = "grades";
	String NEW_DOC_2 = "project";
	
	// Read document
	@Test
	public void readSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		store.createDoc(docUserPair);
		byte[] contents = "Any string".getBytes();
		store.store(docUserPair, contents);
		byte[] userDoc = store.load(docUserPair);
		
		assertEquals(userDoc, contents);
	}
	
	// Read empty document
	@Test
	public void readEmptySuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		store.createDoc(docUserPair);
		byte[] userDoc = store.load(docUserPair);
		
		assertEquals(userDoc, null);
	}
	
	// User that exists but does not have documents
	@Test(expected=DocDoesNotExist_Exception.class)
	public void emptyRepository() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		store.load(docUserPair);
	}
		
	// User that exists but does not have given document
	@Test(expected=DocDoesNotExist_Exception.class)
	public void docDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
			
		DocUserPair docUserPair2 = new DocUserPair();
		docUserPair2.setUserId(USER_EXISTS);
		docUserPair2.setDocumentId(NEW_DOC_2);
			
		store.createDoc(docUserPair);
		store.load(docUserPair2);
	}
	
	// User that does not exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(NEW_DOC);
		store.load(docUserPair);
	}
	
	// Success
	/*@Test
	public void storeSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(NEW_DOC);
		
		store.createDoc(docUserPair);
		super.setCapacity(USER_EXISTS, 10*200);
		byte[] contents = "The quick brown fox jumps over the lazy dog".getBytes();
		store.store(docUserPair, contents);
	}*/
	
	
	
	// User that does not exist
	/*@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(NEW_DOC);
		byte[] contents = "Any string".getBytes();
		store.store(docUserPair, contents);
	}*/

}
