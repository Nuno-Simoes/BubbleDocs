package store.ws.impl;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import org.jdom2.*;
import org.jdom2.output.XMLOutputter;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.uddi.UDDINaming;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class FrontEnd {

	private static SDStore port0;
	private static SDStore port1;
	private static SDStore port2;
	public static  String token;

	private int seq;

	public static FrontEnd frontEnd = null;

	public FrontEnd() {
		this.connect();
		this.seq=0;
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

		port1 = service.getSDStoreImplPort();
		bindingProvider = (BindingProvider) port1;
		requestContext = bindingProvider.getRequestContext();

		port2 = service.getSDStoreImplPort();
		bindingProvider = (BindingProvider) port2;
		requestContext = bindingProvider.getRequestContext();
	}

	public byte[] load (DocUserPair docUserPair) {

		byte[] byte0 = null;
		byte[] byte1 = null;
		byte[] byte2 = null;

		try {
			byte0 = port0.load(docUserPair);
			byte1 = port1.load(docUserPair);
			byte2 = port2.load(docUserPair);
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}

		if (byte0.equals(byte1)) {
			return byte0;
		} else if (byte0.equals(byte2)) {
			return byte0;
		} else {
			return byte1;
		}

	}

	public void store (DocUserPair docUserPair, byte[] contents) {

		// root element
		Element root = new Element("root");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(root);

		// tag element
		Element tag = new Element("tag");
		tag.setAttribute(new Attribute("seq", Integer.toString(seq)));
		tag.setAttribute(new Attribute("pid", ""));

		doc.getRootElement().addContent(tag);

		// doc element
		Element document = new Element("document");
		document.setAttribute(new Attribute("userId", docUserPair.getUserId()));
		document.setAttribute(new Attribute("docId", docUserPair.getDocumentId()));
		document.setAttribute(new Attribute("content", printBase64Binary(contents)));
		
		doc.getRootElement().addContent(document);

		token = new XMLOutputter().outputString(doc);

		try {
			port0.store(docUserPair, contents);
			port1.store(docUserPair, contents);
			port2.store(docUserPair, contents);
		} catch (CapacityExceeded_Exception e) {
			e.printStackTrace();
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}

		this.seq++;
	}
}
