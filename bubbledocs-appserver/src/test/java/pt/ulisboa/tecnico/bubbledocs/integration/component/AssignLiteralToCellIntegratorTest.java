package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.ProtectedCellException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignLiteralToCellIntegrator;

public class AssignLiteralToCellIntegratorTest extends BubbleDocsIntegratorTest {

	private String userToken;
	private String noPermUserToken;
	private String notLoggedUserToken;
	private int idSheetValidUser;
	private int idSheetNotLoggedUser;

	private static final String USERNAME = "ars";
	private static final String NAME = "António Rito Silva";
	private static final String EMAIL = "ars@ars.com";

	private static final String NO_PERMISSION_USERNAME = "sam";
	private static final String NO_PERMISSION_NAME = "Sam";
	private static final String NO_PERMISSION_EMAIL = "sam@sam.com";

	private static final String NOT_LOGGED_USERNAME = "preso44";
	private static final String NOT_LOGGED_NAME = "José Sócrates";
	private static final String NOT_LOGGED_EMAIL = "preso44@evora.pt";

	private static final String PASSWORD = "ContasSuica";

	private static final String SPREADNAME = "whatever";
	private static final String SPREADNAME2 = "whatever2";
	private static final int ID_DOES_NOT_EXIST = 100;

	@Override
	public void populate4Test() {
		User user = createUser(USERNAME, NAME, EMAIL);
		user.setPassword(PASSWORD);

		User noPermUser = createUser(NO_PERMISSION_USERNAME, NO_PERMISSION_NAME, 
				NO_PERMISSION_EMAIL);
		noPermUser.setPassword(PASSWORD);

		User notLoggedUser = createUser(NOT_LOGGED_USERNAME, NOT_LOGGED_NAME,
				NOT_LOGGED_EMAIL);
		notLoggedUser.setPassword(PASSWORD);

		Spreadsheet sheetValidUser = createSpreadSheet(user, SPREADNAME, 10, 10);
		Spreadsheet sheetNotLoggedUser = createSpreadSheet(notLoggedUser, SPREADNAME2, 
				10, 100);

		sheetValidUser.getCell(7, 3).setIsProtected(true);

		idSheetValidUser = sheetValidUser.getId();
		idSheetNotLoggedUser = sheetNotLoggedUser.getId();

		userToken = addUserToSession(USERNAME);
		noPermUserToken = addUserToSession(NO_PERMISSION_USERNAME);

		notLoggedUserToken = notLoggedUser.getToken();
	}

	@Test
	public void success() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(userToken, idSheetValidUser, 
						"1;5", "3");
		integrator.execute();

		Spreadsheet sheet = super.getSpreadSheet(SPREADNAME);

		assertEquals(3, sheet.getCell(1, 5).getContent().getResult(), 0);
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userNotLogged() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(notLoggedUserToken, idSheetNotLoggedUser, 
						"1;5", "3");
		integrator.execute();
	}

	@Test(expected = SpreadsheetDoesNotExistException.class)
	public void spreadsheetDoesNotExist() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(userToken, ID_DOES_NOT_EXIST, 
						"1;5", "3");
		integrator.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void invalidUser() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(noPermUserToken, idSheetValidUser, 
						"1;5", "3");
		integrator.execute();
	}

	@Test(expected = OutOfBoundsException.class)
	public void outOfBounds() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(userToken, idSheetValidUser, 
						"19;5", "3");
		integrator.execute();
	}

	@Test(expected = InvalidContentException.class)
	public void notALiteral() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(userToken, idSheetValidUser, 
						"1;5", "ola");
		integrator.execute();
	}

	@Test(expected = ProtectedCellException.class)
	public void protectedCell() {
		AssignLiteralToCellIntegrator integrator = new 
				AssignLiteralToCellIntegrator(userToken, idSheetValidUser, 
						"7;3", "3");
		integrator.execute();
	}
}