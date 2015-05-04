package pt.ulisboa.tecnico.bubbledocs.integration;

import pt.ulisboa.tecnico.bubbledocs.service.AssignBinaryFunctionToCellService;

public class AssignBinaryFunctionToCellIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private int sheetId;
	private String cellId;
	private String binaryFunction;

	public AssignBinaryFunctionToCellIntegrator(String userToken, int sheetId, 
			String cellId, String binaryFunction) {
		this.userToken = userToken;
		this.sheetId = sheetId;
		this.cellId = cellId;
		this.binaryFunction = binaryFunction;
	}
	
	@Override
	protected void dispatch() throws Exception {
		AssignBinaryFunctionToCellService localService = 
				new AssignBinaryFunctionToCellService(userToken, sheetId, cellId,
						binaryFunction);
		localService.execute();
	}

}