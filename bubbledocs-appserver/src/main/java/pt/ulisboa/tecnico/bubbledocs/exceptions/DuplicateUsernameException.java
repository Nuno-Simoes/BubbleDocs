package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class DuplicateUsernameException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public DuplicateUsernameException() {
        super();
    }
 
    public DuplicateUsernameException (String message) {
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
