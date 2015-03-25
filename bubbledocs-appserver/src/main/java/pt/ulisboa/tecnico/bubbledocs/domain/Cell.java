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
    	this.setContent(new Literal(-1));
    	this.setIsProtected(false);
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

    	element.setAttribute("line", Integer.toString(getLine()));
       	element.setAttribute("column", Integer.toString(getColumn()));
    	
       	Content content = this.getContent();
       	element.addContent(content.exportToXML());
      	return element;
    }
    
    public void importFromXML (Element element) {
    	try {
    		this.setLine(element.getAttribute("line").getIntValue());
    		this.setColumn(element.getAttribute("column").getIntValue());
    	} catch (DataConversionException dce) {
    		throw new ImportDocumentException();
    	}
    	
    	this.setIsProtected(false);
    	
    	Content newContent;
    	Element content;
    	if ((content = element.getChild("reference"))!=null) {
    		newContent = new Reference();
    		Spreadsheet s = this.getSpreadsheet();
    		
    		try {
    			Cell c = s.getCell(element.getAttribute("line").getIntValue(), 
    					element.getAttribute("column").getIntValue());
    			newContent.setCell(c);
    		} catch (DataConversionException dce) {
    			throw new ImportDocumentException();
    		}
    	} else if ((content = element.getChild("add"))!=null) {
    		newContent = new Add();
    	} else if ((content = element.getChild("mult"))!=null) {
    		newContent = new Mult();
    	} else if ((content = element.getChild("div"))!=null) {
    		newContent = new Div();
    	} else if ((content = element.getChild("sub"))!=null) {
    		newContent = new Sub();
    	} else {
    		content = element.getChild("literal");
    		newContent = new Literal(-1);
    	}
    	
    	newContent.setCell(this);
    	newContent.importFromXML(content);
    	this.setContent(newContent);
    }
    
    /*public void importFromXML (Element element) {
		this.setName(element.getAttributeValue("name"));
		this.setOwner(element.getAttributeValue("owner"));
		Date date = new Date();
		String newDate = date.toString();
		this.setDate(newDate);
		
		try {
			this.setColumns(element.getAttribute("column").getIntValue());
			this.setLines(element.getAttribute("line").getIntValue());
		} catch (DataConversionException dce) {
			throw new ImportDocumentException();
		}
		
		Element cells = element.getChild("cell");
		for (Element c : cells.getChildren("cell")) {
			Cell newCell = new Cell();
			newCell.importFromXML(c);
			this.addCells(newCell);
		}
	}*/

    //public void importFromXML(Element cellElement) {
    	/*Element add = cellElement.getChild("add");
    	Element sub = cellElement.getChild("sub");
    	Element div = cellElement.getChild("div");
    	Element mult = cellElement.getChild("mult");
    	Element literal = cellElement.getChild("literal");
    	Element reference = cellElement.getChild("reference");
    	
    	if(add.getContent() instanceof Add){
    		Add addVariable = new Add(); 
    		addVariable.equals(cellElement.getChild("add").getContent());
    	}
    	else if(sub.getContent() instanceof Sub){
    		Sub subVariable = new Sub();
    		subVariable.equals(cellElement.getChild("sub").getContent());
    	}
    	else if(mult.getContent() instanceof Mult){
    		Mult multVariable = new Mult();
    		multVariable.importFromXML(mult);
    	}
    	else if(div.getContent() instanceof Div){
    		Div divVariable = new Div();
    		divVariable.importFromXML(div);
    	}
    	else if(reference.getContent() instanceof Reference){
    		Reference referenceVariable = new Reference();
    		referenceVariable.importFromXML(reference);
    	}
    	else{
    		Literal literalVariable = new Literal();
    		literalVariable.importFromXML(literal);
    		}
    	*/
    	
    /*try {
    		setColumn(cellElement.getAttribute("column").getIntValue());
    		setLine(cellElement.getAttribute("line").getIntValue());
    	} catch (DataConversionException e) { 
    		throw new ImportDocumentException();
    	}
        }*/
    
    public void delete() {
    	this.getContent().delete();
    	this.setSpreadsheet(null);
    }
    
}
