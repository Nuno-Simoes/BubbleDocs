package pt.ulisboa.tecnico.bubbledocs.domain;

public class Reference extends Reference_Base {
    
    public Reference() {
        super();
    }
    
    public Reference (Cell cell) {
    	super();
    	init(cell);
    }
    
    protected void init (Cell cell) {
    	this.setCell(cell);
    }
    
    @Override
    public double getResult() {
    	Content content = this.getCell().getContent();
    	double result = content.getResult();
    	return result;
    }
    
}
