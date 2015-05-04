package pt.ulisboa.tecnico.bubbledocs;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.bubbledocs.domain.Add;
import pt.ulisboa.tecnico.bubbledocs.domain.BinaryFunction;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Div;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ExportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralToCellService;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCellService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadsheetService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUserService;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocumentService;
//import pt.ulisboa.tecnico.bubbledocs.service.ImportSpreadsheetService;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUserService;

public class BubbleApplication {
	public static void main (String[] args) {
		System.out.println("Welcome To The Bubble Docs Application!");
		populateDomain();	
	}
	
	@Atomic
	private static void populateDomain() {
		Portal portal = Portal.getInstance();
		setup(portal);
		portal.listUsers();
		int id = 0;
		
		for (Spreadsheet s : portal.listSpreadsheets("pfsss")) {
			System.out.println(s.getName());
			id = s.getId();
		}
		
		for (Spreadsheet s : portal.listSpreadsheets("rasss")) {
			System.out.println(s.getName());
		}
		
		LoginUserService login = new LoginUserService("pfsss", "sub");
		login.execute();
		String userToken = login.getUserToken();
				
		byte[] file = convertToXML(userToken, id);
		
		portal.removeSpreadsheet("pfsss", "Notas ES");
		
    	for (Spreadsheet s : portal.listSpreadsheets("pfsss")) {
    		System.out.print("Name: " + s.getName());
    		System.out.println(", Id: " + s.getId());
    	}
    		
    	//recoverFromBackup(file);
    		
    	for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    		System.out.print("Name: " + s.getName());
    		System.out.println(", Id: " + s.getId());
    	}
    	
    	file = convertToXML(userToken, id);
    	printDomainInXML(file); 
	}
	
    private static void setup(Portal portal) {
    	
    		if(!portal.getUsersSet().isEmpty() 
    				|| !portal.getSpreadsheetsSet().isEmpty() ) {
    			for (User userToDelete : portal.getUsersSet()) {
    				portal.removeSpreadsheet(userToDelete);
    				portal.removeUsers(userToDelete);
    			}
    			for (Spreadsheet spreadToDelete : portal.getSpreadsheetsSet()) {
    				portal.removeSpreadsheet(spreadToDelete.getId());
    			}
    			portal.setSheetId(0);
    			portal.setUserId(0);
    		}
    			
    		String root = null;

    		try {
    			LoginUserService service  = new LoginUserService("root", "rootroot");
    			service.execute();
    			root = service.getUserToken();
    		} catch (InvalidPermissionException ie) {
    			System.err.println("Error in login " + ie.getMessage());
    		}

    		CreateUserService createUser1 = new CreateUserService(root, "pfsss", "pf@pf.com", "Paul Door");
    		createUser1.execute();

    		CreateUserService createUser2 = new CreateUserService(root, "rasss", "ra@ra.com", "Step Rabbit");
    		createUser2.execute();
    		
    		Portal p = Portal.getInstance();
    		
    		User pf = p.findUser("pfsss");
    		pf.setPassword("sub");
    		
    		LoginUserService login = new LoginUserService("pfsss", "sub");
    		login.execute();
    		String user = login.getUserToken();
    		
    		CreateSpreadsheetService createSpreadsheet= new CreateSpreadsheetService(user, "Notas ES", 300, 20);
    		createSpreadsheet.execute();
    		/* Spreadsheet sheet = portal.findSpreadsheet(user, "Notas ES");*/
    		
    		String spreadsheet = createSpreadsheet.getResult();

    		int docId = 0;
    		Spreadsheet ss = null;

    		for (Spreadsheet s : portal.getSpreadsheetsSet()){
    			if(s.getName().equals(spreadsheet)){
    				docId = s.getId();
    				ss = s;
    				break;
    			}
    		}

    		AssignLiteralToCellService assignliteral = new AssignLiteralToCellService(user, docId, "3;4", "5");
    		assignliteral.execute();
    		AssignReferenceCellService assignreference = new AssignReferenceCellService(user, docId, "5;6", "1;1");
    		assignreference.execute();


    		Literal literal = new Literal(2);
    		Cell cell = ss.getCell(3, 4);
    		Reference reference = new Reference(cell);
    		BinaryFunction function = new Add(literal, reference);
    		ss.setContent(5, 6, function);

    		cell = ss.getCell(1, 1);
    		reference = new Reference(cell);
    		cell = ss.getCell(3, 4);
    		Reference reference2 = new Reference(cell);
    		function = new Div(reference, reference2);
    		ss.setContent(2, 2, function);
   }
   
    
    public static byte[] convertToXML (String userToken, int docId) {
    	try {
    		ExportDocumentService exportService = new ExportDocumentService(userToken, docId);
    		exportService.execute();
    		return exportService.getResult();
    	} catch (ExportDocumentException | LoginBubbleDocsException | 
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
    
    /*private static void recoverFromBackup(byte[] doc) {
    	try {
    		ImportSpreadsheetService importService = new ImportSpreadsheetService(doc);
    		importService.execute();
    	} catch (ImportDocumentException ide) {
    		System.err.println("Error importing document");
    	}
    } */  
}