package store.ws.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.LoadResponse;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.StoreResponse;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.handler.RelayClientHandler;
import store.ws.uddi.UDDINaming;

public class FrontEnd {

    public static final String CLASS_NAME = FrontEnd.class.getSimpleName();
    public static final String TOKEN = "client";
    private static final int NOS = 3;
    private List<String> url = new ArrayList<String>();
    private List<SDStore> port = new ArrayList<SDStore>();
    private int pid;
    
    public void connect () {

    	List<String> name = new ArrayList<String>();
		String uddiURL = "http://localhost:8081";
		
		for (int i=0; i<NOS; i++) {
			String newName = "SdStore" + i;
			name.add(i, newName);
		}
		
		try {
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			for (int i=0; i<NOS; i++) {
				String newUrl = uddiNaming.lookup(name.get(i));
				url.add(i, newUrl);
			}
		} catch (JAXRException e) {
			e.printStackTrace();
		}
		
		SDStore_Service service = new SDStore_Service();
		SDStore newPort = service.getSDStoreImplPort();
		for (int i=0; i<NOS; i++) {
			port.add(i, newPort);
		}
		this.pid = randInt(0, 9);
	}
    
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
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
		
		List<BindingProvider> bindingProvider = new ArrayList<BindingProvider>();
		List<Map<String, Object>> requestContext = new ArrayList<Map<String, Object>>();
		List<byte[]> result = new ArrayList<byte[]>();
		List<Response<LoadResponse>> response = new ArrayList<Response<LoadResponse>>();
		
		for (int i=0; i<NOS; i++) {
			BindingProvider newBindingProvider = (BindingProvider) port.get(i);
			bindingProvider.add(i, newBindingProvider);
			
			Map<String, Object> newRequestContext = bindingProvider.get(i).getRequestContext();
			requestContext.add(i, newRequestContext);
			(requestContext.get(i)).put(RelayClientHandler.REQUEST_PROPERTY, "");
			(requestContext.get(i)).put(ENDPOINT_ADDRESS_PROPERTY, url.get(i));
			
			Response<LoadResponse> newResponse = (port.get(i)).loadAsync(docUserPair);
			response.add(i, newResponse);			
		}
		
		int q = 0;
		HashSet<Integer> set = new HashSet<Integer>();
		
		while (q<(NOS/2+1)) {
			for (int i=0; i<NOS; i++) {
				if ((response.get(i)).isDone()) {
					set.add(i);
				}
			}
			q = set.size();
		}
		
		byte[] finalResult = null;
		int finalSeq = 0;
		int finalPid = 0;
		
