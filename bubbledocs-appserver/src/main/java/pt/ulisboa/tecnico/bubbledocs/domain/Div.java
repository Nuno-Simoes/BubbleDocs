package pt.ulisboa.tecnico.bubbledocs.domain;

import pt.ulisboa.tecnico.bubbledocs.exceptions.DivisionByZeroException;

public class Div extends Div_Base {
    
    public Div() {
        super();
    }
    
    public Div (Argument firstArgument, Argument secondArgument) {
    	super();
    	super.init(firstArgument, secondArgument);
    }
    
    @Override
    public double getResult() throws DivisionByZeroException {
    	Double firstArgument = this.getArgument1().getResult();
    	Double secondArgument = this.getArgument2().getResult();
    	if(secondArgument == 0){
    		throw new DivisionByZeroException();
    	}
    	Double result = firstArgument / secondArgument;
    	return result;
    }
    
}
