package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralToCellService;

public class AssignLiteralToCellIntegrator extends BubbleDocsIntegrator {

	private String token;
	private int docId;
	private String cellId;
	private String literal;

	public AssignLiteralToCellIntegrator(String token, int docId,
			String cellId, String literal) {
		this.token = token;
		this.docId = docId;
		this.cellId = cellId;
		this.literal = literal;
	}
	
	@Override
	protected void dispatch() {
		AssignLiteralToCellService localService = 
				new AssignLiteralToCellService(token, docId, cellId, literal);
		localService.execute();
	}

}