		for (Integer i : set) {
			byte[] newResult = null;
			
			try {
				newResult = ((port.get(i)).load(docUserPair));
				result.add(newResult);
			} catch (DocDoesNotExist_Exception e) {
				e.printStackTrace();
			} catch (UserDoesNotExist_Exception e) {
				e.printStackTrace();
			}
			
			Map<String, Object> newResponseContext = (bindingProvider.get(i)).getResponseContext();
			String newValue = (String)newResponseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
			int newSeq = decodeSeq(newValue);
			int newPid = decodePid(newValue);
			
	        if ((newSeq > finalSeq) || ((newSeq==finalSeq) && (newPid>finalPid))) {
				finalSeq = newSeq;
				finalResult = newResult;
			}
		}
		return finalResult;
	}

	public void store (DocUserPair docUserPair, byte[] contents) {

		List<BindingProvider> bindingProvider = new ArrayList<BindingProvider>();
		List<Map<String, Object>> requestContext = new ArrayList<Map<String, Object>>();
		List<Response<LoadResponse>> response = new ArrayList<Response<LoadResponse>>();
		
		String reservedUser = "reservedUser";
		String reservedDoc = "reservedDoc";
		DocUserPair reservedPair = new DocUserPair();
		reservedPair.setDocumentId(reservedDoc);
		reservedPair.setUserId(reservedUser);
		
		for (int i=0; i<NOS; i++) {
			BindingProvider newBindingProvider = (BindingProvider)(port.get(i));
			bindingProvider.add(i, newBindingProvider);
			
			Map<String, Object> newRequestContext = (bindingProvider.get(i)).getRequestContext();
			requestContext.add(newRequestContext);
			(requestContext.get(i)).put(RelayClientHandler.REQUEST_PROPERTY, "");
			(requestContext.get(i)).put(ENDPOINT_ADDRESS_PROPERTY, url.get(i));
			
			Response<LoadResponse> newResponse = (port.get(i)).loadAsync(reservedPair);
			response.add(i, newResponse);			
		}
		
		int q = 0;
		HashSet<Integer> set = new HashSet<Integer>();
		
		while (q<(NOS/2+1)) {
			for (int i=0; i<NOS; i++) {
				if ((response.get(i)).isDone()) {
					set.add(i);
				}
			}
			q = set.size();
		}
		
		int finalSeq = 0;
		int finalPid = 0;
		
		for (Integer i : set) {
			
			try {
				(port.get(i)).load(docUserPair);
			} catch (DocDoesNotExist_Exception e) {
				e.printStackTrace();
			} catch (UserDoesNotExist_Exception e) {
				e.printStackTrace();
			}
			
			Map<String, Object> newResponseContext = (bindingProvider.get(i)).getResponseContext();
			String newValue = (String)newResponseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
			int newSeq = decodeSeq(newValue);
			int newPid = decodePid(newValue);
			
			if(newSeq>finalSeq) {
				finalSeq = newSeq;
			} else if(newSeq==finalSeq) {
				if(newPid>finalPid) {
					finalSeq = newSeq;
				}
			}
		}
		
		finalSeq++;				
	    String propertyValue = encode(docUserPair.getUserId(), docUserPair.getDocumentId(), 
	    		contents, Integer.toString(finalSeq), Integer.toString(pid));
	    
	    List<BindingProvider> bindingProvider2 = new ArrayList<BindingProvider>();
	    List<Map<String, Object>> requestContext2 = new ArrayList<Map<String, Object>>();
		List<Response<StoreResponse>> response2 = new ArrayList<Response<StoreResponse>>();

	    for (int i=0; i<NOS; i++) {
	    	BindingProvider newBindingProvider = (BindingProvider)(port.get(i));
	    	bindingProvider2.add(i, newBindingProvider);
	    	
	    	Map<String, Object> newRequestContext = (bindingProvider.get(i)).getRequestContext();
	    	requestContext2.add(i, newRequestContext);
	    	(requestContext.get(i)).put(RelayClientHandler.REQUEST_PROPERTY, propertyValue);
	    	(requestContext.get(i)).put(ENDPOINT_ADDRESS_PROPERTY, url.get(i));
	    	
	    	Response<StoreResponse> newResponse = (port.get(i)).storeAsync(docUserPair, contents);
	    	response2.add(i, newResponse);

	    }
	    
	    int ack=0;
	    while (ack<(NOS/2+1)) {
	    	for (int i=0; i<NOS; i++) {
	    		if (!response2.get(i).isDone()) {
	    			try {
						(port.get(i)).store(docUserPair, contents);
					} catch (CapacityExceeded_Exception e) {
						e.printStackTrace();
					} catch (DocDoesNotExist_Exception e) {
						e.printStackTrace();
					} catch (UserDoesNotExist_Exception e) {
						e.printStackTrace();
					}
	    		} else {
	    			ack++;
	    		}
	    	}
	    }
	}
	
	public String encode (String userId, String docId, byte[] contents, String seq, String pid) {
		Element root = new Element("root");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(root);
		
		Element tag = new Element("tag");
		tag.setAttribute(new Attribute("seq", seq));
		tag.setAttribute(new Attribute("pid", pid));
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
}