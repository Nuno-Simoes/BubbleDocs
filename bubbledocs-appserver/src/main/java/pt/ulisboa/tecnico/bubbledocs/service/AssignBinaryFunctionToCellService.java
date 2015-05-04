package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;


public class AssignBinaryFunctionToCellService extends PortalService {

	private String userToken;
	private int sheetId;
	private String cellId;
	private String binaryFunction;
	private String result;

	public AssignBinaryFunctionToCellService(String userToken, int sheetId, String cellId,
			String binaryFunction) {
		this.userToken = userToken;
		this.sheetId = sheetId;
		this.cellId = cellId;
		this.binaryFunction = binaryFunction;
	}
	
	@Override
	protected void dispatch() {
		User u = getUser(userToken);
    	Portal p = Portal.getInstance();
    	Spreadsheet spread = p.findSpreadsheet(sheetId);
    	Permission perm = u.findPermission(u.getUsername(), sheetId);
    	
    	if (p.isOwner(u, spread) || perm.getWrite()) {
    		String cellString = cellId;
    		
    		Cell c = spread.splitCellId(cellString);
    		c.setContent(spread.parseBinaryFunction(binaryFunction));
    		result = c.getResult();
    	} else {
    		throw new InvalidPermissionException (u.getUsername());
    	}
	}
	
	public final String getResult() {
		return result;
	}
}
