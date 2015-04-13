package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class UserDoesNotExistException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public UserDoesNotExistException() {
        super();
    }
 
    public UserDoesNotExistException (String message) {
        super ();
        this.message = "user " + message + " not found";
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