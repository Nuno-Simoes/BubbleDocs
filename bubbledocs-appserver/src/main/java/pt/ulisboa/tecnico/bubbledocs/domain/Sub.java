package pt.ulisboa.tecnico.bubbledocs.domain;

public class Sub extends Sub_Base {
    
    public Sub() {
        super();
    }
    
    @Override
    public void compute() {
    	Double firstArgument = this.getArgument1().getValue();
    	Double secondArgument = this.getArgument2().getValue();
    	Double result = firstArgument - secondArgument;
    	this.setValue(result);
    }
    
}
