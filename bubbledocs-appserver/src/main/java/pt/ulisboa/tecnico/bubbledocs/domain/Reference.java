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
    	double result;
    	
    	if (content.equals("#VALUE")) {
    		result = Double.NaN;
    	} else {
    		result = content.getResult();
    	}
    	
    	return result;
    }
    
}
