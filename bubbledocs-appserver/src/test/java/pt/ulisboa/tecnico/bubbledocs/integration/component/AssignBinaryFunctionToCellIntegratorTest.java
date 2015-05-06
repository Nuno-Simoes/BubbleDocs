package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.DivisionByZeroException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ProtectedCellException;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignBinaryFunctionToCellIntegrator;

public class AssignBinaryFunctionToCellIntegratorTest extends BubbleDocsIntegratorTest {
	
	private String validToken;
	private String noPermissionToken;
	private String invalidSessionToken;
	private Spreadsheet validDoc;
	
	// - valid user
	private static final String USERNAME = "Humbug";
	private static final String NAME = "AM";
	private static final String EMAIL = "favourite@worst.nightmare";
	private static final String PASSWORD = "WPSIATWIN";
	
	// - user with no permission
	private static final String NO_PERMISSION_USERNAME = "QOTSA";
	private static final String NO_PERMISSION_NAME = "Josh Homme";
	private static final String NO_PERMISSION_EMAIL = "homme@joshuatree.ca";
	private static final String NO_PERMISSION_PASSWORD = "tail";
	
	// - user not logged
	private static final String NOT_LOGGED_USERNAME = "Kane";
	private static final String NOT_LOGGED_NAME = "Miles Kane";
	private static final String NOT_LOGGED_EMAIL = "kane@lsp.co.uk";
	private static final String NOT_LOGGED_PASSWORD = "kingcrawler";
	
	// - valid spreadsheet
	private static final int DOC_ID = 2013;
	private static final String DOC_NAME = "Suck it and See";
	
	// - valid cell
	private static final String CELL = "1;1";
	
	// - invalid cells
	private static final String PROTECTED_CELL = "2;2";
	private static final String CELL_OUT_OF_BOUNDS = "60;60";
	
	// - valid functions
	private static final String FUNCTION_1 = "ADD(1,2)";
	private static final String FUNCTION_2 = "ADD(9,1;2)";
	private static final String FUNCTION_3 = "ADD(1;4,1;5)";
	private static final String FUNCTION_4 = "SUB(7,3)";
	private static final String FUNCTION_5 = "SUB(2,5)";
	private static final String FUNCTION_6 = "MULT(0,6)";
	private static final String FUNCTION_7 = "DIV(10,5)";
	private static final String FUNCTION_8 = "DIV(9,2)";
	
	// - invalid functions
	private static final String INVALID_FUNCTION_1 = "DIV(3,0)";
	private static final String INVALID_FUNCTION_2 = "SUM(4,4)";
	private static final String INVALID_FUNCTION_3 = "ADD()";
	private static final String INVALID_FUNCTION_4 = "ADD(1)";
	private static final String INVALID_FUNCTION_5 = "ADD(1,2,3)";
	private static final String INVALID_FUNCTION_6 = "ADD(five,six)";
	private static final String INVALID_FUNCTION_7 = "ADD(2,5;invalid)";
	
	@Override
	public void populate4Test() {
		User validUser = createUser(USERNAME, NAME, EMAIL);
		validUser.setPassword(PASSWORD);
		validToken = addUserToSession(USERNAME);
		
		User noPermissionUser = createUser(NO_PERMISSION_USERNAME,
				NO_PERMISSION_NAME, NO_PERMISSION_EMAIL);
		noPermissionUser.setPassword(NO_PERMISSION_PASSWORD);
		noPermissionToken = addUserToSession(NO_PERMISSION_USERNAME);
		
		User notLoggedUser = createUser(NOT_LOGGED_USERNAME,
				NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
		notLoggedUser.setPassword(NOT_LOGGED_PASSWORD);
		invalidSessionToken = notLoggedUser.getToken();
		
		validDoc = createSpreadSheet(validUser, DOC_NAME, 50, 50);
		validDoc.setId(DOC_ID);
		validDoc.getCell(1,2).setContent(new Literal(6));
		validDoc.getCell(1, 4).setContent(new Literal(4));
		validDoc.getCell(1, 5).setContent(new Literal(5));
		validDoc.getCell(2, 2).setIsProtected(true);
		validUser.modifyPermissions(NOT_LOGGED_USERNAME, DOC_ID, true, true);
	}
	
	// Successful test 1 - add two literals
	@Test
	public void add1() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_1);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 3, 0);
	}
	
	// Successful test 2 - add literal with reference
	@Test
	public void add2() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_2);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 15, 0);
	}
	
	// Successful test 3 - add two references
	@Test
	public void add3() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_3);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 9, 0);
	}
	
	// Successful test 4 - sub
	@Test
	public void sub1() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_4);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 4, 0);
	}
	
	// Successful test 5 - sub with negative result
	@Test
	public void sub2() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_5);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), -3, 0);
	}
	
	// Successful test 6 - multiplication
	@Test
	public void mult() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_6);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 0, 0);
	}
	
	// Successful test 7 - division
	@Test
	public void div1() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_7);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 2, 0);
	}
	
	// Successful test 7 - division
	@Test
	public void div2() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, FUNCTION_8);
		integrator.execute();
		assertEquals(validDoc.getCell(1, 1).getContent().getResult(), 4.5, 0);
	}
	
	// Unsuccessful test 1 - protected cell
	@Test(expected=ProtectedCellException.class)
	public void protectedCell() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, PROTECTED_CELL, FUNCTION_1);
		integrator.execute();
	}
	
	// Unsuccessful test 2 - division by zero
	@Test(expected=DivisionByZeroException.class)
	public void divisionByZero() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_1);
		integrator.execute();
	}
	
	// Unsuccessful test 3 - invalid function name
	@Test(expected=InvalidContentException.class)
	public void invalidFunctionName() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_2);
		integrator.execute();
	}
	
	// Unsuccessful test 4 - empty arguments
	@Test(expected=InvalidContentException.class)
	public void emptyArguments() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_3);
		integrator.execute();
	}
	
	// Unsuccessful test 5 - invalid number of arguments
	@Test(expected=InvalidContentException.class)
	public void invalidNumberOfArguments() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_4);
		integrator.execute();
	}
	
	// Unsuccessful test 6 - invalid number of arguments
	@Test(expected=InvalidContentException.class)
	public void invalidNumberOfArguments2() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_5);
		integrator.execute();
	}
	
	// Unsuccessful test 7 - cell out of bounds
	@Test(expected=OutOfBoundsException.class)
	public void cellOutOfBounds() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL_OUT_OF_BOUNDS, FUNCTION_1);
		integrator.execute();
	}
	
	// Unsuccessful test 8 - invalid arguments
	@Test(expected=InvalidContentException.class)
	public void invalidArguments1() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_6);
		integrator.execute();
	}
	
	// Unsuccessful test 8 - invalid arguments
	@Test(expected=InvalidContentException.class)
	public void invalidArguments2() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(validToken,
						DOC_ID, CELL, INVALID_FUNCTION_7);
		integrator.execute();
	}
	
	// Unsuccessful test 9 - user without permission
	@Test(expected=InvalidPermissionException.class)
	public void invalidPermission() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(noPermissionToken, 
				DOC_ID, CELL, FUNCTION_1);
		integrator.execute();
	}
	
	// Unsuccessful test 10 - invalid session
	@Test(expected=LoginBubbleDocsException.class)
	public void invalidToken() {
		AssignBinaryFunctionToCellIntegrator integrator =
				new AssignBinaryFunctionToCellIntegrator(invalidSessionToken, 
				DOC_ID, CELL, FUNCTION_1);
		integrator.execute();		
	}

}
