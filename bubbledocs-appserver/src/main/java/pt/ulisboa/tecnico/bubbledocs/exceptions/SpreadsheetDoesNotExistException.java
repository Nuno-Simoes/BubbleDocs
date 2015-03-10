package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class SpreadsheetDoesNotExistException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public SpreadsheetDoesNotExistException() {
        super();
    }
 
    public SpreadsheetDoesNotExistException (String message) {
        super ();
        this.message = "spreadsheet with id " + message + " not found";
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