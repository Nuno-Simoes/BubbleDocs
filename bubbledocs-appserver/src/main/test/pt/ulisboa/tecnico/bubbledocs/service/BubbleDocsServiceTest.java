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
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

// add needed import declarations

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
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {
    }

    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the iniital state and checking that the service has the expected behavior
    User createUser(String username, String password, String name) 
    		throws UserAlreadyExistsException {
    	RootUser r = RootUser.getInstace();
    	r.addUser(username, name, password);
    }

    public SpreadSheet createSpreadSheet(User user, String name, int row,
            int column) {
    	user.createSpreadsheet(name, row, column);
    }

    // returns a spreadsheet whose name is equal to name
    public SpreadSheet getSpreadSheet(String name) 
    		throws SpreadsheetDoesNotExistException {
    	Portal p = Portal.getInstace();
    	p.findSpreadsheet(name);
    }

    // returns the user registered in the application whose username is equal to username
    public User getUserFromUsername(String username) throws UserDoesNotExistException {
    	Portal p = Portal.getInstance();
    	p.findUser(username);
    }

    // put a user into session and returns the token associated to it
    public String addUserToSession(String username) {
    	
    }

    // remove a user from session given its token
    public void removeUserFromSession(String token) {
    	Session s = Session.getInstance();
    	s.removeUser(token);
    	
    }

    // return the user registered in session whose token is equal to token
    public User getUserFromSession(String token) {
    	Session s = Session.getInstance();
    	User u = s.findUser(token);
    	return u;
    }

}
