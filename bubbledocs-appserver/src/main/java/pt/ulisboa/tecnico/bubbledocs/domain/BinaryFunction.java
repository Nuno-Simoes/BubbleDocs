package pt.ulisboa.tecnico.bubbledocs.domain;

public class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public BinaryFunction(Argument firstArgument, Argument secondArgument) {
    	super();
    	this.init(firstArgument, secondArgument);
    }
    
    protected void init(Argument firstArgument, Argument secondArgument) {
    	Literal zero = new Literal(0);
    	if (Double.isNaN(firstArgument.getResult())) {
    		this.setArgument1(zero);
    	} else {
    		this.setArgument1(firstArgument);
    	}
    	
    	if (Double.isNaN(secondArgument.getResult())) {
    		this.setArgument2(zero);
    	} else {
    		this.setArgument2(secondArgument);
    	}
    }
        
}
