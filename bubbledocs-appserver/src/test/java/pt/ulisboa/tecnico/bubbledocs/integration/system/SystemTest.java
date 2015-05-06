package pt.ulisboa.tecnico.bubbledocs.integration.system;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.User;

public class SystemTest {

	@Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }
	
	 @After
	    public void tearDown() {
	        try {
	        	deleteRemains();
	            FenixFramework.getTransactionManager().rollback();
	        } catch (IllegalStateException | SecurityException | SystemException e) {
	            e.printStackTrace();
	        }
	    }
	
    public void deleteRemains() {
    	Portal p = Portal.getInstance();
    	RootUser r = RootUser.getInstance();
    	for (User u : p.getUsersSet()) {
    		r.removeUser(u.getUsername());
    	}
    }
}
