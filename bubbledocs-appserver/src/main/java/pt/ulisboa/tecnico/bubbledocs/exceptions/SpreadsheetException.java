package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class SpreadsheetException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public SpreadsheetException() {
        super();
    }
 
    public SpreadsheetException (String message) {
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