package pt.ulisboa.tecnico.bubbledocs.service;

import pt.ulisboa.tecnico.bubbledocs.domain.User;
import pt.ulisboa.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.ulisboa.tecnico.bubbledocs.exceptions.UserDoesNotExistException;

public class CreateDocumentService extends BubbleDocsService {
	
	private String sheetName;
	private int lines;
	private int columns;
	private String userToken;
	
	public CreateDocumentService(String userToken, String sheetName, int lines, 
			int columns) {
		this.sheetName = sheetName;
		this.lines = lines;
		this.columns = columns;
		this.userToken = userToken;
	}

	@Override
	protected void dispatch() throws UserDoesNotExistException, LoginBubbleDocsException {
		User u = super.getUser(userToken);
		
		u.createSpreadsheet(sheetName, lines, columns);
	}	
	
	public String getResult(){
		return this.sheetName;
	} 
}