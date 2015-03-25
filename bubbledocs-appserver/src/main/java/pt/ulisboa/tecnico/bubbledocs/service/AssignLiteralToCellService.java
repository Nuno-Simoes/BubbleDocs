package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.EmptyUsernameException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidContentException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.OutOfBoundsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.SpreadsheetDoesNotExistException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class AssignLiteralToCellService extends PortalService {
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
	protected void dispatch() throws EmptyUsernameException,
			UserDoesNotExistException, SpreadsheetDoesNotExistException,
			InvalidPermissionException {
		
		User u = super.getUser(accessUsername);

		try {
			Integer.parseInt(literal);
		} catch (NumberFormatException nfe) {
			throw new InvalidContentException();
		}

		if (u.getUsername().equals("")) {
			throw new EmptyUsernameException();
		}

		Portal portal = Portal.getInstance();
		Spreadsheet s = portal.findSpreadsheet(docId);
		if (s.equals(null)) {
			throw new SpreadsheetDoesNotExistException(Integer.toString(docId));
		}
		
		if (u.equals(null)) {
			throw new UserDoesNotExistException(u.getUsername());
		}
		Permission p = u.findPermission(u.getUsername(), docId);

		if (p.getWrite()) {
			String[] parts = cellId.split(";");
			int part1 = Integer.parseInt(parts[0]);
			int part2 = Integer.parseInt(parts[1]);
			Cell c = s.getCell(part1, part2);
			if (c.getIsProtected()) {
				throw new InvalidPermissionException();
			}
			if (c.equals(null)) {
				throw new OutOfBoundsException();
			}
			s.setContent(part1,part2,new Literal(Integer.parseInt(literal)));
		} else {
			throw new InvalidPermissionException(u.getUsername());
		}
	}

	public final String getResult() {
		return result;
	}
}
