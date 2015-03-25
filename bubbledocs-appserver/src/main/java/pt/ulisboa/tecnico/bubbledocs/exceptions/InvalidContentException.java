package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class InvalidContentException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public InvalidContentException() {
        super();
        this.message = "insert a valid content";
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