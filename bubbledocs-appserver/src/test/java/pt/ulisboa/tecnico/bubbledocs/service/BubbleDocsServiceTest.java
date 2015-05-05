package pt.ulisboa.tecnico.bubbledocs.service;

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
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;


public class BubbleDocsServiceTest {
	
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
        	deleteRemains();
            FenixFramework.getTransactionManager().rollback();
            
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }
    

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {
    }
    
    public void deleteRemains() {
    	Portal p = Portal.getInstance();
    	RootUser r = RootUser.getInstance();
    	for ( User u : p.getUsersSet()) {
    		r.removeUser(u.getUsername());
    	}
    }

    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the initial state and checking that the service has the expected behavior
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

    // returns a spreadsheet whose name is equal to name
    public Spreadsheet getSpreadSheet(String name) 
    		throws SpreadsheetDoesNotExistException {
    	Portal p = Portal.getInstance();
    	return p.findSpreadsheet(name);
    }

    // returns the user registered in the application whose username is equal to username
    public User getUserFromUsername(String username) throws UserDoesNotExistException {
    	Portal p = Portal.getInstance();
    	return p.findUser(username);
    }

    // put a user into session and returns the token associated to it
    public String addUserToSession(String username) {
    	Portal p = Portal.getInstance();
    	Session s = Session.getInstance();

    	User u = p.findUser(username);
    	s.login(username, u.getPassword(), false);
    	return u.getToken();
    }

    // remove a user from session given its token
    public void removeUserFromSession(String token) {
    	Session s = Session.getInstance();
    	Portal p = Portal.getInstance();
    	
    	User u = p.findUserByToken(token);
    	s.logout(u);
    }

    // return the user registered in session whose token is equal to token
    public User getUserFromSession(String token) throws LoginBubbleDocsException {
    	Session s  = Session.getInstance();
    	User u = s.getLoggedUser(token);
    	return u;
    }

}