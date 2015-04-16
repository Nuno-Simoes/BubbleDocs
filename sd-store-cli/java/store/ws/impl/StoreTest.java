package store.ws.impl;

import org.junit.*;

public class StoreTest {

	String uddiURL = "http://localhost:8081";
	String name = "SdStore";
	SDStore port;
		    	
    @BeforeClass
    public static void oneTimeSetUp() {
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	String endpointAdress = uddiNaming.lookup(name);
    	SDStore_Service service = new SDStore_Service();
    	port = service.getSDStoreImplPort();
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	
    }

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}
}