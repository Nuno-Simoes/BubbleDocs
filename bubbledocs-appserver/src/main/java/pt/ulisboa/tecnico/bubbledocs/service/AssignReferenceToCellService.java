package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Session;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.Reference;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidSessionException;

public class AssignReferenceToCellService extends BubbleDocsService {
	
    private String result;
	private String token;
	private int docId;
	private String cellId;
	private String reference;

    public AssignReferenceToCellService(String token, int docId, String cellId,
            String reference) {
    	this.token = token;
    	this.docId = docId;
    	this.cellId = cellId;
    	this.reference = reference;
	
    }

    @Override
    protected void dispatch() throws InvalidPermissionException, 
    		InvalidSessionException {
    	
    	User u = getUser(token);
    	Portal p = Portal.getInstance();
    	Session session = Session.getInstance();
    	Spreadsheet s = p.findSpreadsheet(docId);
    	Permission perm = u.findPermission(u.getUsername(), docId);
    	
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
