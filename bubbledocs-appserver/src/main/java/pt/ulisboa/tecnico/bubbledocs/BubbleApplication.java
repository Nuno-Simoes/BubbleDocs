package pt.ulisboa.tecnico.bubbledocs;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

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
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    			//Utilizando a funcionalidade de exportacao, converte cada folha de calculo
    			//para o formato XML e escreve no terminal o resultado desta conversao.
    		}
    		
    		portal.removeSpreadsheet("pf", "Notas ES");
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    			System.out.print("Name: " + s.getName());
    			System.out.println(" Id: " + s.getId());
    		}
    		
    		//Utilizar a funcionalidade de importac¸ao para criar uma folha de calculo
    		//semelhante a que foi exportada anteriormente e que foi removida agora.
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    			System.out.print("Name: " + s.getName());
    			System.out.println(" Id: " + s.getId());
    		}
    		
    		for (Spreadsheet s : portal.listSpreadsheets("pf")) {
    			//Utilizando a funcionalidade de exportacao, converte cada folha de calculo
    			//para o formato XML e escreve no terminal o resultado desta conversao.
    		}   
    	    
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
	if (portal.getUsersSet().isEmpty());
		//SetupDomanin.populateDomain();
    }
    
}