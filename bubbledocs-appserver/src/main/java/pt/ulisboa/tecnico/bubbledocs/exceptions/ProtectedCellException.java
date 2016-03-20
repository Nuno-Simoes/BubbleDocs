package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class ProtectedCellException extends BubbledocsException {

	private static final long serialVersionUID = 1L;
	private String message;
	 
    public ProtectedCellException() {
        super();
        this.message = "access to protected cell";
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
