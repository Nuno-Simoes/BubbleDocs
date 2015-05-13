package store.ws.impl;

public class StoreDocTest extends StoreServiceTest {
	
/*	String USER_EXISTS = "alice";
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
		port.store(docUserPair, contents);
	}
	
	// Document that exceeds repository capacity
	@Test(expected=CapacityExceeded_Exception.class)
	public void capacityExceeded() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(DOC_EXISTS);
		
		byte[] contents = new byte[10*1025];
		port.store(docUserPair, contents);
	}
	
	// User that exists but does not have documents
	@Test(expected=DocDoesNotExist_Exception.class)
	public void emptyRepository() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(EMPTY_USER);
		docUserPair.setDocumentId(DOC_EXISTS);
		byte[] contents = "Any string".getBytes();
		port.store(docUserPair, contents);
	}
	
	// User that exists but does not have given document
	@Test(expected=DocDoesNotExist_Exception.class)
	public void docDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_EXISTS);
		docUserPair.setDocumentId(DOC_DOES_NOT_EXIST);

		byte[] contents = "Any string".getBytes();
		port.store(docUserPair, contents);
	}
	
	// User that does not exist
	@Test(expected=UserDoesNotExist_Exception.class)
	public void userDoesNotExist() throws Exception {
		DocUserPair docUserPair = new DocUserPair();
		docUserPair.setUserId(USER_DOES_NOT_EXIST);
		docUserPair.setDocumentId(DOC_EXISTS);
		
		byte[] contents = "Any string".getBytes();
		port.store(docUserPair, contents);
	}*/
}
