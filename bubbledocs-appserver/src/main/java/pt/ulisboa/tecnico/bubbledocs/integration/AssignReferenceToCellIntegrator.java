package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceToCellService;

public class AssignReferenceToCellIntegrator extends BubbleDocsIntegrator {

	private String token;
	private int docId;
	private String cellId;
	private String reference;

	public AssignReferenceToCellIntegrator(String token, int docId,
			String cellId, String reference) {
		this.token = token;
		this.docId = docId;
		this.cellId = cellId;
		this.reference = reference;
	}
	
	@Override
	protected void dispatch() {
		AssignReferenceToCellService localService = 
				new AssignReferenceToCellService(token, docId, cellId, reference);
		localService.execute();
	}

}