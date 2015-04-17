package store.ws.impl;

import javax.xml.registry.JAXRException;

import org.junit.Test;

import store.ws.uddi.UDDINaming;
import mockit.*;

public class ConnectionTest extends StoreServiceTest {
	
	@Mocked private UDDINaming connection;
	
	@Test (expected = JAXRException.class)
	public void remoteInvocationError() throws JAXRException{
		new Expectations() {{
			connection.lookup(anyString);
			result = new JAXRException();
		}};
		
		new UDDINaming("string1").lookup("string2");
	}
}
