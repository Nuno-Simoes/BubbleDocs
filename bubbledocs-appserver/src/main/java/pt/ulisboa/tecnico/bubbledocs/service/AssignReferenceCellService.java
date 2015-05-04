package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class AssignReferenceCellService extends BubbleDocsService {
	
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
    protected void dispatch() throws InvalidPermissionException,
    	LoginBubbleDocsException, InvalidSessionException {
    	
    	User u = getUser(tokenUser);
    	Portal p = Portal.getInstance();
    	Session session = Session.getInstance();
    	Spreadsheet s = p.findSpreadsheet(sheetId);
    	Permission perm = u.findPermission(u.getUsername(), sheetId);
    	
    	if (!session.isInSession(u)) {
    		throw new InvalidSessionException(u.getUsername());
    	}
    	
    	
    	if (p.isOwner(u, s) || perm.getWrite()) {
    		String string = cellId;
    		String strings = reference;
    		
    		Cell c = s.splitCellId(string);
    		Cell refCell = s.splitCellReference(strings);
    		Reference ref = new Reference(refCell);
    		
    		s.setContent(c.getLine(), c.getColumn() , ref);
    		result = c.getResult();
    	} else {
    		throw new InvalidPermissionException (u.getUsername());
    	}
  
    }

    public final String getResult() {
        return result;
    }
}
