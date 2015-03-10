package pt.ulisboa.tecnico.bubbledocs.domain;

public class Mult extends Mult_Base {
    
    public Mult() {
        super();
    }
    
    @Override
    public void compute() {
    	Double firstArgument = this.getArgument1().getValue();
    	Double secondArgument = this.getArgument2().getValue();
    	Double result = firstArgument * secondArgument;
    	this.setValue(result);
    }
    
}
