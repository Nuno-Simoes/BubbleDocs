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
    	this.setArgument1(firstArgument);
    	this.setArgument2(secondArgument);
    }
    
    public void compute(){}
    
}
