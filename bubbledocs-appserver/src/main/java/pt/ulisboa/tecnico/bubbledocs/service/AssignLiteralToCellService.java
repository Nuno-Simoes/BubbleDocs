package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

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
	protected void dispatch() throws InvalidUsernameException,
			UserDoesNotExistException, SpreadsheetDoesNotExistException,
			InvalidPermissionException, LoginBubbleDocsException {
		
		User u = super.getUser(token);

		try {
			Integer.parseInt(literal);
		} catch (NumberFormatException nfe) {
			throw new InvalidContentException();
		}

		if (u.getUsername().equals("")) {
			throw new InvalidUsernameException();
		}

		Portal portal = Portal.getInstance();
		Spreadsheet s = portal.findSpreadsheet(docId);
		
		if (u.equals(null)) {
			throw new UserDoesNotExistException(u.getUsername());
		}
		Permission p = u.findPermission(u.getUsername(), docId);

		if (p.getWrite()) {
			String part = cellId;
			Cell c = s.splitCellId(part);
			if (c.getIsProtected()) {
				throw new InvalidPermissionException();
			}
			if (c.equals(null)) {
				throw new OutOfBoundsException();
			}
			s.setContent(c.getLine(), c.getColumn(), new Literal(Integer.parseInt(literal)));
		} else {
			throw new InvalidPermissionException(u.getUsername());
		}
	}

	public final String getResult() {
		return result;
	}
}
