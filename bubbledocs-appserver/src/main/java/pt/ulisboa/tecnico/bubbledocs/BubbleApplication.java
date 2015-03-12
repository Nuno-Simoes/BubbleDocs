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
    		
    		//recoverFromBackup(file);
    		
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
    	//if (portal.getUsersSet().isEmpty()) {
    		SetupDomain.populateDomain();
    	//}
    }
    
	
    @Atomic
    private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
    	Spreadsheet s = new Spreadsheet();
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
    
}