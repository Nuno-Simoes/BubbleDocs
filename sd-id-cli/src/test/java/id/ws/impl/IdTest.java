package id.ws.impl;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import id.ws.uddi.UDDINaming;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class IdTest {

	static private String uddiURL = "http://localhost:8081";
	static private String name = "SdId";
	static protected SDId port;
		    	
    //static members
    //one-time initialisation and clean-up
    @BeforeClass
    public static void oneTimeSetUp() throws JAXRException {
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	String endpointAddress = uddiNaming.lookup(name);
    	SDId_Service service = new SDId_Service();
    	port = service.getSDIdImplPort();
    	BindingProvider bindingProvider = (BindingProvider) port;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	port = null;
    }

    //members
    //initialisation and clean-up for each test
    @Before
    public void setUp() {}

    @After
    public void tearDown() {}
}