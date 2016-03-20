package pt.ulisboa.tecnico.bubbledocs.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSessionException;

public class ImportDocumentService extends BubbleDocsService {
	
    private final byte[] doc;
    private final String username;
    private Spreadsheet sheet;

    public ImportDocumentService (byte[] doc, String username) {
    	this.doc = doc;
    	this.username = username;
    }

    @Override
    protected void dispatch() throws InvalidSessionException, 
    		ImportDocumentException {
    	User u = Portal.getInstance().findUser(username);
    	Session s = Session.getInstance();
    	
    	if(!(s.isInSession(u))) {
    		throw new InvalidSessionException(u.getUsername());
    	}
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
    	this.sheet = portal.importFromXML(rootElement, username);
    }
    
    public final Spreadsheet getResult() {
		return this.sheet;
	}
    
}