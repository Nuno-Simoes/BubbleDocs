package pt.ulisboa.tecnico.bubbledocs.integration.system;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadsheetService;
import pt.ulisboa.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.ulisboa.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import mockit.Expectations;
import mockit.Mocked;

public class LocalSystemTest extends SystemTest {
	
	@Mocked IDRemoteServices idRemote;
	@Mocked StoreRemoteServices storeRemote;
	
	@Test
	public void test() {
				
		new Expectations(){{
			idRemote.loginUser(anyString, anyString);
			//idRemote.createUser(anyString, anyString);
			//idRemote.removeUser(anyString);
			//idRemote.renewPassword(anyString);
		}};
		
		// Login
		String usernameRoot = "root";
		String passwordRoot = "rootroot";
		
		LoginUserIntegrator login = new LoginUserIntegrator(usernameRoot,
				passwordRoot);
		login.execute();
		String tokenRoot = login.getResult();
		
		// Create user
		String username = "tolkien";
		String email = "tolkien@lotr.sh";
		String name = "J.R.R. Tolkien";
		String password = "Shire";
		
		CreateUserIntegrator createUser = new CreateUserIntegrator(tokenRoot,
				username, email, name);
		createUser.execute();
		
		// Login
		login = new LoginUserIntegrator(username, password);
		login.execute();
		String token = login.getResult();
		
		// ALTERAR QUANDO HOUVER INTEGRATOR
		// Create spreadsheet
		String nameDocument = "Lord of the Rings";
		int lines = 20;
		int columns = 13;
		CreateSpreadsheetService createSpreadsheet = 
				new CreateSpreadsheetService(token, nameDocument, lines,
						columns);
		createSpreadsheet.execute();
		Spreadsheet document = createSpreadsheet.getResult();
		
		// Renew password
	}
	

}
