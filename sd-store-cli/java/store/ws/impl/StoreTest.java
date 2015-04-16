package store.ws.impl;

import org.junit.*;

public class StoreTest {

	String uddiURL = "http://localhost:8081";
	String name = "SdStore";
	
	<ws.url>http://localhost:8080/store-ws/endpoint</ws.url>
	    	
    // static members
    // one-time initialization and clean-up
    @BeforeClass
    public static void oneTimeSetUp() {
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	String endpointAdress = uddiNaming.lookup(name);
    }

    @AfterClass
    public static void oneTimeTearDown() {

    }


    // members
    // initialization and clean-up for each test
    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

}