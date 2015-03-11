package pt.ulisboa.tecnico.bubbledocs.domain;

public class Add extends Add_Base {
    
    public Add() {
        super();
        
    }
    
    public Add(Argument firstArgument, Argument secondArgument){
    	super();
    	super.init(firstArgument, secondArgument);
    }
    
    @Override
    public void compute() {
    	Double firstArgument = this.getArgument1().getValue();
    	Double secondArgument = this.getArgument2().getValue();
    	Double result = firstArgument + secondArgument;
    	this.setValue(result);
    }
    
}
