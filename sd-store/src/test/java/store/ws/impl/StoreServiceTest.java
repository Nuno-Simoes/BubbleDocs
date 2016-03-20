package store.ws.impl;

import org.junit.After;
import org.junit.Before;

public class StoreServiceTest {
	
	StoreImpl store;

	@Before
	public void setUp(){
		store = new StoreImpl();
	}
	
	@After
	public void tearDown(){
		store = null;
	}
		
	public boolean userExists (String userId) {
		User u = store.findUser(userId);
		if (u!=null && u.getUserId().equals(userId)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean docExists (String userId, String docId) {
		User u = store.findUser(userId);
		if (u.getRepository()==null) {
			return false;
		}
		
		for (Document d : u.getRepository().getDocuments()) {
			if (d.getDocumentId().equals(docId)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void setCapacity (String userId, int newCapacity) {
		User u = store.findUser(userId);
		Repository repository = u.getRepository();
		
		if (repository!=null) {
			repository.setSize(newCapacity);
		}
	}
	
}
