package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

public class Reference extends Reference_Base {
    
    public Reference() {
        super();
    }
    
    public Reference (Cell cell) {
    	super();
    	init(cell);
    }
    
    protected void init (Cell cell) {
    	this.setReferencedCell(cell);
    }
    
    @Override
    public double getResult() {
    	Content content = this.getReferencedCell().getContent();
    	double result;
    	
    	if (content.equals("#VALUE")) {
    		result = Double.NaN;
    	} else {
    		result = content.getResult();
    	}
    	
    	return result;
    }
    
    public Element exportToXML() {
    	Element element = new Element("reference");
    	
    	element.setAttribute("line", Integer.toString(this.getReferencedCell().getLine()));
       	element.setAttribute("column", Integer.toString(this.getReferencedCell().getColumn()));
       	       	
      	return element;
    }
    
    public void importFromXML (Element element, Cell cell) {
    	
    	Spreadsheet s = cell.getSpreadsheet();
    	
    	try {
    		Cell c = s.getCell(element.getAttribute("line").getIntValue(), 
    				element.getAttribute("column").getIntValue());
    		this.setReferencedCell(c);
    	} catch (DataConversionException dce) {
    		throw new ImportDocumentException();
    	}
    }
    
    public void importFromXML (Element element) {
    	
    	Spreadsheet s = this.getCell().getSpreadsheet();
    	
    	try {
    		Cell c = s.getCell(element.getAttribute("line").getIntValue(), 
    				element.getAttribute("column").getIntValue());
    		this.setReferencedCell(c);
    	} catch (DataConversionException dce) {
    		throw new ImportDocumentException();
    	}
    }
    
    public void delete() {
    	this.setCell(null);
    	this.setReferencedCell(null);
    }
    
}
