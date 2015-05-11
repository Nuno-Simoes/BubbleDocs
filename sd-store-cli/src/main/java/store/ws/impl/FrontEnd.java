package store.ws.impl;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.uddi.UDDINaming;

public class FrontEnd {
	
	private static SDStore port0;
	private static SDStore port1;
	private static SDStore port2;
	
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
	
	public void connect () {
		
		String uddiURL = "http://localhost:8081";
		String name0 = "SdStore0";
		String name1 = "SdStore1";
		String name2 = "SdStore2";
		String endpointAddress0 = null;
		String endpointAddress1 = null;
		String endpointAddress2 = null;
		
		try {
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			endpointAddress0 = uddiNaming.lookup(name0);
			endpointAddress1 = uddiNaming.lookup(name1);
			endpointAddress2 = uddiNaming.lookup(name2);
		} catch (JAXRException e) {
			e.printStackTrace();
		}
		
		SDStore_Service service = new SDStore_Service();
		BindingProvider bindingProvider;
		Map<String, Object> requestContext;
		
		port0 = service.getSDStoreImplPort();
		bindingProvider = (BindingProvider) port0;
		requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress0);
		
		port1 = service.getSDStoreImplPort();
		bindingProvider = (BindingProvider) port1;
		requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress1);
		
		port2 = service.getSDStoreImplPort();
		bindingProvider = (BindingProvider) port2;
		requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress2);	
	}
	
	public byte[] load (DocUserPair docUserPair) {
		
		byte[] byte0 = null;
		byte[] byte1 = null;
		byte[] byte2 = null;
		
		try {
			byte0 = port0.load(docUserPair);
			byte1 = port1.load(docUserPair);
			byte2 = port2.load(docUserPair);
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}
		
		if (byte0.equals(byte1)) {
			return byte0;
		} else if (byte0.equals(byte2)) {
			return byte0;
		} else {
			return byte1;
		}
				
	}
	
	public void store (DocUserPair docUserPair, byte[] contents) {
		try {
			port0.store(docUserPair, contents);
			port1.store(docUserPair, contents);
			port2.store(docUserPair, contents);
		} catch (CapacityExceeded_Exception e) {
			e.printStackTrace();
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}
	}
	
}
