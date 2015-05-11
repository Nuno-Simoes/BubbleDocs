package store.ws.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.annotation.Resource;

import org.jdom2.Attribute;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Element;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
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
import store.ws.impl.handler.RelayServerHandler;

@WebService(
		endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore",
		wsdlLocation="SD-STORE.1_1.wsdl",
		name="SdStore",
		portName="SDStoreImplPort",
		targetNamespace="urn:pt:ulisboa:tecnico:sdis:store:ws",
		serviceName="SDStore")

@HandlerChain(file="/handler-chain.xml")
public class StoreImpl implements SDStore {

	List<User> users = new ArrayList<User>();
	public static final String CLASS_NAME = StoreImpl.class.getSimpleName();

	@Resource
	private WebServiceContext webServiceContext;

	private static int seq;

	public StoreImpl () {

		// Users for SDis
		User alice = new User("alice");
		User bruno = new User("bruno");
		User carla = new User("carla");
		User duarte = new User("duarte");
		User eduardo = new User("eduardo");

		// Users for ES
		User poe = new User("poe");
		User ars = new User("ars");

		users.add(alice);
		users.add(bruno);
		users.add(carla);
		users.add(duarte);
		users.add(eduardo);

		users.add(poe);
		users.add(ars);
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
			users.add(user);
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
		if (user.getRepository()==null) {
			return new ArrayList<String>();
		} else {
			return user.getRepository().listDocuments();
		}
	}

	// Writes docUserPair.getDocumentId() for user docUserPair.getUserId()
	// with contents
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {

		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		// *** #6 ***
		// get token from message context
		String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, propertyValue);

		org.jdom2.Document jdomDoc = null;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		try {
			jdomDoc = builder.build(new ByteArrayInputStream(parseBase64Binary(propertyValue)));
		} catch (JDOMException je) {
			je.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

		Element root = jdomDoc.getRootElement();
		Element document = root.getChild("document");
		docUserPair.setUserId(document.getAttributeValue("userId"));
		docUserPair.setDocumentId(document.getAttributeValue("docId"));
		contents = parseBase64Binary(document.getAttributeValue("content"));

		Element tag = root.getChild("tag");
		int receivedSeq = Integer.parseInt(tag.getAttributeValue("seq"));

		String userID = docUserPair.getUserId();
		String documentID = docUserPair.getDocumentId();


		if(receivedSeq>seq) {

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
				//DocDoesNotExist docDoesNotExist = new DocDoesNotExist();
				//docDoesNotExist.setDocId(documentID);
				//throw new DocDoesNotExist_Exception("Invalid document", 
				//		docDoesNotExist);
				try {
					createDoc(docUserPair);
				} catch (DocAlreadyExists_Exception e) {
					e.printStackTrace();
				}			
			}
			repository = user.getRepository();

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

			seq = receivedSeq;

			// *** #7 ***
			// put token in message context
			messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, "ack");
		}
	}

	// Lets user docUserPair.getUserId() read docUserPair.getDocumentId()
	public byte[] load(DocUserPair docUserPair)
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception {

		// retrieve message context
		MessageContext messageContext = webServiceContext.getMessageContext();

		// *** #6 ***
		// get token from message context
		String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
		System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, propertyValue);
		
		org.jdom2.Document jdomDoc = null;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		try {
			jdomDoc = builder.build(new ByteArrayInputStream(parseBase64Binary(propertyValue)));
		} catch (JDOMException je) {
			je.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

		Element simpleDocument = jdomDoc.getRootElement();
		docUserPair.setUserId(simpleDocument.getAttributeValue("userId"));
		docUserPair.setDocumentId(simpleDocument.getAttributeValue("docId"));

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
		// throw new DocDoesNotExist_Exception
		Repository repository = user.getRepository();
		if (repository==null || !repository.documentExists(documentID)) {
			DocDoesNotExist docDoesNotExist = new DocDoesNotExist();
			docDoesNotExist.setDocId(documentID);
			throw new DocDoesNotExist_Exception("Invalid document", 
					docDoesNotExist);
		}
		
		// root element
		Element root = new Element("root");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(root);

		// tag element
		Element tag = new Element("tag");
		tag.setAttribute(new Attribute("seq", Integer.toString(seq)));
		tag.setAttribute(new Attribute("pid"," "));

		doc.getRootElement().addContent(tag);

		// doc element
		Element document = new Element("document");
		document.setAttribute(new Attribute("userId", docUserPair.getUserId()));
		document.setAttribute(new Attribute("docId", docUserPair.getDocumentId()));
		document.setAttribute(new Attribute("content", printBase64Binary(repository.readDocument(documentID))));
		
		doc.getRootElement().addContent(document);

		String token = new XMLOutputter().outputString(doc);
		
		// *** #7 ***
        // put token in message context
        messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, token);
        
      
		// 3 - Else, read
		return repository.readDocument(documentID);
	}
}