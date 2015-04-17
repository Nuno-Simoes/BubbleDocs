package store.ws.impl;

import javax.xml.ws.WebServiceException;

import org.junit.Test;

import mockit.*;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;

public class WebServiceConnection extends StoreServiceTest {
	
	@Mocked private SDStore_Service service;
	@Mocked private SDStore port;
	
	@Test(expected=WebServiceException.class)
    public void testMockServerException() throws Exception {
		
        new Expectations() {{
            service.getSDStoreImplPort(); result = port;
        	port.createDoc(null);
        	result = new WebServiceException("fabricated"); 
        }};
        
        SDStore_Service test = new SDStore_Service();
        SDStore test1 =test.getSDStoreImplPort();
        test1.createDoc(null);
    }
}
