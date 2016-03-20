package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;

public class AssignLiteralToCellService extends BubbleDocsService {

	private String result;
	private String token;
	private int docId;
	private String cellId;
	private String literal;

	public AssignLiteralToCellService(String token, int docId,
			String cellId, String literal) {
		this.token = token;
		this.docId = docId;
		this.cellId = cellId;
		this.literal = literal;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException,
			InvalidContentException {
		
		User u = super.getUser(token);
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
