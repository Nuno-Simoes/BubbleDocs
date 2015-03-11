package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

public class Cell extends Cell_Base {
    
    public Cell() {
        super();
    }
    
    public Cell(int line , int column){
    	super();
    	this.init(line, column);
    }
    
    protected void init (int line, int column){
    	this.setLine(line);
    	this.setColumn(column);
    	this.setContent(new Literal((int) Double.NaN));
    }
    
    public String getResult() {
    	double result = this.getContent().getResult();
    	if (Double.isNaN(result)) {
    		return "#VALUE";
    	} else {
    		return Double.toString(result);
    	}
    }
    
    public Element exportToXML() {
    	Element element = new Element("cell");

       	element.setAttribute("column", Integer.toString(getColumn()));
		element.setAttribute("line", Integer.toString(getLine()));
    	
    	Element referenceElement = new Element("reference");
		element.addContent(referenceElement);
		
		Element addElement = new Element("add");
		element.addContent(addElement);
		
		Element subElement = new Element("sub");
		element.addContent(subElement);
		
		Element divElement = new Element("div");
		element.addContent(divElement);
		
		Element multElement = new Element("mult");
		element.addContent(multElement);
		
		Element literalElement = new Element("literal");
		element.addContent(literalElement);
    	
    	return element;
    }

    public void importFromXML(Element cellElement) {
    	Element add = cellElement.getChild("add");
    	Element sub = cellElement.getChild("sub");
    	Element div = cellElement.getChild("div");
    	Element mult = cellElement.getChild("mult");
    	Element literal = cellElement.getChild("literal");
    	Element reference = cellElement.getChild("reference");
    	
    	if(add.getContent() instanceof Add){
    		/*Add addVariable = new Add(); 
    		addVariable.equals(cellElement.getChild("add").getContent());
    	*/}
    	else if(sub.getContent() instanceof Sub){
    		/*Sub subVariable = new Sub();
    		subVariable.equals(cellElement.getChild("sub").getContent());
    	*/}
    	else if(mult.getContent() instanceof Mult){
    		/*Mult multVariable = new Mult();
    		multVariable.importFromXML(mult);
    	*/}
    	else if(div.getContent() instanceof Div){
    		/*Div divVariable = new Div();
    		divVariable.importFromXML(div);
    	*/}
    	else if(reference.getContent() instanceof Reference){
    		/*Reference referenceVariable = new Reference();
    		referenceVariable.importFromXML(reference);*/
    	}
    	else{
    		Literal literalVariable = new Literal();
    		literalVariable.importFromXML(literal);
    		}
    	
    	
    try {
    		setColumn(cellElement.getAttribute("column").getIntValue());
    		setLine(cellElement.getAttribute("line").getIntValue());
    	} catch (DataConversionException e) { 
    		throw new ImportDocumentException();
    	}
        }
    
}
