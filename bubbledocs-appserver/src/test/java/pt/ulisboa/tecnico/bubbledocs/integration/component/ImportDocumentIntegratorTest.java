package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.CannotLoadDocumentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSessionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportDocumentIntegratorTest extends BubbleDocsIntegratorTest {

	private String validToken;
	private String invalidToken;
	private String notLoggedToken;
	public byte[] document;
	public Spreadsheet theRaven;

	// - valid user
	private static final String USERNAME = "poe";
	private static final String NAME = "Edgar Allan Poe";
	private static final String EMAIL = "egar.a.poe@tales.com";
	private static final String PASSWORD = "ligeia";

	// - invalid user
	private static final String INVALID_USERNAME = "love";
	private static final String INVALID_NAME = "H.P. Lovecraft";
	private static final String INVALID_EMAIL = "hplove@tales.com";
	private static final String INVALID_PASSWORD = "horror";

	// - expired session
	private static final String NOT_LOGGED_USERNAME = "walter";
	private static final String NOT_LOGGED_NAME = "Mr. White";
	private static final String NOT_LOGGED_EMAIL = "walter@caravan";
	private static final String NOT_LOGGED_PASSWORD = "jesse";

	// - document
	private static final String DOC_NAME = "The Raven";
	private static final int DOC_ID = 13;
	private static final int DOC_LINES = 200;
	private static final int DOC_COLUMNS = 3;

	@Override
	public void populate4Test() {
		User validUser = createUser(USERNAME, NAME, EMAIL);
		validUser.setPassword(PASSWORD);

		User invalidUser = createUser(INVALID_USERNAME,
				INVALID_NAME, INVALID_EMAIL);
		invalidUser.setPassword(INVALID_PASSWORD);

		User notLoggedUser = createUser(NOT_LOGGED_USERNAME,
				NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);
		notLoggedUser.setPassword(NOT_LOGGED_PASSWORD);

		Spreadsheet theRaven = new Spreadsheet(DOC_NAME, DOC_LINES, DOC_COLUMNS);
		theRaven.setId(DOC_ID);
		theRaven.setOwner(USERNAME);
		Portal.getInstance().addSpreadsheets(theRaven);
		Permission permission = new Permission(true, true);
		theRaven.addPermissions(permission);
		validUser.addPermissions(permission);

		validToken = addUserToSession(USERNAME);
		invalidToken = addUserToSession(INVALID_USERNAME);
		notLoggedToken = addUserToSession(NOT_LOGGED_USERNAME);

		notLoggedUser.setSessionTime(0);

		ExportDocumentIntegrator service = new ExportDocumentIntegrator(validToken, DOC_ID, USERNAME, DOC_NAME);
		service.execute();
		document = service.getResult();
	}

	@Test
	public void success() {

		Portal.getInstance().setSheetId(1);
		ImportDocumentIntegrator integrator = 
				new ImportDocumentIntegrator(validToken, DOC_NAME);
		integrator.execute();
		Spreadsheet result = integrator.getResult();

		assertEquals(result.getName(), DOC_NAME);
		assertEquals(result.getLines(), DOC_LINES);
		assertEquals(result.getOwner(), USERNAME);
		assertEquals(result.getId(), 2);		
	}

	@Test(expected=InvalidSessionException.class)
	public void notLoggedUser() {
		ImportDocumentIntegrator integrator =
				new ImportDocumentIntegrator(notLoggedToken, DOC_NAME);
		integrator.execute();
	}

	@Test(expected=CannotLoadDocumentException.class)
	public void documentNotExported() {

		ImportDocumentIntegrator integrator =
				new ImportDocumentIntegrator(invalidToken, DOC_NAME);
		integrator.execute();
	}
	
	@Test(expected=UnavailableServiceException.class)
	public void unavailableService(@Mocked final StoreRemoteServices remote) {

		new Expectations() {{
			remote.loadDocument(anyString, anyString);
			result = new RemoteInvocationException();
		}};

		ImportDocumentIntegrator integrator =
				new ImportDocumentIntegrator(validToken, DOC_NAME);
		integrator.execute();
	}
}