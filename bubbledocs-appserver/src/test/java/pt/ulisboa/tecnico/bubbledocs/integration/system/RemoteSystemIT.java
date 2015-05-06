package pt.ulisboa.tecnico.bubbledocs.integration.system;

import org.junit.Test;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignBinaryFunctionToCellIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignLiteralToCellIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.AssignReferenceToCellIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.GetSpreadsheetContentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.ImportDocumentIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.LoginUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.RemoveUserIntegrator;
import pt.ulisboa.tecnico.bubbledocs.integration.RenewPasswordIntegrator;
import pt.ulisboa.tecnico.bubbledocs.service.CreateDocumentService;
import pt.ulisboa.tecnico.bubbledocs.service.ExportDocumentService;

public class RemoteSystemIT extends SystemTest {
	
	@Test
	public void test() {
		
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
		int lines = 5;
		int columns = 4;
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
		ExportDocumentService exportDocument =
				new ExportDocumentService(token, document.getId());
		exportDocument.execute();
		
		// Import
		ImportDocumentIntegrator importDocument = 
				new ImportDocumentIntegrator(token, Integer.toString(document.getId()));
		importDocument.execute();
		document = importDocument.getResult();
		
		// Assign literal to cell
		AssignLiteralToCellIntegrator assignLiteral =
				new AssignLiteralToCellIntegrator(token, document.getId(),
						"2;2", "4");
		assignLiteral.execute();
		
		// Assign reference to cell
		AssignReferenceToCellIntegrator assignReference =
				new AssignReferenceToCellIntegrator(token, document.getId(),
						"3;3", "2;2");
		assignReference.execute();
		
		// Get spreadsheet content
		GetSpreadsheetContentIntegrator getSpreadsheetContent =
				new GetSpreadsheetContentIntegrator(token, document.getId());
		getSpreadsheetContent.execute();

		// Remove user
		RemoveUserIntegrator removeUser = new RemoveUserIntegrator(tokenRoot, username);
		removeUser.execute();
	}
}
