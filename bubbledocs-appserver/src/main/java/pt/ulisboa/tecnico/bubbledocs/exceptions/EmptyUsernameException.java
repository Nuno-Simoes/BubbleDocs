package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class EmptyUsernameException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public EmptyUsernameException() {
        super();
        this.message = "insert a valid username";
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