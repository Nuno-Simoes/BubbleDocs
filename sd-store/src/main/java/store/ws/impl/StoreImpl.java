package store.ws.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

@WebService(
		endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore",
		wsdlLocation="SD-STORE.1_1.wsdl",
		name="SdStore",
		portName="SDStoreImplPort",
		targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
		serviceName="SDStore")
public class StoreImpl implements SDStore {

	List<User> users = new ArrayList<User>();
	
	public StoreImpl () {
		User alice = new User("alice");
		User bruno = new User("bruno");
		User carla = new User("carla");
		User duarte = new User("duarte");
		User eduardo = new User("eduardo");
		
		users.add(alice);
		users.add(bruno);
		users.add(carla);
		users.add(duarte);
		users.add(eduardo);
	}
	
	public User findUser (String userId) {
		for (User u : users) {
			if (u.getUserId().equals(userId)) {
				return u;
			}
		}
		
		return null;
	}
	
	// Creates new empty document with name docUserPair.getDocumentId()
	// for user docUserPair.getUserId()
	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {
		
		String userID = docUserPair.getUserId();
		String documentID = docUserPair.getDocumentId();
		
		// 1 - Verify if user does not exist. If true, create new user with
		// username userID.
		User user = findUser(userID);
		if (user==null) {
			user = new User(userID);
		}
		
		// 2 - Verify if user does not have a repository. If true, create a
		// new repository for that user with given document.
		Repository repository = user.getRepository();
		if (repository==null) {
			user.createRepository(documentID);
			return;
		}
		
		// 3 - Verify if document name already exists in repository. If true, 
		// throw new DocAlreadyExists_Exception.
		if (repository.documentExists(documentID)) {
			DocAlreadyExists docAlreadyExists = new DocAlreadyExists();
			docAlreadyExists.setDocId(documentID);
			throw new DocAlreadyExists_Exception("Duplicate document", 
					docAlreadyExists);
		}
		
		// 4 - Else, create new empty document in user repository.
		repository.addDocument(documentID);
	}

	// Lists all documents for userId
	public List<String> listDocs(String userId)
			throws UserDoesNotExist_Exception {
		
		// 1 - Verify if given user does not exist. If true, throw new 
		// UserDoesNotExist_Exception.
		User user = findUser(userId);
		if (user==null) {
			UserDoesNotExist userDoesNotExist = new UserDoesNotExist();
			userDoesNotExist.setUserId(userId);
			throw new UserDoesNotExist_Exception("Invalid user", 
					userDoesNotExist);
		}
		
		// 2 - Else, list name of all documents in said repository.
		return user.getRepository().listDocuments();
	}

	// Writes docUserPair.getDocumentId() for user docUserPair.getUserId()
	// with contents
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		
		String userID = docUserPair.getUserId();
		String documentID = docUserPair.getDocumentId();
		
		// 1 - Verify if user does not exist. If true, throw new 
		// UserDoesNotExist_Exception.
		User user = findUser(docUserPair.getUserId());
		if (user==null) {
			UserDoesNotExist userDoesNotExist = new UserDoesNotExist();
			userDoesNotExist.setUserId(userID);
			throw new UserDoesNotExist_Exception("Invalid user", 
					userDoesNotExist);
		}
		
		// 2 - Verify if document name does not exist in repository. If true,
		// throw new DocDoesNotExist_Exception.
		Repository repository = user.getRepository();
		if ((repository==null) || !repository.documentExists(documentID)) {
			DocDoesNotExist docDoesNotExist = new DocDoesNotExist();
			docDoesNotExist.setDocId(documentID);
			throw new DocDoesNotExist_Exception("Invalid document", 
					docDoesNotExist);
		}
		
		// 3 - Verify if capacity of repository plus capacity of contents
		// exceeds size limit for repository. If true, throw new
		// CapacitExceeded_Exception.
		int newSize = contents.length + repository.getSize();
		if (newSize > repository.getCapacity()) {
			CapacityExceeded capacityExceeded = new CapacityExceeded();
			capacityExceeded.setAllowedCapacity(repository.getCapacity());
			capacityExceeded.setCurrentSize(newSize);
			throw new CapacityExceeded_Exception("Capacity exceeded", 
					capacityExceeded);
		}
		
		// 4 - Else, write.
		repository.writeDocument(documentID, contents);
	}

	// Lets user docUserPair.getUserId() read docUserPair.getDocumentId()
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		
		String userID = docUserPair.getUserId();
		String documentID = docUserPair.getDocumentId();
		
		// 1 - Verify if user does not exist. If true, throw new 
		// UserDoesNotExist_Exception.
		User user = findUser(userID);
		if (user==null) {
			UserDoesNotExist userDoesNotExist = new UserDoesNotExist();
			userDoesNotExist.setUserId(userID);
			throw new UserDoesNotExist_Exception("Invalid user", 
					userDoesNotExist);
		}
		
		// 2 - Verify if document name does not exist in repository. If true,
		// throw new DocDoesNotExist_Exception.return null;
		Repository repository = user.getRepository();
		if (repository==null || !repository.documentExists(documentID)) {
			DocDoesNotExist docDoesNotExist = new DocDoesNotExist();
			docDoesNotExist.setDocId(documentID);
			throw new DocDoesNotExist_Exception("Invalid document", 
					docDoesNotExist);
		}
		
		// 3 - Else, read
		return repository.readDocument(documentID);
	}
	
}