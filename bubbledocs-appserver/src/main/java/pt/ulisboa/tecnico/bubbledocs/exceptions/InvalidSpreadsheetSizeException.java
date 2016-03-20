package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidSpreadsheetSizeException extends BubbledocsException {		
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public InvalidSpreadsheetSizeException() {
        super();
    }
 
    public InvalidSpreadsheetSizeException (String message) {
        super ();
        this.message = "insert a valid spreadsheet size";
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