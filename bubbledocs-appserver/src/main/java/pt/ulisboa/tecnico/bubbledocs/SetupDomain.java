package pt.ulisboa.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.bubbledocs.domain.Add;
import pt.ulisboa.tecnico.bubbledocs.domain.BinaryFunction;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Div;
import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;

public class SetupDomain {
	
    public static void main (String[] args) {
    	populateDomain();
    }
    
    @Atomic
    static void populateDomain() {
    	
    	Portal portal = Portal.getInstance();
    	RootUser root = RootUser.getInstance();
    	
    	root.addUser("pf", "Paul Door", "sub");
    	root.addUser("ra", "Step Rabbit", "cor");
    	
    	User user = portal.findUser("pf");
    	user.createSpreadsheet("Notas ES", 300, 20);
    	
    	Spreadsheet sheet = portal.findSpreadsheet(user, "Notas ES");
    	
    	Literal literal = new Literal(5);
    	sheet.setContent(3, 4, literal);
    	
    	Cell cell = sheet.getCell(5, 6);
    	Reference reference = new Reference(cell);
    	sheet.setContent(1, 1, reference);
    	
    	literal = new Literal(2);
    	cell = sheet.getCell(3, 4);
    	reference = new Reference(cell);
    	BinaryFunction function = new Add(literal, reference);
    	sheet.setContent(5, 6, function);
    	
    	cell = sheet.getCell(1, 1);
    	reference = new Reference(cell);
    	cell = sheet.getCell(3, 4);
    	Reference reference2 = new Reference(cell);
    	function = new Div(reference, reference2);
    	sheet.setContent(2, 2, function);
    }

}