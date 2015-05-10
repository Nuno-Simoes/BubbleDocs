package pt.ulisboa.tecnico.bubbledocs.service.remote;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import store.ws.impl.SecureStore;

public class StoreRemoteServices {
	public static StoreRemoteServices storeRemote = null;
	
	static protected SecureStore secureStore;
	
	public StoreRemoteServices (){}

	public static StoreRemoteServices getInstance(){
		if(storeRemote == null)
			storeRemote = new StoreRemoteServices();
		return storeRemote;
	}

	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException{
		Portal portal = Portal.getInstance();
		User user = portal.findUser(username);
		SecretKeySpec key = user.getKey();
		IvParameterSpec iv = user.getIv();
		
		DocUserPair pair = new DocUserPair();
		pair.setUserId(username);
		pair.setDocumentId(docName);
		
		secureStore.store(pair, document, key, iv);
	}

	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		return secureStore.load(pair);
	}
}