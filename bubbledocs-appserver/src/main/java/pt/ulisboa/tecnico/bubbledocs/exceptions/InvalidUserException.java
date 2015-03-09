package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidUserException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public InvalidUserException() {
        super();
    }
 
    public InvalidUserException (String message) {
        super ();
        this.message = "invalid " + message + " user";
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