package id.ws.impl;

import javax.xml.registry.JAXRException;

import org.junit.Test;

import id.ws.uddi.UDDINaming;
import mockit.Expectations;
import mockit.Mocked;

public class ConnectionTest extends IdTest {
	
	@Mocked private UDDINaming uddi;
	
	@Test(expected = JAXRException.class)
	public void remoteInvocationError() throws JAXRException {
		new Expectations() {{
			uddi.lookup(anyString);
			result = new JAXRException();
		}};
		
		new UDDINaming("ConnectionTest1").lookup("ConnectionTest2");
	}
	
}