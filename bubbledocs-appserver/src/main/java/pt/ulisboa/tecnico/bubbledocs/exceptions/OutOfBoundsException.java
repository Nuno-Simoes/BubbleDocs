package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class OutOfBoundsException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public OutOfBoundsException() {
        super();
    }
 
    public OutOfBoundsException (String message) {
        super ();
        this.message = "cell in " + message + " is out of bounds";
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