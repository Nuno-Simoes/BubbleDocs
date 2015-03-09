package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class UserException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public UserException() {
        super();
    }
 
    public UserException (String message) {
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