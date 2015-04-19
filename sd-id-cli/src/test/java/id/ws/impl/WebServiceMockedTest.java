package id.ws.impl;

import org.junit.Test;
import mockit.Expectations;
import mockit.Mocked;
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;

import javax.xml.ws.WebServiceException;

public class WebServiceMockedTest extends IdTest {
	
	@Mocked private SDId_Service service;
	@Mocked private SDId port;
	
	@Test(expected=WebServiceException.class)
    public void testMockServerException() throws Exception {
		
        new Expectations() {{
            service.getSDIdImplPort(); result = port;
        	port.createUser(anyString, anyString);
        	result = new WebServiceException("WebServiceException"); 
        }};
        
        SDId_Service service2 = new SDId_Service();
        SDId port2 = service2.getSDIdImplPort();
        port2.createUser("joaquim", "joaquim@tecnico.pt");
    }
}