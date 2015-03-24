package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Literal;
import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Cell;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.PortalException;

public class AssignLiteralToCellService extends PortalService {
	private String result;
	private String accessUsername;
	private int docId;
	private String cellId;
	private String literal;

	public AssignLiteralToCellService(String accessUsername, int docId, String cellId,
			String literal) {
		this.accessUsername = accessUsername;
		this.docId = docId;
		this.cellId = cellId;
		this.literal = literal;

	}

	@Override
	protected void dispatch() throws PortalException {
		Portal portal = getPortal();
		Spreadsheet s = portal.findSpreadsheet(docId);
		User u = portal.findUser(accessUsername);
		Permission p = u.findPermission(accessUsername, docId);

		if (p.getWrite()) {
			String[] parts = cellId.split(";");
			int part1 = Integer.parseInt(parts[0]);
			int part2 = Integer.parseInt(parts[1]);
			Cell c = s.getCell(part1, part2);
			c.setContent(new Literal(Integer.parseInt(literal)));
		}

	}

	public final String getResult() {
		return result;
	}
}
