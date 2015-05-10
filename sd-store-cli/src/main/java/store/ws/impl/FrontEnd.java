package store.ws.impl;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
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
		return new byte[1024];
	}
	
	public void store (DocUserPair docUserPair, byte[] contents) {
		
	}
	
}
