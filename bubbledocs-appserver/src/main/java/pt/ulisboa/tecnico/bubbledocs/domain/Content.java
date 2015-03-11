package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

public class Content extends Content_Base {
    
    public Content() {
        super();
    }
    
    public void importFromXML(Element contentElement) {
    	try {
    		setValue(contentElement.getAttribute("value").getDoubleValue());
    	} catch (DataConversionException e) { 
    		throw new ImportDocumentException();
    	}
    }

    public Element exportToXML() {
    	Element element = new Element("content");
    	element.setAttribute("value", Double.toString(getValue()));
    	
    	return element;
    }
    
}
