package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.service.CreateSpreadsheetService;


public class CreateSpreadsheetIntegrator extends BubbleDocsIntegrator {

	private int lines;
	private int columns;
	private String sheetName;
	private String userToken;
	
	private Spreadsheet result;

	public CreateSpreadsheetIntegrator(String userToken, String sheetName,
			int lines, int columns) {
		this.sheetName = sheetName;
		this.lines = lines;
		this.columns = columns;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch(){
		CreateSpreadsheetService localService = 
				new CreateSpreadsheetService(userToken, sheetName, lines, columns);
		localService.execute();
		this.result = localService.getResult();
	}
	
	public Spreadsheet getResult() {
		return this.result;
	}
}
