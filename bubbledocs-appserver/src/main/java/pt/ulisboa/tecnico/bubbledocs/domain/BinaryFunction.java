package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

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
    
    public void importFromXML (Element element) {
    	Element argument = element.getChild("arguments");
    	Element first = argument.getChildren().get(0);
    	Element second = argument.getChildren().get(1);
    	Argument newFirst;
    	Argument newSecond;
    	
    	if (first.getName().equals("reference")) {
    		newFirst = new Reference();
    		newFirst.importFromXML(first, this.getCell());
    	} else {
    		newFirst = new Literal();
    		newFirst.importFromXML(first);
    	}
    	
    	if (second.getName().equals("reference")) {
    		newSecond = new Reference();
        	newSecond.importFromXML(second, this.getCell());
    	} else {
    		newSecond = new Literal();
        	newSecond.importFromXML(second);
    	}
    	
    	this.setArgument1(newFirst);
    	this.setArgument2(newSecond);
    }
    
    public void delete() {
    	this.getArgument1().delete();
    	this.getArgument2().delete();
    	this.setCell(null);
    	this.setArgument1(null);
    	this.setArgument2(null);
    }
        
}
