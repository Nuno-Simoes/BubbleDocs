package pt.ulisboa.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;


import pt.ulisboa.tecnico.bubbledocs.domain.*;



public class SetupDomain {
	
    public static void main (String[] args) {
    	populateDomain();
    }
    
    @Atomic
    static void populateDomain() {
    	
    	Portal portal = Portal.getInstance();

    	Spreadsheet spreadsheet1 = new Spreadsheet("Notas ES", 300, 20);
    	portal.addSpreadsheets(spreadsheet1);
    	
    	User user1 = new User("pf", "PauL Door", "sub");
    	User user2 = new User("ra", "Step Rabbit", "cor");
    	portal.addUsers(user1);
    	portal.addUsers(user2);
    	
    	Literal literal1 = new Literal(5);
        Cell cell1 = new Cell(3, 4);
        cell1.setContent(literal1);

        Cell cell2 = new Cell(5, 6);
        Cell cell3 = new Cell(1, 1);
        Reference reference1 = new Reference(cell2);
        cell2.setContent(reference1);

        Argument literal2 = new Literal(2);
        Argument reference2 = new Reference(cell1);
        BinaryFunction function1 = new Add(literal2, reference2);
        cell1.setContent(function1);
     
        Cell cell4 = new Cell (2, 2);
        Argument reference3 = new Reference(cell1);
        Argument reference4 = new Reference(cell3);
        BinaryFunction function2 = new Div(reference4, reference3);
        cell4.setContent(function2);
        
  
}}