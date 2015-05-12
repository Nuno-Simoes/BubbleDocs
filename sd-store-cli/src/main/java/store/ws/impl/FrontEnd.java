package store.ws.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.handler.RelayClientHandler;
import store.ws.uddi.UDDINaming;

public class FrontEnd {

    public static final String CLASS_NAME = FrontEnd.class.getSimpleName();
    public static final String TOKEN = "client";
    String url;
    
    public void connect () {

		String uddiURL = "http://localhost:8081";
		String name0 = "SdStore";

		try {
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			url = uddiNaming.lookup(name0);
		} catch (JAXRException e) {
			e.printStackTrace();
		}
	}
    
    // create stub
    SDStore_Service service = new SDStore_Service();
    SDStore port = service.getSDStoreImplPort();

   	public static FrontEnd frontEnd = null;
    
	public FrontEnd() {
		this.connect();
	}

	public static FrontEnd getInstance() {
		if (frontEnd==null) {
			frontEnd = new FrontEnd();
		}
		return frontEnd;
	}

	public byte[] load (DocUserPair docUserPair) {
		
		BindingProvider bindingProvider = (BindingProvider) port;
	    Map<String, Object> requestContext = bindingProvider.getRequestContext();

		requestContext.put(RelayClientHandler.REQUEST_PROPERTY, "");
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
		//String initialValue = encode(docUserPair.getUserId(), docUserPair.getDocumentId(), "");
		//System.out.printf("FRONT END LOAD: %s put token '%s' on request context%n", CLASS_NAME, initialValue);
		//requestContext.put(RelayClientHandler.REQUEST_PROPERTY, initialValue);
		
		//requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
		//System.out.printf("Remote call to %s ...%n", url);
		
		byte[] result = null;
		
		try {
			result = port.load(docUserPair);
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}
		
		// Waits for Q responses
		Map<String, Object> responseContext = bindingProvider.getResponseContext();
	    String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
	    System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
	    
	    // Returns maxVal
	    return result;	
	}

	public void store (DocUserPair docUserPair, byte[] contents) {
		
		BindingProvider bindingProvider = (BindingProvider) port;
	    Map<String, Object> requestContext = bindingProvider.getRequestContext();

		requestContext.put(RelayClientHandler.REQUEST_PROPERTY, "");
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);
		
		String reservedUser = "reservedUser";
		String reservedDoc = "reservedDoc";
		DocUserPair reservedPair = new DocUserPair();
		reservedPair.setDocumentId(reservedDoc);
		reservedPair.setUserId(reservedUser);
				
		try {
			port.load(reservedPair);
		} catch (DocDoesNotExist_Exception e1) {
			e1.printStackTrace();
		} catch (UserDoesNotExist_Exception e1) {
			e1.printStackTrace();
		}
		
		Map<String, Object> responseContext = bindingProvider.getResponseContext();
	    String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
	    int tag = decode(finalValue);
	    String maxTag = Integer.toString(tag+1);
		
	    String propertyValue = encode(docUserPair.getUserId(), docUserPair.getDocumentId(), 
	    		contents, maxTag);
	    
	    BindingProvider bindingProvider2 = (BindingProvider) port;
	    Map<String, Object> requestContext2 = bindingProvider2.getRequestContext();

		requestContext2.put(RelayClientHandler.REQUEST_PROPERTY, propertyValue);
		requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, url);
	    
	    try {
			port.store(docUserPair, contents);
		} catch (CapacityExceeded_Exception e) {
			e.printStackTrace();
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}

	    Map<String, Object> responseContext2 = bindingProvider.getResponseContext();
	    String finalValue2 = (String) responseContext2.get(RelayClientHandler.RESPONSE_PROPERTY);
	    System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue2);
	}
	
	public String encode (String userId, String docId, byte[] contents, String seq) {
		Element root = new Element("root");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(root);
		
		Element tag = new Element("tag");
		tag.setAttribute(new Attribute("seq", seq));
		tag.setAttribute(new Attribute("pid", ""));
		doc.getRootElement().addContent(tag);
		
		Element document = new Element("document");
		document.setAttribute(new Attribute("userId", userId));
		document.setAttribute(new Attribute("docId", docId));
		document.setAttribute(new Attribute("content", printBase64Binary(contents)));
		doc.getRootElement().addContent(document);
		
		return new XMLOutputter().outputString(doc);
	}
	
	public int decode (String document) {
		org.jdom2.Document jdomDoc = null;
		
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);
		
		try {
			jdomDoc = builder.build(new ByteArrayInputStream(parseBase64Binary(document)));
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
	
}
