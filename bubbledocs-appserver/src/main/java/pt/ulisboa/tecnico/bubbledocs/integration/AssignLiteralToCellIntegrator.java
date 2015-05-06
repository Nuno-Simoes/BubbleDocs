package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.AssignLiteralToCellService;

public class AssignLiteralToCellIntegrator extends BubbleDocsIntegrator {

	private String accessUsername;
	private int docId;
	private String cellId;
	private String literal;

	public AssignLiteralToCellIntegrator(String accessUsername, int docId,
			String cellId, String literal) {
		this.accessUsername = accessUsername;
		this.docId = docId;
		this.cellId = cellId;
		this.literal = literal;
	}
	
	@Override
	protected void dispatch() {
		AssignLiteralToCellService localService = 
				new AssignLiteralToCellService(accessUsername, docId, cellId, literal);
		localService.execute();
	}

}