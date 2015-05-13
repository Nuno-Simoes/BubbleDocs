package pt.ulisboa.tecnico.bubbledocs.service.remote;

import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.impl.SecureStore;

public class StoreRemoteServices {
	public static StoreRemoteServices storeRemote = null;
	
	static protected SecureStore secureStore;
	
	public StoreRemoteServices (){
		secureStore = new SecureStore();
	}

	public static StoreRemoteServices getInstance(){
		if(storeRemote == null)
			storeRemote = new StoreRemoteServices();
		return storeRemote;
	}

	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException {
		DocUserPair pair = new DocUserPair();
		pair.setUserId(username);
		pair.setDocumentId(docName);
		try {
			secureStore.store(pair, document);
		} catch (DocDoesNotExist_Exception | UserDoesNotExist_Exception
				| CapacityExceeded_Exception e) {
			throw new CannotStoreDocumentException();
		}
	}

	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		byte[] document;
		try {
			document = secureStore.load(pair);
		} catch (DocDoesNotExist_Exception | UserDoesNotExist_Exception e) {
			throw new CannotLoadDocumentException();
		}
		
		return document;
	}
}