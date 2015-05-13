package store.ws.impl;

import org.junit.Test;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.Assert.*;

public class StoreDocTest extends StoreServiceTest {
	
	String USER_EXISTS = "alice";
	String USER_EXISTS_2 = "bruno";
	String EMPTY_USER = "carla";
	String DOC_EXISTS = "grades";
	String DOC_DOES_NOT_EXIST = "project";
	String USER_DOES_NOT_EXIST = "gabriel";
	
	// Success
	@Test
	public void storeSuccess() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(DOC_EXISTS);
		
//		port.createDoc(docUserPair);
		byte[] contents = "The quick brown fox jumps over the lazy dog".getBytes();
		client.store(docUserPair, contents);
		byte[] result = client.load(docUserPair);
		
		assertEquals(printBase64Binary(contents), printBase64Binary(result));
	}
		
	// User that exists but does not have documents
	@Test(expected=DocDoesNotExist_Exception.class)
	public void emptyRepository() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(EMPTY_USER);
		docUserPair.setDocumentId(DOC_EXISTS);
		byte[] contents = "Any string".getBytes();
		client.store(docUserPair, contents);
	}
	
	// User that exists but does not have given document
	@Test(expected=DocDoesNotExist_Exception.class)
	public void docDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(DOC_DOES_NOT_EXIST);

		byte[] contents = "Any string".getBytes();
		client.store(docUserPair, contents);
	}
	
	// User that does not exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(DOC_EXISTS);
		
		byte[] contents = "Any string".getBytes();
		client.store(docUserPair, contents);
	}
}
