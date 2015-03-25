package pt.ulisboa.tecnico.bubbledocs;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ExportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocumentService;
import pt.ulisboa.tecnico.bubbledocs.service.ImportSpreadsheetService;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUserService;

public class BubbleApplication {
	public static void main (String[] args) {
		System.out.println("Welcome To The Bubble Docs Application!");
		
		Portal portal = Portal.getInstance();
		setupIfNeed(portal);
		portal.listUsers();
		int id = 0;
		
		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
			System.out.println(s.getName());
			id = s.getId();
		}
		
		for (Spreadsheet s : portal.listSpreadsheets("ra")) {
			System.out.println(s.getName());
		}
		
		LoginUserService login = new LoginUserService("pf", "sub");
		login.execute();
		String userToken = login.getUserToken();
		
		byte[] file = convertToXML(userToken, id);
		
		printDomainInXML(file);
		
		portal.removeSpreadsheet("pf", "Notas ES");
    		
    	for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    		System.out.print("Name: " + s.getName());
    		System.out.println(", Id: " + s.getId());
    	}
    		
    	recoverFromBackup(file);
    		
    	for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    		System.out.print("Name: " + s.getName());
    		System.out.println(", Id: " + s.getId());
    	}
    	
    	file = convertToXML(userToken, id);
    	printDomainInXML(file);
    	
	}
	
    private static void setupIfNeed(Portal portal) {
    	if (portal.getUsersSet().isEmpty()) {
    		SetupDomain.populateDomain();
    	}
    }
    
    public static byte[] convertToXML (String userToken, int docId) {
    	try {
    		ExportDocumentService exportService = new ExportDocumentService(userToken, docId);
    		exportService.execute();
    	} catch (ExportDocumentException | UserNotLoggedException | 
    			InvalidPermissionException e) {
    		System.err.println("Error in exporting to XML: " + e.getMessage());
    	}
    	return null;
    }
    
    public static void printDomainInXML (byte[] doc) {
    	if (doc == null) {
    		System.err.println("Null document to print");
    		return;
    	}
    	
    	org.jdom2.Document jdomDoc;
    	SAXBuilder builder = new SAXBuilder();
    	builder.setIgnoringElementContentWhitespace(true);
    	
    	try {
    		jdomDoc = builder.build(new ByteArrayInputStream(doc));
    	} catch (JDOMException | IOException e) {
    		e.printStackTrace();
    		throw new ImportDocumentException();
    	}
    	
    	XMLOutputter xml = new XMLOutputter();
    	xml.setFormat(Format.getPrettyFormat());
    	System.out.println(xml.outputString(jdomDoc));
    }
    
    private static void recoverFromBackup(byte[] doc) {
    	try {
    		ImportSpreadsheetService importService = new ImportSpreadsheetService(doc);
    		importService.execute();
    	} catch (ImportDocumentException ide) {
    		System.err.println("Error importing document");
    	}
    }   
}