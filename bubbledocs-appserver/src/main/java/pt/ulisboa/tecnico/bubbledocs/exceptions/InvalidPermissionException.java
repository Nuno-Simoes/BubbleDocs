package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidPermissionException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public InvalidPermissionException() {
        super();
    }
 
    public InvalidPermissionException (String message) {
        super ();
        this.message = message + " does not have permission to perform access";
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