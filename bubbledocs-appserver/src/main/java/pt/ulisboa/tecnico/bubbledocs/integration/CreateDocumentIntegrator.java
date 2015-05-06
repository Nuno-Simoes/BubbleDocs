package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.CreateDocumentService;


public class CreateDocumentIntegrator extends BubbleDocsIntegrator {

	private int lines;
	private int columns;
	private String sheetName;
	private String userToken;

	public CreateDocumentIntegrator(String userToken, String sheetName,
			int lines, int columns) {
		this.sheetName = sheetName;
		this.lines = lines;
		this.columns = columns;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch(){
		CreateDocumentService localService = new CreateDocumentService(
				userToken, sheetName, lines, columns);
		localService.execute();
	}
}
