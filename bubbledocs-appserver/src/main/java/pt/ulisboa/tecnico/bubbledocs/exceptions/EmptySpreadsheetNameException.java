package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class EmptySpreadsheetNameException extends BubbledocsException {
	private static final long serialVersionUID = 1L;
	private String message;
	 
	public EmptySpreadsheetNameException() {
		super();
		this.message = "insert a valid spreadsheet name";
	}
	
	@Override
	public String toString() {
	    return message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}