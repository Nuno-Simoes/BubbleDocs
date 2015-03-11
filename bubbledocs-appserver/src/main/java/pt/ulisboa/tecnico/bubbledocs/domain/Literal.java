package pt.ulisboa.tecnico.bubbledocs.domain;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import pt.ulisboa.tecnico.bubbledocs.exceptions.ImportDocumentException;

public class Literal extends Literal_Base {
    
	public Literal() {
		super();
	}

	public Literal(double value) {
		super();
		this.init(value);
	}

	protected void init(double value) {
		this.setValue(value);
	}
	
	public void importFromXML(Element literalElement) {
		try {
			setLiteral(literalElement.getAttribute("value").getIntValue());
		} catch (DataConversionException e) { 
			throw new ImportDocumentException();
		}
	    }

	    public Element exportToXML() {
		Element element = new Element("literal");
		element.setAttribute("value", Double.toString(getLiteral()));		
		return element;
	    }
}
