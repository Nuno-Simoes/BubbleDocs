package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class LoginBubbleDocsException extends BubbledocsException{

	private static final long serialVersionUID = 1L;
	private String message;
	 
    public LoginBubbleDocsException() {
        super();
        this.message = "Invalid username or password";
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
