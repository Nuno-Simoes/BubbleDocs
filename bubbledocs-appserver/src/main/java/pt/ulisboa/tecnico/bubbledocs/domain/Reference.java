package pt.ulisboa.tecnico.bubbledocs.domain;

public class Reference extends Reference_Base {
    
    public Reference() {
        super();
    }
    
    public Reference(Cell cell){
    	super ();
    	this.init (cell);
    }
    
    protected void init (Cell cell){
    	this.setCell(cell);
    }
    
    
}
