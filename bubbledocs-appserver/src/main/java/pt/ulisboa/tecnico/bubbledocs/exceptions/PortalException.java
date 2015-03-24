package pt.ulisboa.tecnico.bubbledocs.exceptions;

public abstract class PortalException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    public PortalException() {
        super();
    }
    
    public PortalException(String msg){
    	super(msg);
    }
    
}