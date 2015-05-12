package store.ws.impl;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

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
		String name0 = "SdStore0";

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

    // access request context
    BindingProvider bindingProvider = (BindingProvider) port;
    Map<String, Object> requestContext = bindingProvider.getRequestContext();

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
		return null;
	}

	public void store (DocUserPair docUserPair, byte[] contents) {
		// *** #1 ***
	    // put token in request context
	    String initialValue = TOKEN;
	    System.out.printf("%s put token '%s' on request context%n", CLASS_NAME, initialValue);
	    requestContext.put(RelayClientHandler.REQUEST_PROPERTY, initialValue);

	    // set endpoint address
	    requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

	    // make remote call
	    System.out.printf("Remote call to %s ...%n", url);
	    
	    try {
			port.store(docUserPair, contents);
		} catch (CapacityExceeded_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocDoesNotExist_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    // access response context
	    Map<String, Object> responseContext = bindingProvider.getResponseContext();

	    // *** #12 ***
	    // get token from response context
	    String finalValue = (String) responseContext.get(RelayClientHandler.RESPONSE_PROPERTY);
	    System.out.printf("%s got token '%s' from response context%n", CLASS_NAME, finalValue);
	}
}
