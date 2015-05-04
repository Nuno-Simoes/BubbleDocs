package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidSessionException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public InvalidSessionException() {
        super();
    }
 
    public InvalidSessionException (String message) {
        super ();
        this.message = "user " + message + " is not in session";
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