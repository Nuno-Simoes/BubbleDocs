package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class CreateSpreadsheetService extends PortalService {
	
	private String sheetName;
	private int lines;
	private int columns;
	private String userToken;
	
	public CreateSpreadsheetService(String sheetName, int lines, int columns
			, String userToken) {
		this.sheetName = sheetName;
		this.lines = lines;
		this.columns = columns;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws UserDoesNotExistException {
		User u = getUser(userToken);
		u.createSpreadsheet(sheetName, lines, columns);
	}
	
	
}