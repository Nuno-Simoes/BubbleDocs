package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class ImportDocumentException extends BubbledocsException{
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public ImportDocumentException() {
        super();
        this.message = "Failed converting";
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