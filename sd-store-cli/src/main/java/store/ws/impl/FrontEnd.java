package store.ws.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.handler.RelayClientHandler;
import store.ws.uddi.UDDINaming;

public class FrontEnd {

    public static final String CLASS_NAME = FrontEnd.class.getSimpleName();
    public static final String TOKEN = "client";
    private static final int NOS = 3;
    private List<String> url = new ArrayList<String>();
    private List<SDStore> port = new ArrayList<SDStore>();
    
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
		List<Map<String, Object>> responseContext = new ArrayList<Map<String, Object>>();
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
			int newSeq = decode(newValue);
			
			if(newSeq>finalSeq) {
				finalResult = newResult;
			}
		}
		
		return finalResult;
	}

	public void store (DocUserPair docUserPair, byte[] contents) {
		
		/*BindingProvider bindingProvider = (BindingProvider) port;
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
	    System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue2);*/
	}
	
	public String encode (String userId, String docId, byte[] contents, String seq) {
		System.out.println("ENCODE CLIENT");
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
		System.out.println("DECODE CLIENT");
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
		
		System.out.println(jdomDoc);
		
		Element root = jdomDoc.getRootElement();
		Element tag = root.getChild("tag");
		int receivedSeq = Integer.parseInt(tag.getAttributeValue("seq"));
		
		return receivedSeq;
	}
	
}
