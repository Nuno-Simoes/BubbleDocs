package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class DivisionByZeroException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public DivisionByZeroException() {
        super();
        this.message = "division by zero";
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