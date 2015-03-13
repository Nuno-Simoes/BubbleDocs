package pt.ulisboa.tecnico.bubbledocs.domain;

public class Mult extends Mult_Base {
    
    public Mult() {
        super();
    }
    
    public Mult (Argument firstArgument, Argument secondArgument) {
    	super();
    	super.init(firstArgument, secondArgument);
    }
    
    @Override
    public double getResult() {
    	Double firstArgument = this.getArgument1().getResult();
    	Double secondArgument = this.getArgument2().getResult();
    	Double result;
    	
    	if (Double.isNaN(firstArgument) || Double.isNaN(secondArgument)) {
    		result = Double.NaN;
    	} else {
    		result = firstArgument * secondArgument;
    	}
    	
    	return result;
    }
    
}
