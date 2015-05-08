package store.ws.impl;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.uddi.UDDINaming;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class StoreServiceTest {

	static private String uddiURL = "http://localhost:8081";
	static private String name = "SdStore";
	static protected SDStore port;

	public static StoreServiceTest store = null;

	public static StoreServiceTest getInstance(){
		if(store == null)
			store = new StoreServiceTest();
		return store;
	}

	//static members
	//one-time initialisation and clean-up
	@BeforeClass
	public static void oneTimeSetUp() throws JAXRException {
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		String endpointAddress = uddiNaming.lookup(name);
		SDStore_Service service = new SDStore_Service();
		port = service.getSDStoreImplPort();
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}

	@AfterClass
	public static void oneTimeTearDown() {
		port = null;
	}

	public void store (String username, String docName, byte[] document) 
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception, 
			UserDoesNotExist_Exception{
		DocUserPair pair = new DocUserPair();
		pair.setUserId(username);
		pair.setDocumentId(docName);
		port.store(pair, document);
	}

	public byte[] load (String username, String docName) 
			throws DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		DocUserPair pair = new DocUserPair();
		pair.setDocumentId(docName);
		pair.setUserId(username);
		return port.load(pair);
	}
}