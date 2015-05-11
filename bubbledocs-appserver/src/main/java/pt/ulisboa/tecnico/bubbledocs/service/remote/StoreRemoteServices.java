package pt.ulisboa.tecnico.bubbledocs.service.remote;

import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
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
		secureStore.store(pair, document);
	}

	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		byte[] document = secureStore.load(pair);
		return document;
	}
}