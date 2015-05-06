package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;

public class AssignLiteralToCellService extends BubbleDocsService {

	private String result;
	private String accessUsername;
	private int docId;
	private String cellId;
	private String literal;

	public AssignLiteralToCellService(String accessUsername, int docId,
			String cellId, String literal) {
		this.accessUsername = accessUsername;
		this.docId = docId;
		this.cellId = cellId;
		this.literal = literal;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException, 
	LoginBubbleDocsException, InvalidContentException, OutOfBoundsException {

		User u = super.getUser(accessUsername);
		Portal p = Portal.getInstance();
		Spreadsheet s = p.findSpreadsheet(docId);
		Permission perm = u.findPermission(u.getUsername(), docId);

		try {
			Integer.parseInt(literal);
		} catch (NumberFormatException nfe) {
			throw new InvalidContentException();
		}

		if (p.isOwner(u, s) || perm.getWrite()) {
			String part = cellId;
			Cell c = s.splitCellId(part);
			s.setContent(c.getLine(), c.getColumn(), new Literal(Integer.parseInt(literal)));
			result = c.getResult();
		} else {
			throw new InvalidPermissionException(u.getUsername());
		}
	}

	public final String getResult() {
		return result;
	}
}