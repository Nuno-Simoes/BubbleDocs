package pt.ulisboa.tecnico.bubbledocs.integration.system;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignBinaryFunctionToCellIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignLiteralToCellIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignReferenceToCellIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.ExportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.GetSpreadsheetContentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.CreateDocumentService;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocumentService;
import pt.ulisboa.tecnico.bubbledocs.service.ImportDocumentService;
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
			idRemote.renewPassword(anyString);
			storeRemote.storeDocument(anyString, anyString, null);
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
		CreateDocumentService createSpreadsheet = 
				new CreateDocumentService(token, nameDocument, lines, columns);
		createSpreadsheet.execute();
		Spreadsheet document = createSpreadsheet.getResult();
		
		// Renew password
		RenewPasswordIntegrator renewPassword = new RenewPasswordIntegrator(token);
		renewPassword.execute();
		
		// Assign binary function
		AssignBinaryFunctionToCellIntegrator assignBinaryFunction = 
				new AssignBinaryFunctionToCellIntegrator(token, document.getId(),
						"1;1", "ADD(8,1)");
		assignBinaryFunction.execute();
				
		// Export
		
//		new Expectations(){{
//			storeRemote.loadDocument(anyString, anyString);
//			result = file;
//		}};
		
		// Import
		
		// Assign literal to cell
		
		// Assign reference to cell
		
		// Get spreadsheet content
		
		// Remove user
	}
	

}
