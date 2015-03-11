package pt.ulisboa.tecnico.bubbledocs.domain;

public class Sub extends Sub_Base {
    
    public Sub() {
        super();
    }
    
    public Sub(Argument firstArgument, Argument secondArgument){
    	super();
    	super.init(firstArgument, secondArgument);
    }
    
    @Override
    public void compute() {
    	Double firstArgument = this.getArgument1().getValue();
    	Double secondArgument = this.getArgument2().getValue();
    	Double result = firstArgument - secondArgument;
    	this.setValue(result);
    }
    
}
