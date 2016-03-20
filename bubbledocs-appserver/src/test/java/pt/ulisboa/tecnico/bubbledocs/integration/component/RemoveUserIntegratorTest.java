package pt.ulisboa.tecnico.bubbledocs.integration.component;

import static org.junit.Assert.*;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.integration.RemoveUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RemoveUserIntegratorTest extends BubbleDocsIntegratorTest {

	private static final String USERNAME_TO_DELETE = "smf";
	private static final String USERNAME_TO_DELETE_2 = "jose";
	private static final String NAME_TO_DELETE = "Sérgio Fernandes";
	private static final String EMAIL_TO_DELETE = "smf@smf.com";
	private static final String USERNAME = "ars";
	private static final String NAME = "António Rito Silva";
	private static final String EMAIL = "ars@ars.com";
	private static final String PASSWORD = "random";
	private static final String ROOT_USERNAME = "root";
	private static final String USERNAME_DOES_NOT_EXIST = "no-one";
	private static final String SPREADSHEET_NAME = "spread";
	private static final String NOT_LOGGED_USERNAME = "zecabra";
	private static final String NOT_LOGGED_NAME = "José Cabra";
	private static final String NOT_LOGGED_EMAIL = "zecabra@deixeitudoporela.pt";

	private String root;
	private String logzecabra;

	@Override
	public void populate4Test() {
		User ars = createUser(USERNAME, NAME, EMAIL);
		ars.setPassword(PASSWORD);

		User smf = createUser(USERNAME_TO_DELETE,
				NAME_TO_DELETE, EMAIL_TO_DELETE);
		smf.setPassword(PASSWORD);
		
		User jose = createUser(USERNAME_TO_DELETE_2, "jose", "jose@jose.jose");
		jose.setPassword(PASSWORD);

		User zecabra = createUser(NOT_LOGGED_USERNAME,
				NOT_LOGGED_NAME, NOT_LOGGED_EMAIL);

		createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

		logzecabra = zecabra.getToken();
		root = addUserToSession(ROOT_USERNAME);
	};

	@Test
	public void success() {
		boolean deletedUser = false;
		boolean deletedSpreadsheet = false;
				
		RemoveUserIntegrator service = new RemoveUserIntegrator(root, USERNAME_TO_DELETE);
		service.execute();
		
		try {
			getUserFromUsername(USERNAME_TO_DELETE);
		} catch (LoginBubbleDocsException une) {
			deletedUser = true;
		}
		
		assertTrue("user was not deleted", deletedUser);

		try {
			getSpreadSheet(SPREADSHEET_NAME);
		} catch (SpreadsheetDoesNotExistException sne) {
			deletedSpreadsheet = true;
		}
		
		assertTrue("User spreadsheets were not deleted", deletedSpreadsheet);        
	}

	/*
	 * accessUsername exists, is in session and is root toDeleteUsername exists
	 * and is in session Test if user and session are both deleted
	 */
	@Test
	public void successToDeleteIsInSession() {
		boolean deleted = false;
		String token = addUserToSession(USERNAME_TO_DELETE_2);

		RemoveUserIntegrator service = new RemoveUserIntegrator(root, USERNAME_TO_DELETE_2);
		service.execute();

		try { 
			getUserFromSession(token); 
		} catch (LoginBubbleDocsException le) {
			deleted = true;
		}
		
		assertTrue("Removed user but not removed from session", deleted);
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userNotLogged() {
		RemoveUserIntegrator service = new RemoveUserIntegrator(logzecabra,
				USERNAME_DOES_NOT_EXIST);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void userToDeleteDoesNotExist() {
		RemoveUserIntegrator service = 
				new RemoveUserIntegrator(root, USERNAME_DOES_NOT_EXIST);
		service.execute();
	}

	@Test(expected = InvalidPermissionException.class)
	public void notRootUser() {
		String ars = addUserToSession(USERNAME);

		RemoveUserIntegrator service = 
				new RemoveUserIntegrator(ars, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void rootNotInSession() {
		removeUserFromSession(root);

		RemoveUserIntegrator service = 
				new RemoveUserIntegrator(root, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void notInSessionAndNotRoot() {
		String ars = addUserToSession(USERNAME);
		removeUserFromSession(ars);

		RemoveUserIntegrator service = 
				new RemoveUserIntegrator(ars, USERNAME_TO_DELETE);
		service.execute();
	}

	@Test(expected = LoginBubbleDocsException.class)
	public void accessUserDoesNotExist() {
		RemoveUserIntegrator service = 
				new RemoveUserIntegrator(USERNAME_DOES_NOT_EXIST,
						USERNAME_TO_DELETE);
		service.execute();
	}

	@Test()
	public void unavailableService(@Mocked final IDRemoteServices remote) {
		Portal p = Portal.getInstance();

		new Expectations() {{
			remote.removeUser(anyString); 
			result = new RemoteInvocationException();
		}};

		try {
			new RemoveUserIntegrator(root, USERNAME_TO_DELETE).execute();
		} catch (UnavailableServiceException use) {
			assertEquals(USERNAME_TO_DELETE, 
					p.findUser(USERNAME_TO_DELETE).getUsername());
			assertEquals(NAME_TO_DELETE, 
					p.findUser(USERNAME_TO_DELETE).getName());
			assertEquals(EMAIL_TO_DELETE, 
					p.findUser(USERNAME_TO_DELETE).getEmail());
		}
	}
}