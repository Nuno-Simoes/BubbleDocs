package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class UserAlreadyExistsException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public UserAlreadyExistsException() {
        super();
    }
 
    public UserAlreadyExistsException (String message) {
        super ();
        this.message = "username " + message + " already taken";
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