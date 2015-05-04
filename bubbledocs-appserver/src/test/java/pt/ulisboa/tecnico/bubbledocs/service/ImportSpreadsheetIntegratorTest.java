package pt.ulisboa.tecnico.bubbledocs.service;

import static org.junit.Assert.assertEquals;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSessionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.ulisboa.tecnico.bubbledocs.integration.ImportSpreadsheetIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportSpreadsheetIntegratorTest extends BubbleDocsServiceTest {
	
	public String token;
	public byte[] spreadsheet;
	
	private static final String USERNAME = "poe";
	private static final String NAME = "Edgar Allan Poe";
	private static final String EMAIL = "egar.a.poe@tales.com";
	private static final String PASSWORD = "ligeia";
	
	private static final String INVALID_TOKEN = "not_a_token";
	
	private static final String DOC_NAME = "The Raven";
	private static final int DOC_ID = 13;
	private static final int DOC_LINES = 200;
	private static final int DOC_COLUMNS = 3;

	@Override
	public void populate4Test() {
		User user = new User(USERNAME, NAME, EMAIL);
		user.setPassword(PASSWORD);
		Spreadsheet sheet = new Spreadsheet(DOC_NAME, DOC_LINES, DOC_COLUMNS);
		sheet.setId(DOC_ID);
		sheet.setOwner(USERNAME);
		
		token = addUserToSession(USERNAME);
		
		ExportDocumentService service = new ExportDocumentService(USERNAME, DOC_ID);
		service.execute();
		spreadsheet = service.getResult();
	}
	
	@Test
	public void success() {
		Portal.getInstance().setSheetId(1);
		ImportSpreadsheetIntegrator integrator =
				new ImportSpreadsheetIntegrator(USERNAME, Integer.toString(DOC_ID));
		integrator.execute();
		Spreadsheet result = integrator.getResult();
		
		assertEquals(result.getName(), DOC_NAME);
		assertEquals(result.getLines(), DOC_LINES);
		assertEquals(result.getOwner(), USERNAME);
		assertEquals(result.getId(), 2);		
	}
	
	// Invalid token
	@Test(expected=InvalidSessionException.class)
	public void invalidToken() {
		ImportSpreadsheetIntegrator integrator = 
				new ImportSpreadsheetIntegrator(INVALID_TOKEN, "1");
		integrator.execute();
	}
	
	// Remote service fails
	@Mocked StoreRemoteServices remote;
	@Test(expected=UnavailableServiceException.class)
	public void unavailableService() {
		
		new Expectations() {{
			remote.loadDocument(anyString, anyString);
			result = new RemoteInvocationException();
		}};
		
		ImportSpreadsheetIntegrator integrator =
				new ImportSpreadsheetIntegrator (token, Integer.toString(DOC_ID));
		integrator.execute();
	}
	
	// Document never exported
	@Test(expected=UnavailableServiceException.class)
	public void docNotExported() {
		ImportSpreadsheetIntegrator integrator =
				new ImportSpreadsheetIntegrator (token, "5");
		integrator.execute();
	}
}
