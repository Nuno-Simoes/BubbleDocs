package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Permission;
import pt.ulisboa.tecnico.bubbledocs.domain.Portal;
import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.BubbledocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.InvalidPermissionException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;

public class GetSpreadsheetContentService extends BubbleDocsService {

	private String[][] result;
	private String userToken;
	private int docId;

	public GetSpreadsheetContentService(String userToken, int docId) {
		this.userToken = userToken;
		this.docId = docId;
	}

	@Override
	protected void dispatch() throws BubbledocsException, InvalidPermissionException, 
	LoginBubbleDocsException {
		User u = super.getUser(userToken);
		Portal p = Portal.getInstance();
		Spreadsheet s = p.findSpreadsheet(docId);
		Permission perm = u.findPermission(u.getUsername(), s.getId());
		result = new String[s.getLines()][s.getColumns()];
		if(p.isOwner(u, s) || perm.getRead() || perm.getWrite()) {
			for(int i = 0; i < s.getLines(); i++) {
				for(int j = 0; j < s.getColumns(); j++) {
					if(s.getCell(i, j).getContent().equals(null)) {
						result[i][j] = "";
					}
					result[i][j] = s.getCell(i, j).getContent().toString();
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