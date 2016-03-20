
package pt.ulisboa.tecnico.sdis.id.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.10
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SDId", targetNamespace = "urn:pt:ulisboa:tecnico:sdis:id:ws", wsdlLocation = "file:/Users/nuno/Downloads/T_04_07_23-project/sd-id/src/main/resources/SD-ID.1_1.wsdl")
public class SDId_Service
    extends Service
{

    private final static URL SDID_WSDL_LOCATION;
    private final static WebServiceException SDID_EXCEPTION;
    private final static QName SDID_QNAME = new QName("urn:pt:ulisboa:tecnico:sdis:id:ws", "SDId");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/Users/nuno/Downloads/T_04_07_23-project/sd-id/src/main/resources/SD-ID.1_1.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SDID_WSDL_LOCATION = url;
        SDID_EXCEPTION = e;
    }

    public SDId_Service() {
        super(__getWsdlLocation(), SDID_QNAME);
    }

    public SDId_Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), SDID_QNAME, features);
    }

    public SDId_Service(URL wsdlLocation) {
        super(wsdlLocation, SDID_QNAME);
    }

    public SDId_Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SDID_QNAME, features);
    }

    public SDId_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SDId_Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SDId
     */
    @WebEndpoint(name = "SDIdImplPort")
    public SDId getSDIdImplPort() {
        return super.getPort(new QName("urn:pt:ulisboa:tecnico:sdis:id:ws", "SDIdImplPort"), SDId.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SDId
     */
    @WebEndpoint(name = "SDIdImplPort")
    public SDId getSDIdImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("urn:pt:ulisboa:tecnico:sdis:id:ws", "SDIdImplPort"), SDId.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SDID_EXCEPTION!= null) {
            throw SDID_EXCEPTION;
        }
        return SDID_WSDL_LOCATION;
    }

}
