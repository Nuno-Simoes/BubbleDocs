package pt.ulisboa.tecnico.bubbledocs.integration.component;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.RootUser;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;

public class BubbleDocsIntegratorTest {
	
    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }
    
    
    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
            
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }
    
    public void populate4Test() {
    }
    
    public User createUser(String username, String name, String email) 
    		throws DuplicateUsernameException {
    	Portal p = Portal.getInstance();
    	RootUser r = RootUser.getInstance();
    	r.addUser(username, name, email);
    	
    	return p.findUser(username);
    }

    public Spreadsheet createSpreadSheet(User user, String name, int row,
            int column) {
    	Portal p = Portal.getInstance();
    	user.createSpreadsheet(name, row, column);
    	return p.findSpreadsheet(user, name);	
    }

    public Spreadsheet getSpreadSheet(String name) 
    		throws SpreadsheetDoesNotExistException {
    	Portal p = Portal.getInstance();
    	return p.findSpreadsheet(name);
    }

    public User getUserFromUsername(String username) throws LoginBubbleDocsException {
    	Portal p = Portal.getInstance();
    	return p.findUser(username);
    }

    public String addUserToSession(String username) {
    	Portal p = Portal.getInstance();
    	Session s = Session.getInstance();

    	User u = p.findUser(username);
    	s.login(username, u.getPassword(), false);
    	return u.getToken();
    }

    public void removeUserFromSession(String token) {
    	Session s = Session.getInstance();
    	Portal p = Portal.getInstance();
    	
    	User u = p.findUserByToken(token);
    	s.logout(u);
    }

    public User getUserFromSession(String token) throws LoginBubbleDocsException {
    	Session s  = Session.getInstance();
    	User u = s.getLoggedUser(token);
    	return u;
    }
    
    public float getLastAccessTimeInSession(String userToken) {
    	Portal p = Portal.getInstance();
    	User u = p.findUserByToken(userToken);
    	return u.getSessionTime();
    }
}