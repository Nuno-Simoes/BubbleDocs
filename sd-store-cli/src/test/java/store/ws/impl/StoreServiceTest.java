package store.ws.impl;

import javax.xml.registry.JAXRException;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreServiceTest {

	static protected FrontEnd client;

	public static StoreServiceTest store = null;

	public static StoreServiceTest getInstance(){
		if(store == null)
			store = new StoreServiceTest();
		return store;
	}

	//static members
	//one-time initialisation and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws JAXRException {
		client = FrontEnd.getInstance();
	}

	@AfterClass
	public static void oneTimeTearDown() {
		client = null;
	}

	public void store (String username, String docName, byte[] document) 
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception, 
			UserDoesNotExist_Exception{
		DocUserPair pair = new DocUserPair();
		pair.setUserId(username);
		pair.setDocumentId(docName);
		client.store(pair, document);
	}

	public byte[] load (String username, String docName) 
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		return client.load(pair);
	}
}