package pt.ulisboa.tecnico.bubbledocs.domain;

public class Add extends Add_Base {
    
    public Add() {
        super();
    }
    
    public Add (Argument firstArgument, Argument secondArgument) {
    	super();
    	super.init(firstArgument, secondArgument);
    }
    
    @Override
    public double getResult() {
    	Double firstArgument = this.getArgument1().getResult();
    	Double secondArgument = this.getArgument2().getResult();
    	Double result = firstArgument + secondArgument;
    	return result;
    }
    
}
