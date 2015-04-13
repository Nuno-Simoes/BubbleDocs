package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class AssignReferenceCellService extends PortalService {
	
    private String result;
	private String tokenUser;
	private int sheetId;
	private String cellId;
	private String reference;

    public AssignReferenceCellService(String tokenUser, int sheetId, String cellId,
            String reference) {
    	this.tokenUser = tokenUser;
    	this.sheetId = sheetId;
    	this.cellId = cellId;
    	this.reference = reference;
	
    }

    @Override
    protected void dispatch() throws InvalidPermissionException, LoginBubbleDocsException {
    	User u = getUser(tokenUser);
    	Portal p = Portal.getInstance();
    	Spreadsheet s = p.findSpreadsheet(sheetId);
    	Permission perm = u.findPermission(u.getUsername(), sheetId);
    	
    	if (p.isOwner(u, s) || perm.getWrite()) {
    		String string = cellId;
    		String[] parts = string.split(";");
    		int part1 = Integer.parseInt(parts[0]); 
    		int part2 = Integer.parseInt(parts[1]);
    		
    		String strings = reference;
    		String[] part = strings.split(";");
    		int part3, part4;
    		try {
    			part3 = Integer.parseInt(part[0]); 
    			part4 = Integer.parseInt(part[1]);
    		} catch (NumberFormatException nfe) {
    			throw new InvalidContentException();
    		}
    		
    		Cell c = s.getCell(part1, part2);
    		Cell refCell = s.getCell(part3, part4);
    		Reference ref = new Reference(refCell);

    		s.setContent(c.getLine(), c.getColumn() , ref);
    	
    	} else {
    		throw new InvalidPermissionException (u.getUsername());
    	}
  
    }

    public final String getResult() {
        return result;
    }
}