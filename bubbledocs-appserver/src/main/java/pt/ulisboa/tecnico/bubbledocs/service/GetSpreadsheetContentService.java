package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;

public class GetSpreadsheetContentService extends BubbleDocsService {

	private String[][] result;
	private String userToken;
	private int docId;

	public GetSpreadsheetContentService(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() throws InvalidPermissionException {
		User u = super.getUser(userToken);
		Portal p = Portal.getInstance();
		Spreadsheet s = p.findSpreadsheet(docId);
		Permission perm = u.findPermission(u.getUsername(), s.getId());
		result = new String[s.getLines()+1][s.getColumns()+1];
		if(p.isOwner(u, s) || perm.getRead() || perm.getWrite()) {
			for(int i = 1; i <= s.getLines(); i++) {
				for(int j = 1; j <= s.getColumns(); j++) {
					if(s.getCell(i, j).getContent().equals(null) ||
							Double.isNaN(s.getCell(i, j).getContent().getResult())) {
						result[i][j] = "";
					} else {
						result[i][j] = String.valueOf(s.getCell(i, j).getContent().getResult());
					}
				}
			}
		} else {
			throw new InvalidPermissionException(u.getUsername());
		}
	}

	public final String[][] getResult() {
		return result;
	}

}