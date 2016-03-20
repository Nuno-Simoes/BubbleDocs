package pt.ulisboa.tecnico.bubbledocs.exceptions;

public class ExportDocumentException extends BubbledocsException {
	
	private static final long serialVersionUID = 1L;
	private String message;
	 
    public ExportDocumentException() {
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
