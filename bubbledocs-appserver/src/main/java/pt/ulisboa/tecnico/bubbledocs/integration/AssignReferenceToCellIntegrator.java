package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.AssignReferenceToCellService;

public class AssignReferenceToCellIntegrator extends BubbleDocsIntegrator {

	private String accessUsername;
	private int docId;
	private String cellId;
	private String reference;

	public AssignReferenceToCellIntegrator(String accessUsername, int docId,
			String cellId, String reference) {
		this.accessUsername = accessUsername;
		this.docId = docId;
		this.cellId = cellId;
		this.reference = reference;
	}
	
	@Override
	protected void dispatch() {
		AssignReferenceToCellService localService = 
				new AssignReferenceToCellService(accessUsername, docId, cellId, reference);
		localService.execute();
	}

}