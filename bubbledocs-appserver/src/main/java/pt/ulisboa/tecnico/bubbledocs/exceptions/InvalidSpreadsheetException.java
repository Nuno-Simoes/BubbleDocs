package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidSpreadsheetException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public InvalidSpreadsheetException() {
        super();
    }
 
    public InvalidSpreadsheetException (String message) {
        super ();
        this.message = "spreadsheet " + message + " not found";
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