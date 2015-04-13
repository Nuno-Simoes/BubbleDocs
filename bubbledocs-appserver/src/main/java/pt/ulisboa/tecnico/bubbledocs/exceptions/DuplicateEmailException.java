package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class DuplicateEmailException extends BubbledocsException {

	private static final long serialVersionUID = 1L;
	private String message;
	 
    public DuplicateEmailException() {
        super();
    }
 
    public DuplicateEmailException (String message) {
        super ();
        this.message = "email " + message + " already taken";
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
