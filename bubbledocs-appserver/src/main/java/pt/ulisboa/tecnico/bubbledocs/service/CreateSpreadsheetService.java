package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.Spreadsheet;
import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class CreateSpreadsheetService extends BubbleDocsService {
	
	private int lines;
	private int columns;
	private String sheetName;
	private String userToken;
	private Spreadsheet result;
	
	public CreateSpreadsheetService(String userToken, String sheetName, int lines, 
			int columns) {
		this.sheetName = sheetName;
		this.lines = lines;
		this.columns = columns;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws UserDoesNotExistException, LoginBubbleDocsException {
		User u = super.getUser(userToken);
		this.result = u.createSpreadsheet(sheetName, lines, columns);
	}
	
	public Spreadsheet getResult() {
		return this.result;
	} 
}