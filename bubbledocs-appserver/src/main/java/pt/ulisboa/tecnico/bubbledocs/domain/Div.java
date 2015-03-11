package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ulisboa.tecnico.bubbledocs.exceptions.DivisionByZeroException;

public class Div extends Div_Base {
    
    public Div() {
        super();
    }
    
    @Override
    public void compute() throws DivisionByZeroException {
    	Double firstArgument = this.getArgument1().getValue();
    	Double secondArgument = this.getArgument2().getValue();
    	if(secondArgument == 0){
    		throw new DivisionByZeroException();
    	}
    	Double result = firstArgument / secondArgument;
    	this.setValue(result);
    }
    
}
