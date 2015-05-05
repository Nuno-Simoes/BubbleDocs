package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.GetSpreadsheetContentService;

public class GetSpreadsheetContentIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private int docId;

	public GetSpreadsheetContentIntegrator(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() {
		GetSpreadsheetContentService localService = new GetSpreadsheetContentService(userToken, docId);
		localService.execute();
	}

}