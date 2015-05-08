package pt.ulisboa.tecnico.bubbledocs.service.remote;

import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotStoreDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws;

public class StoreRemoteServices {
	public static StoreRemoteServices storeRemote = null;
	
	static private String uddiURL = "http://localhost:8081";
	static private String name = "SdStore";
	static protected SDStore port;
	
	public StoreRemoteServices (){
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		String endpointAddress = uddiNaming.lookup(name);
		SDStore_Service service = new SDStore_Service();
		port = service.getSDStoreImplPort();
	}

	public static StoreRemoteServices getInstance(){
		if(storeRemote == null)
			storeRemote = new StoreRemoteServices();
		return storeRemote;
	}

	public void storeDocument(String username, String docName, byte[] document)
			throws CannotStoreDocumentException, RemoteInvocationException{
		try{
			DocUserPair pair = new DocUserPair();
			pair.setUserId(username);
			pair.setDocumentId(docName);
			port.store(pair, document);
		} catch(CapacityExceeded_Exception | DocDoesNotExist_Exception | UserDoesNotExist_Exception e){
			throw new CannotStoreDocumentException();
		}
	}

	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException {
		try{
			DocUserPair pair = new DocUserPair();
			pair.setDocumentId(docName);
			pair.setUserId(username);
			return port.load(pair);
		} catch (DocDoesNotExist_Exception | UserDoesNotExist_Exception e){
			throw new CannotLoadDocumentException();
		}
	}
}