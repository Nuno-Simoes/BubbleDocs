package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

public class Cell extends Cell_Base {
    
    public Cell() {
        super();
    }
    
    public Element exportToXML() {
    	Element element = new Element("cell");

    	element.setAttribute("isProtected", Boolean.toString(getIsProtected()));
    	element.setAttribute("column", Integer.toString(getColumn()));
		element.setAttribute("line", Integer.toString(getLine()));

    	/*deve faltar a funçao add content uma vez que ja existe a predefiniçao nao criou por cima
    	 * Element contentElement = new Element("content");
    	element.addContent(contentElement);

    	contentElement.addContent(Content.exportToXML());
    	 */
    	return element;
        }

        public void importFromXML(Element cellElement) {
    	try {
    		setIsProtected(cellElement.getAttribute("isProtected").getBooleanValue());
    		setColumn(cellElement.getAttribute("column").getIntValue());
    		setLine(cellElement.getAttribute("line").getIntValue());
    	} catch (DataConversionException e) { 
    		throw new ImportDocumentException();
    	}
    	  
    	/* Element content = cellElement.getChild("content");
    	
    	for (Element contentElement : content.getChildren("contact")) {
    	    Content c = new Content();
    	    c.importFromXML(contentElement);
    	    addContent(c);
    	}*/

        }
    
}
