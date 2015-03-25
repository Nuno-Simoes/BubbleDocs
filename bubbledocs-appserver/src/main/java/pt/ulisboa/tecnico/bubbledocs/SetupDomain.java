package pt.ulisboa.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.bubbledocs.domain.Add;
import pt.ulisboa.tecnico.bubbledocs.domain.BinaryFunction;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Div;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralToCellService;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCellService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadsheetService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUserService;
import pt.ulisboa.tecnico.bubbledocs.service.LoginUserService;

public class SetupDomain {
	
    public static void main (String[] args) {
    	populateDomain();
    }
    
    @Atomic
    static void populateDomain() {
    	String root = null;
 
    	
             try {
                LoginUserService service  = new LoginUserService("root", "rootroot");
                service.execute();
                root = service.getUserToken();
            } catch (InvalidPermissionException ie) {
                System.err.println("Error in login " + ie.getMessage());
            }
    	
    	Portal portal = Portal.getInstance();
    	
    	CreateUserService createUser1 = new CreateUserService(root, "pf", "Paul Door", "sub");
    	createUser1.execute();
    	
    	CreateUserService createUser2 = new CreateUserService(root, "ra", "Step Rabbit", "cor");
    	createUser2.execute();
    	
    	String user = createUser1.getResult();
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

}