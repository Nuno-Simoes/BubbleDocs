package store.ws.impl;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import store.ws.impl.uddi.UDDINaming;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class StoreTest {

	static private String uddiURL = "http://localhost:8081";
	static private String name = "SdStore";
	static protected SDStore port;
		    	
    // static members
    // one-time initialisation and clean-up
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

    // members
    // initialization and clean-up for each test
    @Before
    public void setUp() {}

    @After
    public void tearDown() {}
}