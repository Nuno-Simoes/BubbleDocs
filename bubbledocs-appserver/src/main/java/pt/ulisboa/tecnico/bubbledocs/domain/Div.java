package pt.ulisboa.tecnico.bubbledocs.domain;

public class Div extends Div_Base {
    
    public Div() {
        super();
    }
    
    @Override
    public void compute() {
    	Double firstArgument = this.getArgument1().getValue();
    	Double secondArgument = this.getArgument2().getValue();
    	Double result = firstArgument / secondArgument;
    	this.setValue(result);
    }
    
}
