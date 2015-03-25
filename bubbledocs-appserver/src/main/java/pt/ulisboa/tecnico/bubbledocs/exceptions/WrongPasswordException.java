package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class WrongPasswordException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public WrongPasswordException() {
        super();
    }
 
    public WrongPasswordException (String message) {
        super ();
        this.message = message + ": incorrect password ";
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
