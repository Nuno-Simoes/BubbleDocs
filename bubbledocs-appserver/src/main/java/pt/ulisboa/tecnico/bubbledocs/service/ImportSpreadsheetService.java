package pt.ulisboa.tecnico.bubbledocs.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

public class ImportSpreadsheetService extends PortalService {
    private final byte[] doc;

    public ImportSpreadsheetService (byte[] doc) {
    	this.doc = doc;
    }

    @Override
    protected void dispatch() {
    	org.jdom2.Document jdomDoc;
    	
    	SAXBuilder builder = new SAXBuilder();
    	builder.setIgnoringElementContentWhitespace(true);
    	
    	try {
    		jdomDoc = builder.build(new ByteArrayInputStream(doc));
    	} catch (JDOMException | IOException e) {
    		throw new ImportDocumentException();
    	}
    	
    	Element rootElement = jdomDoc.getRootElement();
    	Portal portal = Portal.getInstance();
    	portal.importFromXML(rootElement);
    }
    
}