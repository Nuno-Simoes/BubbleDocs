package pt.ulisboa.tecnico.bubbledocs;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserNotLoggedException;
import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralToCellService;
import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceCellService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadsheetService;
import pt.ulisboa.tecnico.bubbledocs.service.CreateUserService;
import pt.ulisboa.tecnico.bubbledocs.service.RemoveUserService;

public class BubbleApplication {
	public static void main (String[] args) {
		System.out.println("Welcome To The Bubble Docs Application!");
		
		TransactionManager tm = FenixFramework.getTransactionManager();
    	boolean committed = false;
    	
       	try {
    	    tm.begin();
    	    
    	    Portal portal = Portal.getInstance();
    	    
    	    setupIfNeed(portal);
    		
    		portal.listUsers();
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf"))
    			System.out.println(s.getName());
    		
    		for (Spreadsheet s : portal.listSpreadsheets("ra"))
    			System.out.println(s.getName());
    		
    		List<org.jdom2.Document> doc = new ArrayList<org.jdom2.Document>();
    		org.jdom2.Document file = new org.jdom2.Document();
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    			if (s.getName().equals("Notas ES"))
    				file = convertToXML(s);
    			doc.add(convertToXML(s));
    		}
    		
    		for (org.jdom2.Document d : doc) {
    			printDomainInXML(d);
    		}
    		
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
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    			doc.add(convertToXML(s));
    		}
    		
    		for (org.jdom2.Document d : doc)
    			printDomainInXML(d);
    	    
    	    tm.commit();
    	    committed = true;
    	} catch (SystemException | NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
    	    System.err.println("Error in execution of transaction: " + ex);
    	} finally {
    	    if (!committed) 
    		try {
    		    tm.rollback();
    		} catch (SystemException ex) {
    		    System.err.println("Error in roll back of transaction: " + ex);
    		}
    	}
	}
	
    private static void setupIfNeed(Portal portal) {
    	if (portal.getUsersSet().isEmpty()) {
    		SetupDomain.populateDomain();
    	}
    }
	
    @Atomic
    private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
    	Portal portal = Portal.getInstance();
    	Spreadsheet s = new Spreadsheet();
    	portal.addSpreadsheets(s);
    	s.importFromXML(jdomDoc.getRootElement());
    }
    
    @Atomic
    public static org.jdom2.Document convertToXML(Spreadsheet s) {
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		jdomDoc.setRootElement(s.exportToXML());
		return jdomDoc;
    }

    @Atomic
    public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
    }
    
    private static void assignLiteralToCell(String accessUsername,  int docId,
			String cellId, String literal){
              try{
                 AssignLiteralToCellService service=new AssignLiteralToCellService(accessUsername,docId,cellId,literal);
                 service.execute();
                 }
              catch(SpreadsheetDoesNotExistException | OutOfBoundsException
            		 | InvalidContentException | InvalidPermissionException e){
            	  System.err.println("Error assigning literal to cell" + e.getMessage());}
    			}
    
    private static void assignReferenceToCell(String tokenUser, int sheetId, String cellId,
            String reference){
              try{
                 AssignReferenceCellService service=new AssignReferenceCellService(tokenUser,sheetId,cellId,reference);
                 service.execute();
                 }
              catch(SpreadsheetDoesNotExistException | OutOfBoundsException
            		 | InvalidContentException | InvalidPermissionException e){
            	  System.err.println("Error assigning reference to cell" + e.getMessage());}
    			}
    
    private static void createSpreadsheet(String userToken, String sheetName, int lines, 
			int columns){
              try{
                 CreateSpreadsheetService service=new CreateSpreadsheetService(userToken,sheetName,lines,columns);
                 service.execute();
                 }
              catch(UserNotLoggedException | UserDoesNotExistException e){
            	  System.err.println("Error creating spreadsheet" + e.getMessage());}
    			}
    
    private static void createUser(String userToken, String newUsername, 
			String password, String name){
              try{
                 CreateUserService service=new CreateUserService(userToken,newUsername,password,name);
                 service.execute();
                 }
              catch(InvalidPermissionException |
            			UserNotLoggedException | EmptyUsernameException | UserAlreadyExistsException e){
            	  System.err.println("Error creating user" + e.getMessage());}
    			}
    
    private static void removeUser(String userToken, String username){
              try{
                 RemoveUserService service=new RemoveUserService(userToken,username);
                 service.execute();
                 }
              catch(InvalidPermissionException | 
            			UserNotLoggedException | EmptyUsernameException | UserAlreadyExistsException e){
            	  System.err.println("Error removing user" + e.getMessage());}
    			}

    
}