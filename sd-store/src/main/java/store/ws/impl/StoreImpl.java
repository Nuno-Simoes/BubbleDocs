package store.ws.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.annotation.Resource;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import org.jdom2.Attribute;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Element;

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
@HandlerChain(file="handler-chain.xml")
public class StoreImpl implements SDStore {

	List<User> users = new ArrayList<User>();
	
	public static final String CLASS_NAME = StoreImpl.class.getSimpleName();
    public static final String TOKEN = "server";

    @Resource
    private WebServiceContext webServiceContext;

	private int seq;
	private int pid;

	public StoreImpl () {

		// Users for SDis
		User alice = new User("alice");
		alice.createRepository("grades");
		User bruno = new User("bruno");
		bruno.createRepository("project");
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
		
		// User for read
		User reservedUser = new User("reservedUser");
		reservedUser.createRepository("reservedDoc");
		users.add(reservedUser);

		users.add(poe);
		users.add(ars);
		poe.createRepository("13");
		ars.createRepository("folha");
		
		
		this.seq=0;
		this.pid=randInt(10, 100);
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public String encode (String userId, String docId, byte[] contents) {
		System.out.println("ENCODE SERVER");
		Element root = new Element("root");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(root);
		
		Element tag = new Element("tag");
		tag.setAttribute(new Attribute("seq", Integer.toString(this.seq)));
		tag.setAttribute(new Attribute("pid", Integer.toString(this.pid)));
		doc.getRootElement().addContent(tag);
		
		Element document = new Element("document");
		document.setAttribute(new Attribute("userId", userId));
		document.setAttribute(new Attribute("docId", docId));
		document.setAttribute(new Attribute("content", printBase64Binary(contents)));
		doc.getRootElement().addContent(document);
		
		return new XMLOutputter().outputString(doc);
	}
	
	public int decodeSeq (String document) {
		org.jdom2.Document jdomDoc = null;
		
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		try {
			jdomDoc = builder.build(new ByteArrayInputStream(document.getBytes()));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Element root = jdomDoc.getRootElement();
		Element tag = root.getChild("tag");
		int receivedSeq = Integer.parseInt(tag.getAttributeValue("seq"));
				
		return receivedSeq;
	}
	
	public int decodePid (String document) {
		org.jdom2.Document jdomDoc = null;
		
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		try {
			jdomDoc = builder.build(new ByteArrayInputStream(document.getBytes()));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Element root = jdomDoc.getRootElement();
		Element tag = root.getChild("tag");
		int receivedPid = Integer.parseInt(tag.getAttributeValue("pid"));
				
		return receivedPid;
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

		// retrieve message context
        MessageContext messageContext = webServiceContext.getMessageContext();

        // *** #6 ***
        // get token from message context
        String propertyValue = (String) messageContext.get(RelayServerHandler.REQUEST_PROPERTY);
        System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, propertyValue);

		
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
	
    // *** #7 ***
    // put token in message context
    String newValue = "ack";
    System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
    messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newValue);
    
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
        
        int receivedSeq = decodeSeq(propertyValue);
        int receivedPid = decodePid(propertyValue);
        
        if ((receivedSeq > this.seq) || ((receivedSeq==this.seq) && (receivedPid>this.pid))) {

        	String username = docUserPair.getUserId();
        	String document = docUserPair.getDocumentId();

        	// 1 - Verify if user does not exist. If true, throw new
        	// UserDoesNotExist_Exception
        	User user = findUser(username);
        	if (user==null) {
        		UserDoesNotExist userDoesNotExist = new UserDoesNotExist();
        		userDoesNotExist.setUserId(username);
        		throw new UserDoesNotExist_Exception("Invalid user", 
        				userDoesNotExist);
        	}

        	// 2 - Verify if repository is empty or if document does not exist.
        	// If true, throw new DocDoesNotExist_Exception
        	Repository repository = user.getRepository();
        	if (repository==null || !repository.documentExists(document)) {
        		DocDoesNotExist docDoesNotExist = new DocDoesNotExist();
        		docDoesNotExist.setDocId(document);
        		throw new DocDoesNotExist_Exception("Invalid document", 
        				docDoesNotExist);
        	}

        	// 3 - Else, write.
        	repository.writeDocument(document, contents);
        	
        	this.seq = receivedSeq;
        }

        // *** #7 ***
        // put token in message context
        String newValue = "ack";
        System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
        messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newValue);
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
        
        String username = docUserPair.getUserId();
        String document = docUserPair.getDocumentId();
        
        // 1 - Verify if user does not exist. If true, throw new
        // UserDoesNotExist_Exception
        User user = findUser(username);
        if (user==null) {
        	UserDoesNotExist userDoesNotExist = new UserDoesNotExist();
			userDoesNotExist.setUserId(username);
			throw new UserDoesNotExist_Exception("Invalid user", 
					userDoesNotExist);
        }
        
        // 2 - Verify if repository is empty or if document does not exist.
        // If true, throw new DocDoesNotExist_Exception
        Repository repository = user.getRepository();
        if (repository==null || !repository.documentExists(document)) {
        	DocDoesNotExist docDoesNotExist = new DocDoesNotExist();
			docDoesNotExist.setDocId(document);
			throw new DocDoesNotExist_Exception("Invalid document", 
					docDoesNotExist);
        }

        byte[] loadedDoc = repository.readDocument(document);
        
        String newValue = encode(username, document, loadedDoc);
        System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, TOKEN);
        messageContext.put(RelayServerHandler.RESPONSE_PROPERTY, newValue);
        
        return repository.readDocument(document);
	}
}
