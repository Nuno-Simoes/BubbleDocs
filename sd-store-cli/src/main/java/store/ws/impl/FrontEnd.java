package store.ws.impl;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import store.ws.handler.RelayClientHandler;
import store.ws.uddi.UDDINaming;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class FrontEnd {

	private static SDStore port0;
	private static SDStore port1;
	private static SDStore port2;
	public static  String senderToken;
	public static String reciverToken;
		
	String endpointAddress0 = null;
	String endpointAddress1 = null;
	String endpointAddress2 = null;
	
	BindingProvider bindingProvider0;
	BindingProvider bindingProvider1;
	BindingProvider bindingProvider2;
	
	Map<String, Object> requestContext0;
	Map<String, Object> requestContext1;
	Map<String, Object> requestContext2;
	
	Map<String, Object> responseContext0;
	Map<String, Object> responseContext1;
	Map<String, Object> responseContext2;

	public static FrontEnd frontEnd = null;

	public FrontEnd() {
		this.connect();
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

		try {
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			endpointAddress0 = uddiNaming.lookup(name0);
			endpointAddress1 = uddiNaming.lookup(name1);
			endpointAddress2 = uddiNaming.lookup(name2);
		} catch (JAXRException e) {
			e.printStackTrace();
		}

		SDStore_Service service = new SDStore_Service();
		
		port0 = service.getSDStoreImplPort();
		bindingProvider0 = (BindingProvider) port0;
		requestContext0 = bindingProvider0.getRequestContext();

		port1 = service.getSDStoreImplPort();
		bindingProvider1 = (BindingProvider) port1;
		requestContext1 = bindingProvider1.getRequestContext();

		port2 = service.getSDStoreImplPort();
		bindingProvider2 = (BindingProvider) port2;
		requestContext2 = bindingProvider2.getRequestContext();
	}

	public byte[] load (DocUserPair docUserPair) {

		// root element
		Element simpleDocument = new Element("simpleDocument");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(simpleDocument);

		simpleDocument.setAttribute(new Attribute("userId", docUserPair.getUserId()));
		simpleDocument.setAttribute(new Attribute("docId", docUserPair.getDocumentId()));

		senderToken = new XMLOutputter().outputString(doc);

		try {
			requestContext0.put(RelayClientHandler.REQUEST_PROPERTY, senderToken);
			requestContext0.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress0);
			port0.load(docUserPair);
			
			requestContext1.put(RelayClientHandler.REQUEST_PROPERTY, senderToken);
			requestContext1.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress1);
			port1.load(docUserPair);
			
			requestContext2.put(RelayClientHandler.REQUEST_PROPERTY, senderToken);
			requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress2);
			port2.load(docUserPair);
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}
		
		// waits for Q responses
		responseContext0 = bindingProvider0.getResponseContext();
		responseContext1 = bindingProvider1.getResponseContext();
		responseContext2 = bindingProvider2.getResponseContext();
		
		String finalValue0 = (String) responseContext0.get(RelayClientHandler.RESPONSE_PROPERTY);
		String finalValue1 = (String) responseContext1.get(RelayClientHandler.RESPONSE_PROPERTY);
		String finalValue2 = (String) responseContext2.get(RelayClientHandler.RESPONSE_PROPERTY);
		
		org.jdom2.Document jdomDoc0 = null;
		org.jdom2.Document jdomDoc1 = null;
		org.jdom2.Document jdomDoc2 = null;

		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		try {
			jdomDoc0 = builder.build(new ByteArrayInputStream(parseBase64Binary(finalValue0)));
			jdomDoc1 = builder.build(new ByteArrayInputStream(parseBase64Binary(finalValue1)));
			jdomDoc2 = builder.build(new ByteArrayInputStream(parseBase64Binary(finalValue2)));
		} catch (JDOMException je) {
			je.printStackTrace();
		} catch (IOException ie) {
			ie.printStackTrace();
		}

		Element root0 = jdomDoc0.getRootElement();
		Element tag0 = root0.getChild("tag");
		int receivedSeq0 = Integer.parseInt(tag0.getAttributeValue("seq"));
		
		Element root1 = jdomDoc1.getRootElement();
		Element tag1 = root1.getChild("tag");
		int receivedSeq1 = Integer.parseInt(tag1.getAttributeValue("seq"));
		
		Element root2 = jdomDoc2.getRootElement();
		Element tag2 = root2.getChild("tag");
		int receivedSeq2 = Integer.parseInt(tag2.getAttributeValue("seq"));
		
		if(receivedSeq0 > receivedSeq1 && receivedSeq0 > receivedSeq2) {
			Element document0 = root0.getChild("document");
			return parseBase64Binary(document0.getAttributeValue("content"));
		}else if(receivedSeq1 > receivedSeq0 && receivedSeq1 > receivedSeq2) {
			Element document1 = root1.getChild("document");
			return parseBase64Binary(document1.getAttributeValue("content"));
		}else{
			Element document2 = root2.getChild("document");
			return parseBase64Binary(document2.getAttributeValue("content"));
		}
	}

	public void store (DocUserPair docUserPair, byte[] contents) {
		
		// read to find maxseq
		
		// root element
		Element root = new Element("root");
		org.jdom2.Document doc = new org.jdom2.Document();
		doc.setRootElement(root);

		// tag element
		Element tag = new Element("tag");
		tag.setAttribute(new Attribute("seq", "")); // send maxseq+1
		tag.setAttribute(new Attribute("pid", ""));

		doc.getRootElement().addContent(tag);

		// doc element
		Element document = new Element("document");
		document.setAttribute(new Attribute("userId", docUserPair.getUserId()));
		document.setAttribute(new Attribute("docId", docUserPair.getDocumentId()));
		document.setAttribute(new Attribute("content", printBase64Binary(contents)));
		
		doc.getRootElement().addContent(document);

		senderToken = new XMLOutputter().outputString(doc);

		try {
			requestContext0.put(RelayClientHandler.REQUEST_PROPERTY, senderToken);
			requestContext0.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress0);
			port0.store(docUserPair, contents);
			
			requestContext1.put(RelayClientHandler.REQUEST_PROPERTY, senderToken);
			requestContext1.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress1);
			port1.store(docUserPair, contents);
			
			requestContext2.put(RelayClientHandler.REQUEST_PROPERTY, senderToken);
			requestContext2.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress2);
			port2.store(docUserPair, contents);
		} catch (CapacityExceeded_Exception e) {
			e.printStackTrace();
		} catch (DocDoesNotExist_Exception e) {
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			e.printStackTrace();
		}
		
		// waits for Q acks
		responseContext0 = bindingProvider0.getResponseContext();
		responseContext1 = bindingProvider1.getResponseContext();
		responseContext2 = bindingProvider2.getResponseContext();
	}
}
