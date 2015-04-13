package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Sub extends Sub_Base {
    
    public Sub() {
        super();
    }
    
    public Sub (Argument firstArgument, Argument secondArgument) {
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
    		result = firstArgument - secondArgument;
    	}
    	
    	return result;
    }
    
    public Element exportToXML() {
    	Element element = new Element("sub");
    	Element argumentsElement = new Element("arguments");
    	argumentsElement.addContent(this.getArgument1().exportToXML());
    	argumentsElement.addContent(this.getArgument2().exportToXML());
    	element.addContent(argumentsElement);
      	return element;
    }
    
}
