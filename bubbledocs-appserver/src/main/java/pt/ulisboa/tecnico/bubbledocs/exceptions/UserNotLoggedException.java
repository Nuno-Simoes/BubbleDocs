package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class UserNotLoggedException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public UserNotLoggedException() {
        super();
    }
 
    public UserNotLoggedException (String message) {
        super ();
        this.message = "user " + message + " currently logged";
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